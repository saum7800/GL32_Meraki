package com.example.sih.studentHistory

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sih.database.ScoreDatabaseDao
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.*

class StudentHistoryViewModel(
    private val database: ScoreDatabaseDao,
    application: Application,
    private val chart : BarChart
):
    AndroidViewModel(application){

    private val _dates = MutableLiveData<ArrayList<String>>()
    val dates : LiveData<ArrayList<String>>
        get() = _dates
    private lateinit var set : BarDataSet
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}