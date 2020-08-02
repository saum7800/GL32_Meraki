package com.example.sih.session

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.example.sih.database.MyConverters
import com.example.sih.database.ScoreDatabaseDao
import com.example.sih.database.StudentScore
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SessionViewModel(private val database: ScoreDatabaseDao,
                       application: Application

): AndroidViewModel(application){

    private var myConverters=MyConverters()
    var teachName = ""
    private lateinit var set : BarDataSet
    private val fireBaseDatabase = Firebase.database
    private var teacherRef = fireBaseDatabase.reference.child("nameList").child("teacher_name")
    private var statusRef =  fireBaseDatabase.reference.child("online")
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var  sessionId : String? = null
    private var scores = mutableListOf<Long>()

    private val currScores = StudentScore("","")

    private val _drowsy = MutableLiveData<MutableList<Student?>>()
    val drowsy : LiveData<MutableList<Student?>>
        get() = _drowsy

    private val _inattentive = MutableLiveData<MutableList<Student?>>()
    val inattentive : LiveData<MutableList<Student?>>
        get() = _inattentive

    private val _attentive = MutableLiveData<MutableList<Student?>>()
    val attentive : LiveData<MutableList<Student?>>
        get() = _attentive

    private val _interactive = MutableLiveData<MutableList<Student?>>()
    val interactive : LiveData<MutableList<Student?>>
        get() = _interactive


    private fun clearDatabase(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                database.clear()
            }
        }
    }

    init {
        _drowsy.value= mutableListOf()
        _inattentive.value= mutableListOf()
        _attentive.value= mutableListOf()
        _interactive.value= mutableListOf()
    }


    fun saveHistory(){
        Log.d("History","History being saved")
        uiScope.launch {
            withContext(Dispatchers.IO){
                currScores.id= sessionId as String
                currScores.studentScore=myConverters.listToString(scores)
                database.insert(currScores)
            }
        }
    }

    fun readFireBase(){

        uiScope.launch {
            withContext(Dispatchers.IO){

                statusRef.addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        Log.w(TAG, "Failed to read value", error.toException())
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val v  = snapshot.getValue<String>()
                        if (v != null) {
                            sessionId = v
                            makeData()
                        }
                        else{
                            saveHistory()
                        }
                    }

                })
            }
        }
    }


    fun makeData(){

        val scoreRef = fireBaseDatabase.reference.child("means_score")
        val myRef = fireBaseDatabase.reference.child("Teacher")

        val childEventListener = object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value", error.toException())
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val value = snapshot.key?.let { Student(it) }
                var toDelete = _drowsy.value
                toDelete?.remove(value)
                _drowsy.postValue(toDelete)
                toDelete = _inattentive.value
                toDelete?.remove(value)
                _inattentive.postValue(toDelete)
                toDelete = _attentive.value
                toDelete?.remove(value)
                _attentive.postValue(toDelete)
                toDelete = _interactive.value
                toDelete?.remove(value)
                _interactive.postValue(toDelete)

                when(snapshot.getValue<Long>()){
                    0L -> {

                        toDelete = _drowsy.value
                        toDelete?.add(value)
                        _drowsy.postValue(toDelete)

                    }
                    1L -> {
                        toDelete = _inattentive.value
                        toDelete?.add(value)
                        _inattentive.postValue(toDelete)
                    }
                    2L -> {
                        toDelete = _attentive.value
                        toDelete?.add(value)
                        _attentive.postValue(toDelete)
                    }
                    else -> {
                        toDelete = _interactive.value
                        toDelete?.add(value)
                        _interactive.postValue(toDelete)
                    }
                }
            }


            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val value = snapshot.key?.let { Student(it) }
                Log.d("Category",value.toString()+"-"+snapshot.value.toString())
                when(snapshot.getValue<Long>()){
                    0L -> {
                        val temp = _drowsy.value
                        temp?.add(value)
                        _drowsy.postValue(temp)
                    }
                    1L -> {
                        val temp = _inattentive.value
                        temp?.add(value)
                        _inattentive.postValue(temp)
                    }
                    2L -> {
                        val temp = _attentive.value
                        temp?.add(value)
                        _attentive.postValue(temp)
                    }
                    else -> {
                        val temp = _interactive.value
                        temp?.add(value)
                        _interactive.postValue(temp)
                    }
                }
                //Log.d(TAG, "Value is : $value, $k")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

        }

        myRef.addChildEventListener(childEventListener)

        scoreRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value", error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val score = snapshot.getValue<Long>()
                if (score != null) {
                    scores.add(score)
                }
                //Log.d(TAG, "Value is score : $score")
            }

        })
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
