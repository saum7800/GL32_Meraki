package com.example.sih.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.example.sih.R
import com.example.sih.database.ScoreDatabase
import com.example.sih.databinding.FragmentHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment(){

    private lateinit var binding : FragmentHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var viewModelFactory : HistoryViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_history,container,false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = ScoreDatabase.getInstance(application).scoreDatabaseDao
        viewModelFactory = HistoryViewModelFactory(dataSource,application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(HistoryViewModel::class.java)
        binding.lifecycleOwner=this
        val anyChartView = binding.historyChart

        val date =   SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(this)

        viewModel.getScores(date)

        viewModel.notesReceived.observe(viewLifecycleOwner, Observer {
            it->if(it==true){
            plot(viewModel.scores,anyChartView)
            viewModel.graphPlotted()
        }
        })

        return binding.root

    }

    private fun plot(data : MutableList<DataEntry>, chartView : AnyChartView){

        val chart = AnyChart.bar()
        chart.data(data)
        chartView.setChart(chart)

    }
}