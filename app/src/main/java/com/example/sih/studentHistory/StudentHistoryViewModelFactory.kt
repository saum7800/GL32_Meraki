package com.example.sih.studentHistory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sih.database.ScoreDatabaseDao
import com.github.mikephil.charting.charts.BarChart
import java.lang.IllegalArgumentException


class StudentHistoryViewModelFactory (
    private val dataSource: ScoreDatabaseDao,
    private val application: Application,
    private val chart : BarChart
) : ViewModelProvider.Factory{
    @Suppress("unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StudentHistoryViewModel::class.java)){
            return StudentHistoryViewModel(dataSource,application,chart) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}