package com.example.sih.sessionHistory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sih.database.ScoreDatabaseDao
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.*

class SessionHistoryViewModel(
    private val database: ScoreDatabaseDao,
    application: Application,
    private val chart : BarChart
):
    AndroidViewModel(application){

    private val _dates = MutableLiveData<ArrayList<String>>()
    val dates : LiveData<ArrayList<String>>
        get() = _dates
    private lateinit var set : BarDataSet
    private var xValues : MutableList<String> = mutableListOf()
    private var scores : MutableList<BarEntry> = mutableListOf()
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    init {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val cursor  = database.getDates()
                val temp : ArrayList<String> = arrayListOf()
                temp.add(" ")
                if(cursor.moveToFirst()){
                    do{
                        val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                        temp.add(date)
                    }while (cursor.moveToNext())
                    cursor.close()
                    _dates.postValue(temp)
                }
            }
        }
    }


    fun getScores(date: String){

        uiScope.launch {
            getScoresDB(date)
            if(xValues.isNotEmpty()) {
                chart.setDrawValueAboveBar(true)
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
                chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                set = BarDataSet(scores, "Session History")
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

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
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