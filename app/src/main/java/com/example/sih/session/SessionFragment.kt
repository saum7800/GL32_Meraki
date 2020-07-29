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

        readFireBase()

        return binding.root
    }

    fun readFireBase(){

        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<String>()
                Log.d(TAG, "Value is : $value")
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
                Log.w(TAG, "Failed to read value", error.toException())

            }
        })

    }

}