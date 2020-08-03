package com.example.sih.session

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sih.R
import com.example.sih.database.ScoreDatabase
import com.example.sih.database.StudentDatabase
import com.example.sih.databinding.FragmentSessionPlotBinding
import kotlinx.android.synthetic.main.fragment_session_plot.*


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
        val studentDatabaseDao = StudentDatabase.getInstance(application).studentDatabaseDao
        viewModelFactory = SessionViewModelFactory(dataSource, application,studentDatabaseDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SessionViewModel::class.java)
        binding.lifecycleOwner = this


        viewModel.readFireBase()

        drowsyAdapter = CardsAdapter()
        binding.drowsyList.adapter= drowsyAdapter
        inattentiveAdapter = CardsAdapter()
        binding.inattentiveList.adapter=inattentiveAdapter
        attentiveAdapter = CardsAdapter()
        binding.attentiveList.adapter=attentiveAdapter
        interactiveAdapter = CardsAdapter()
        binding.interactiveList.adapter=interactiveAdapter



        viewModel.drowsy.observe(viewLifecycleOwner, Observer {
            it?.let {
                    drowsyAdapter.submitList(it)
                    Log.d("Session-Drowsy", it.toString())
                    drowsyAdapter.notifyDataSetChanged()
            }
        })

        viewModel.inattentive.observe(viewLifecycleOwner, Observer {
            it?.let{

                Log.d("Session-Inattentive", it.toString())
                inattentiveAdapter.submitList(it)
                inattentiveAdapter.notifyDataSetChanged()

            }
        })

        viewModel.attentive.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Session-Attentive", it.toString())
                attentiveAdapter.submitList(it)
                attentiveAdapter.notifyDataSetChanged()
            }
        })
        viewModel.interactive.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.d("Session-Interactive", it.toString())
                interactiveAdapter.submitList(it)
                interactiveAdapter.notifyDataSetChanged()
            }
        })

        viewModel.animBool.observe(viewLifecycleOwner, Observer {
            if(it==true){
                blink()
                viewModel.animDone()
            }
        })
        return binding.root
    }

    private fun blink(){
        val anim = ObjectAnimator.ofInt(
            drowsyList, "backgroundColor", Color.WHITE, Color.parseColor("#70FF0000"),
            Color.WHITE
        )
        anim.duration = 2000
        anim.setEvaluator(ArgbEvaluator())
        anim.start()
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveHistory()
        viewModel.saveStudentHistory()
    }

}

