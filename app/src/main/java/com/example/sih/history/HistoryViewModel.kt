package com.example.sih.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val chart : BarChart
):
        AndroidViewModel(application){

    private val _students = MutableLiveData<ArrayList<String>>()
    val students : LiveData<ArrayList<String>>
        get() = _students
    private var xValues : MutableList<String> = mutableListOf()
    private var scores : MutableList<BarEntry> = mutableListOf()
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    init {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val cursor  = database.getNames()
                val temp : ArrayList<String> = arrayListOf()
                temp.add(" ")
                if(cursor.moveToFirst()){
                    do{
                        val student = cursor.getString(cursor.getColumnIndexOrThrow("name"))
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
            getScoresDB(date)
            if(xValues.isNotEmpty()) {

                Log.d("History",scores.toString())
                chart.setDrawValueAboveBar(true)
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
                val set = BarDataSet(scores, "Student History")
                set.valueTextSize = 12f
                chart.data = BarData(set)
                chart.axisLeft.axisMinimum
                chart.axisRight.axisMinimum
                chart.setPinchZoom(false)
                val xl = chart.xAxis
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

    private suspend fun getScoresDB(student : String){
        return withContext(Dispatchers.IO){
            val temp =database.getScoreByStudent(student)
            var i= 0
            for (t in temp){
                xValues.add(t.id)
                scores.add(BarEntry(i.toFloat(), t.score.toFloat()))
                i+=1
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}