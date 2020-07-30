package com.example.sih.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sih.database.ScoreDatabaseDao
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.*

class HistoryViewModel(
    private val database: ScoreDatabaseDao,
    application: Application,
    private val chart : BarChart
):
        AndroidViewModel(application){

    private lateinit var set : BarDataSet
    private var xValues : MutableList<String> = mutableListOf()
    private var scores : MutableList<BarEntry> = mutableListOf()
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    fun getScores(date: String){

        uiScope.launch {
            getScoresDB(date)

            chart.xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            set = BarDataSet(scores,"BarDataSet")
            set.valueTextSize = 12f
            chart.data = BarData(set)
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

    private suspend fun getScoresDB(date : String){
        return withContext(Dispatchers.IO){
            val temp =database.getScoreByDate(date)
            var i= 0
            for (t in temp){
                xValues.add(t.id)
                scores.add(BarEntry(i.toFloat(), t.score.toFloat()))
                i+=1
            }

        }
    }

}