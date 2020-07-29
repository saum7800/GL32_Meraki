package com.example.sih.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.sih.R
import com.example.sih.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment(){

    private lateinit var binding : FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_history,container,false
        )
        binding.lifecycleOwner=this

    val anyChartView = binding.historyChart
        val chart = AnyChart.bar()

        val data : MutableList<DataEntry> = mutableListOf()
        data.apply {
            add(ValueDataEntry("P1",100))
            add(ValueDataEntry("P2",200))
            add(ValueDataEntry("P3",15))
            add(ValueDataEntry("P4",130))
            add(ValueDataEntry("P5",153))
            add(ValueDataEntry("P6",120))
            add(ValueDataEntry("P7",151))
            add(ValueDataEntry("P8",58))
            add(ValueDataEntry("P9",19))
            add(ValueDataEntry("P10",135))
            add(ValueDataEntry("P11",170))
            add(ValueDataEntry("P12",195))
            add(ValueDataEntry("P13",22))
            add(ValueDataEntry("P14",175))
            add(ValueDataEntry("P15",120))
        }

        chart.data(data)
        anyChartView.setChart(chart)

        return binding.root

    }
}