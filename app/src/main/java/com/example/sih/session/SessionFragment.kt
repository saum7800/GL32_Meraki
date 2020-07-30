package com.example.sih.session

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.example.sih.R
import com.example.sih.database.ScoreDatabase
import com.example.sih.databinding.FragmentSessionPlotBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.delay


class SessionFragment : Fragment() {

    private lateinit var binding: FragmentSessionPlotBinding
    private lateinit var viewModel: SessionViewModel
    private lateinit var viewModelFactory: SessionViewModelFactory
    private lateinit var chart : BarChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_session_plot, container, false
        )
        chart = binding.sessionChart
        val application = requireNotNull(this.activity).application
        val dataSource = ScoreDatabase.getInstance(application).scoreDatabaseDao
        viewModelFactory = SessionViewModelFactory(dataSource, application,chart)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SessionViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.readFireBase()
        return binding.root
    }

}

