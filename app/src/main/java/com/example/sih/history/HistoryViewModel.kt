package com.example.sih.history

import android.app.Application
import android.content.ContentValues.TAG
import android.graphics.Color
import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sih.database.MyConverters
import com.example.sih.database.ScoreDatabaseDao
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.*

class HistoryViewModel(
    private val database: ScoreDatabaseDao,
    application: Application,
    private val chart : LineChart

):
        AndroidViewModel(application){

    private val _students = MutableLiveData<ArrayList<String>>()
    private var myConverters=MyConverters()
    val students : LiveData<ArrayList<String>>
        get() = _students
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    init {
        uiScope.launch {
            withContext(Dispatchers.IO){
                Log.i("mine","got cursor in launch")
                val cursor = database.getSimilarDates("%02-08-2020%")

                Log.i("mine","got cursor $cursor")
                val temp : ArrayList<String> = arrayListOf()
                if(cursor.moveToFirst()){
                    Log.i("mine","got cursor move to fr $cursor")
                    do{
                        val student = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                        Log.i("mine","got cursor student $student")
                       temp.add(student)
                    }while (cursor.moveToNext())
                    cursor.close()
                    Log.i("mine","got cursor temp file $temp")
                    _students.postValue(temp)
                }
            }
        }
    }

    fun getScores(date: String){

        uiScope.launch {
            Log.i("mine","into getscore")


            val xValues : MutableList<String> = mutableListOf()
            val scores : MutableList<Entry> = mutableListOf()
            getScoresDB(date,xValues,scores)
            if(xValues.isNotEmpty()) {


                chart.xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
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

    private suspend fun getScoresDB(student : String, xValues : MutableList<String>, scores : MutableList<Entry> ){
        return withContext(Dispatchers.IO){
           val tem =database.getScoreByDate(student)
            val temp = myConverters.fromStringToList(tem)
            var i= 0
            if (temp != null) {
                for (t in temp){
                    xValues.add(i.toString())
                    scores.add(Entry(i.toFloat(), t.toFloat()))
                    i+=1
                }
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}