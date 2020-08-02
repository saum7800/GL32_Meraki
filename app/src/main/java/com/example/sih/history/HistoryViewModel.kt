package com.example.sih.history

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sih.database.MyConverters
import com.example.sih.database.ScoreDatabaseDao
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.*

class HistoryViewModel(
    private val database: ScoreDatabaseDao,
    application: Application,
    private val chart : LineChart

): AndroidViewModel(application){

    private var myConverters=MyConverters()
    private val _students = MutableLiveData<ArrayList<String>>()
    val students : LiveData<ArrayList<String>>
        get() = _students
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    init {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val cursor = database.getSimilarDates("%02-08-2020%")
                val temp : ArrayList<String> = arrayListOf()
                temp.add(" ")
                if(cursor.moveToFirst()){
                    do{
                        val student = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                       temp.add(student)
                    }while (cursor.moveToNext())
                    cursor.close()
                    _students.postValue(temp)
                }
            }
        }
    }

    fun getScores(date: String){

        uiScope.launch {

            val xValues : MutableList<String> = mutableListOf()
            val scores : MutableList<Entry> = mutableListOf()
            plotScoresDB(date,xValues,scores)
            }

        }



    private suspend fun plotScoresDB(date : String, xValues : MutableList<String>, scores : MutableList<Entry> ){
        return withContext(Dispatchers.IO){
           val temp =database.getScoreByDate(date)
            val list = myConverters.fromStringToList(temp)
            var i= 0
            if (list != null) {
                for (t in list){
                    xValues.add(i.toString())
                    scores.add(Entry(i.toFloat(), t.toFloat()))
                    i+=1
                }

                //chart.xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
                chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                val set = LineDataSet(scores, "Student History")
                set.valueTextSize = 12f
                set.enableDashedHighlightLine(10f, 5f, 0f)
                set.setColors(Color.parseColor("#6200EE"))
                set.setCircleColor(Color.parseColor("#6200EE"))
                chart.data = LineData(set)
                val xl = chart.xAxis
                xl.labelCount = xValues.size
                xl.setDrawGridLines(false)
                xl.setAvoidFirstLastClipping(true)
                val yl = chart.axisLeft
                yl.setDrawGridLines(false)
                val yr = chart.axisRight
                yr.setDrawGridLines(false)
                chart.invalidate()
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}