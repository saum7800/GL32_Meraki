package com.example.sih.session

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.example.sih.database.ScoreDatabaseDao
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

class SessionViewModel(private val database: ScoreDatabaseDao,
                       application: Application,
                       private val chart : BarChart
): AndroidViewModel(application){

    var teachName = ""
    private lateinit var set : BarDataSet
    private val fireBaseDatabase = Firebase.database
    private var teacherRef = fireBaseDatabase.reference.child("nameList").child("teacher_name")
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var scores : MutableList<BarEntry> = mutableListOf()
    private var students : HashMap<String, Float> = hashMapOf()
    var index = 0

    init {
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.animateY(3000)
        val xl = chart.xAxis
        xl.labelCount=5
        xl.setDrawGridLines(false)
        xl.setAvoidFirstLastClipping(true)
        val yl = chart.axisLeft
        yl.setDrawGridLines(false)
        val yr = chart.axisRight
        yr.setDrawGridLines(false)
        chart.invalidate()
    }

    private fun plot(){
        chart.refreshDrawableState()
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(students.keys)
        set = BarDataSet(scores, "Student Set")
        chart.data = BarData(set)
        chart.notifyDataSetChanged()
        chart.invalidate()

    }

    fun readFireBase(){

        uiScope.launch {
            withContext(Dispatchers.IO){

                teacherRef.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        //TODO("Not yet implemented")
                        Log.w(TAG, "Failed to read value", error.toException())
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        //TODO("Not yet implemented")
                        val v = snapshot.getValue<HashMap<String, String>>()?.values
                        if (v != null) {
                            for (va in v){
                                teachName = va.toString()
                            }
                        }
                        makeData()
                        Log.d(TAG, "Value is : $teachName")
                    }

                })
            }
            }
        }


    fun makeData(){

        var myRef = fireBaseDatabase.reference.child("nameList").child("teacher_name")
        myRef = fireBaseDatabase.reference.child(teachName.toString())
        val movingScores = HashMap<String, Double>()

        val childEventListener = object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value", error.toException())
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val value = snapshot.key
                val k = snapshot.getValue<Double>()
                if(k != null){
                    if (value != null) {
                        val oldAvg : Long = movingScores[value.toString()]?.toLong() ?: 0
                        val avg  = k.toDouble()
                        movingScores[value.toString()] = avg
                        scores[students[value.toString()]?.toInt()!!]=
                            students[value.toString()]?.let { BarEntry(it,avg.toFloat()) }!!
                        plot()
                    }
                }
                Log.d(TAG, "Value is : $value, $k")
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val value = snapshot.key
                val k = snapshot.getValue<Double>()
                if(k != null){
                    val avg = k.toDouble()
                    movingScores[value.toString()] = avg
                    students[value.toString()] = index.toFloat()
                    students[value.toString()]?.let { BarEntry(it,avg.toFloat()) }?.let {
                        scores.add(
                            it
                        )
                    }
                    index+=1
                    plot()
                }
                Log.d(TAG, "Value is : $value, $k")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

        }
        myRef.addChildEventListener(childEventListener)
    }


}
