package com.example.sih.session

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sih.R
import com.example.sih.database.ScoreDatabase
import com.example.sih.databinding.FragmentSessionPlotBinding
import com.github.mikephil.charting.charts.BarChart


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

