package com.example.sih.history

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sih.database.ScoreDatabaseDao
import com.github.mikephil.charting.charts.BarChart
import java.lang.IllegalArgumentException


class HistoryViewModelFactory (
    private val dataSource: ScoreDatabaseDao,
    private val application: Application,
    private val chart : BarChart
) : ViewModelProvider.Factory{
    @Suppress("unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HistoryViewModel::class.java)){
            return HistoryViewModel(dataSource,application,chart) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}