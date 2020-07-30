package com.example.sih.session

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.sih.R
import com.example.sih.database.ScoreDatabase
import com.example.sih.databinding.FragmentSessionPlotBinding
import com.example.sih.history.HistoryViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SessionFragment : Fragment() {

    private lateinit var binding : FragmentSessionPlotBinding
    private lateinit var viewModel: SessionViewModel
    private lateinit var viewModelFactory: SessionViewModelFactory
    var teachName = ""
    lateinit var score : HashMap<String, ArrayList<Long>>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_session_plot, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = ScoreDatabase.getInstance(application).scoreDatabaseDao
        viewModelFactory = SessionViewModelFactory(dataSource,application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(SessionViewModel::class.java)
        binding.lifecycleOwner = this
        score = HashMap<String, ArrayList<Long>>()

        readFireBase()

        return binding.root
    }

    fun readFireBase(){

        val database = Firebase.database
        var myRef = database.reference.child("nameList").child("teacher_name")

        myRef.addListenerForSingleValueEvent(object: ValueEventListener{
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

    fun makeData(){
        val database = Firebase.database
        var myRef = database.reference.child("nameList").child("teacher_name")
        myRef = database.reference.child(teachName.toString())
        var scores = HashMap<String, Long>()

        val childEventListener = object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value", error.toException())
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val value = snapshot.key
                val k = snapshot.getValue<ArrayList<Long>>()
                if(k != null){
                    score[value]?.add(k[k.size - 1])
                }
                Log.d(TAG, "Value is : $value, $k")
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val value = snapshot.key
                val k = snapshot.getValue<ArrayList<Long>>()
                if(k != null){
                    if(!scores.containsKey(value)){
                        score.put(value.toString(), k)
                    }
                    else{
                        score[value.toString()] = k
                    }
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
