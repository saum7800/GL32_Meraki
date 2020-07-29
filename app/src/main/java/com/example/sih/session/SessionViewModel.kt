package com.example.sih.session

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.anychart.chart.common.dataentry.DataEntry
import com.example.sih.database.ScoreDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class SessionViewModel( private val database: ScoreDatabaseDao,
application: Application
): AndroidViewModel(application){

    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
}