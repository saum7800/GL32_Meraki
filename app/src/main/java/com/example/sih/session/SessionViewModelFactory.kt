package com.example.sih.session

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sih.database.ScoreDatabaseDao
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarDataSet
import java.lang.IllegalArgumentException

class SessionViewModelFactory (
    private val dataSource: ScoreDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory{
    @Suppress("unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SessionViewModel::class.java)){
            return SessionViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}