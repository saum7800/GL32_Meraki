package com.example.sih.sessionHistory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sih.database.ScoreDatabaseDao
import com.example.sih.session.SessionViewModel
import com.github.mikephil.charting.charts.BarChart
import java.lang.IllegalArgumentException


class SessionHistoryViewModelFactory (
    private val dataSource: ScoreDatabaseDao,
    private val application: Application,
    private val chart : BarChart
) : ViewModelProvider.Factory{
    @Suppress("unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SessionHistoryViewModel::class.java)){
            return SessionHistoryViewModel(dataSource,application,chart) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}