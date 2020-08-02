package com.example.sih.session

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sih.R
import com.example.sih.database.ScoreDatabase
import com.example.sih.databinding.FragmentSessionPlotBinding
import com.github.mikephil.charting.charts.BarChart


class SessionFragment : Fragment() {

    private lateinit var binding: FragmentSessionPlotBinding
    private lateinit var viewModel: SessionViewModel
    private lateinit var viewModelFactory: SessionViewModelFactory
    private lateinit var drowsyAdapter : CardsAdapter
    private lateinit var inattentiveAdapter: CardsAdapter
    private lateinit var attentiveAdapter: CardsAdapter
    private lateinit var interactiveAdapter: CardsAdapter

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
        viewModelFactory = SessionViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SessionViewModel::class.java)
        binding.lifecycleOwner = this

        viewModel.readFireBase()

        drowsyAdapter = CardsAdapter()
        binding.drowsyList.adapter=drowsyAdapter
        inattentiveAdapter = CardsAdapter()
        binding.inattentiveList.adapter=inattentiveAdapter
        attentiveAdapter = CardsAdapter()
        binding.attentiveList.adapter=attentiveAdapter
        interactiveAdapter = CardsAdapter()
        binding.interactiveList.adapter=interactiveAdapter


        viewModel.drowsy.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Session-Drowsy", it.toString())
                drowsyAdapter.submitList(it)
            }
        })

        /**viewModel.inattentive.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.d("Session-Inattentive", it.toString())
                inattentiveAdapter.submitList(it)
            }
        })

        viewModel.attentive.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Session-Attentive", it.toString())
                attentiveAdapter.submitList(it)
            }
        })
        viewModel.interactive.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.d("Session-Interactive", it.toString())
                interactiveAdapter.submitList(it)
            }
        })**/
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveHistory()
    }
}

