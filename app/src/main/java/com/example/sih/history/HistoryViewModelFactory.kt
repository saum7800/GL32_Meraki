package com.example.sih.history

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sih.database.ScoreDatabaseDao
import java.lang.IllegalArgumentException


class HistoryViewModelFactory (
    private val dataSource: ScoreDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory{
    @Suppress("unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HistoryViewModel::class.java)){
            return HistoryViewModel(dataSource,application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}