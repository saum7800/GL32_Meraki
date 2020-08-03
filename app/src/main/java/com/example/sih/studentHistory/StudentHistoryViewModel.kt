package com.example.sih.studentHistory

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sih.database.ScoreDatabaseDao
import com.example.sih.database.StudentDatabaseDao
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.*
import kotlin.math.roundToInt

class StudentHistoryViewModel(
    private val database: StudentDatabaseDao,
    application: Application,
    private val name : String
):
    AndroidViewModel(application){

    private val _sessions = MutableLiveData<List<StudentHist>>()
    val session : LiveData<List<StudentHist>>
        get() = _sessions
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    init {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val x = database.getSessionByStudent(name)
                val temp = mutableListOf<StudentHist>()
                for(t in x){
                    val total = t.drowsy + t.attentive + t.inattentive + t.interactive
                    val obj = StudentHist(
                        t.id.trim().split(',').first(), t.id.trim().split(',').last(),
                        getPercent(t.drowsy,total),getPercent(t.inattentive,total),
                        getPercent(t.attentive,total),getPercent(t.interactive,total)
                    )
                    temp.add(obj)
                }
                _sessions.postValue(temp)
            }
        }
    }

    private fun getPercent(x : Int, t : Int) : String{
        return (x * 100f / t).roundToInt().toString()+'%'
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}