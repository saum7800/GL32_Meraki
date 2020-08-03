package com.example.sih.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SimpleCursorAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sih.R
import com.example.sih.database.ScoreDatabase
import com.example.sih.databinding.FragmentHistoryBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment(), AdapterView.OnItemSelectedListener {

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
        val args = HistoryFragmentArgs.fromBundle(requireArguments())
        val date = args.date
        viewModelFactory = HistoryViewModelFactory(dataSource,application,chart,date)
        viewModel = ViewModelProvider(this,viewModelFactory).get(HistoryViewModel::class.java)
        binding.lifecycleOwner=this

        val spinner = binding.spinner
        viewModel.students.observe(viewLifecycleOwner, Observer {
            if(it!=null && it.isNotEmpty()){
                val adapter = ArrayAdapter(application,android.R.layout.simple_spinner_item,it)
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
            Log.d("History", date)
            viewModel.getScores(date)
    }

}