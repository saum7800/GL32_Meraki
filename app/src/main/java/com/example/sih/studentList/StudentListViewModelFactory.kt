package com.example.sih.studentList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sih.database.StudentDatabaseDao
import com.example.sih.studentHistory.StudentHistoryViewModel
import java.lang.IllegalArgumentException

class StudentListViewModelFactory (
    private val dataSource: StudentDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory{
    @Suppress("unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StudentListViewModel::class.java)){
            return StudentListViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}