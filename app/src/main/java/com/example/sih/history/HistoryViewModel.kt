package com.example.sih.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.sih.database.ScoreDatabaseDao
import kotlinx.coroutines.*

class HistoryViewModel(
    private val database: ScoreDatabaseDao,
    application: Application):
        AndroidViewModel(application){

    lateinit var scores : MutableList<DataEntry>
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    private var _notesReceived = MutableLiveData<Boolean>()
    val notesReceived : LiveData<Boolean>
        get() = _notesReceived

    fun getScores(date: String){

        uiScope.launch {
            scores = getScoresDB(date)
            _notesReceived.value = true
        }

    }

    fun graphPlotted(){
        _notesReceived.value=false
    }

    private suspend fun getScoresDB(date : String): MutableList<DataEntry>{
        return withContext(Dispatchers.IO){
            val temp =database.getScoreByDate(date)
            val scores :MutableList<DataEntry> = mutableListOf()
            for (t in temp){
                scores.add(ValueDataEntry(t.id,t.score))
            }
            scores
        }
    }

}