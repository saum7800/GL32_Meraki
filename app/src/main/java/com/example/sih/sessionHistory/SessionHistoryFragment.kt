package com.example.sih.sessionHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sih.R
import com.example.sih.database.ScoreDatabase
import com.example.sih.databinding.FragmentHistoryBinding
import com.example.sih.databinding.FragmentSessionHistoryBinding
import com.example.sih.history.HistoryViewModel
import com.example.sih.history.HistoryViewModelFactory
import com.github.mikephil.charting.charts.LineChart

class SessionHistoryFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding : FragmentSessionHistoryBinding
    private lateinit var viewModel: SessionHistoryViewModel
    private lateinit var viewModelFactory : SessionHistoryViewModelFactory
    private lateinit var chart : LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_session_history,container,false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = ScoreDatabase.getInstance(application).scoreDatabaseDao
        val chart = binding.sessionHistoryChart
        viewModelFactory = SessionHistoryViewModelFactory(dataSource,application,chart)
        viewModel = ViewModelProvider(this,viewModelFactory).get(SessionHistoryViewModel::class.java)
        binding.lifecycleOwner=this

        val spinner = binding.spinner
        viewModel.dates.observe(viewLifecycleOwner, Observer {
            if(it!=null && it.isNotEmpty()){
                val adapter = ArrayAdapter(application,android.R.layout.simple_spinner_item,viewModel.dates.value!!)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter= adapter
                spinner.onItemSelectedListener=this
            }
        })

        return binding.root
    }



    override fun onNothingSelected(parent: AdapterView<*>?) {
        //Nothing
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val date = parent?.getItemAtPosition(position).toString()
        viewModel.getScores(date)
    }

}