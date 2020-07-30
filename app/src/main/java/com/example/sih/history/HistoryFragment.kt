package com.example.sih.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sih.R
import com.example.sih.database.ScoreDatabase
import com.example.sih.databinding.FragmentHistoryBinding
import com.github.mikephil.charting.charts.LineChart
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoryFragment : Fragment(){

    private lateinit var binding : FragmentHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var viewModelFactory : HistoryViewModelFactory
    private lateinit var chart : LineChart

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
        val chart = binding.historyChart
        viewModelFactory = HistoryViewModelFactory(dataSource,application,chart)
        viewModel = ViewModelProvider(this,viewModelFactory).get(HistoryViewModel::class.java)
        binding.lifecycleOwner=this

        //val date =   SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(this)
        viewModel.getScores(getDate())
        return binding.root

    }

    private fun getDate() : String{
        val curr = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return curr.format(formatter)
    }

}