package com.example.sih.studentHistory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sih.database.ScoreDatabaseDao
import com.example.sih.database.StudentDatabaseDao
import com.github.mikephil.charting.charts.BarChart
import java.lang.IllegalArgumentException


class StudentHistoryViewModelFactory (
    private val dataSource: StudentDatabaseDao,
    private val application: Application,
    private val name : String
) : ViewModelProvider.Factory{
    @Suppress("unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StudentHistoryViewModel::class.java)){
            return StudentHistoryViewModel(dataSource,application,name) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}