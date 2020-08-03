package com.example.sih.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.sih.R
import com.example.sih.databinding.FragmentDashboardBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class DashboardFragment : Fragment(){

    private lateinit var binding : FragmentDashboardBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        calendar = Calendar.getInstance()

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dashboard,container,false
        )
        binding.lifecycleOwner=this

        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val dateString = dayOfMonth.toString().padStart(2,'0')+'-'+(month+1).toString().padStart(2,'0')+"-"+year.toString()
            Log.d("Date",dateString)
            this.findNavController().navigate(
                DashboardFragmentDirections.actionDashboardFragmentToHistoryFragment(dateString.trim())
            )
        }

        binding.sessionButton.setOnClickListener {
            this.findNavController().navigate(
                DashboardFragmentDirections.actionDashboardFragmentToSessionFragment()
            )
        }

        binding.studentHistoryButton.setOnClickListener {
            this.findNavController().navigate(
                DashboardFragmentDirections.actionDashboardFragmentToStudentList()
            )
        }
        return binding.root

    }
}