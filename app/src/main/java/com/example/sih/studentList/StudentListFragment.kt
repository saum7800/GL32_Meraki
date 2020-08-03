package com.example.sih.studentList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.sih.R
import com.example.sih.database.StudentDatabase
import com.example.sih.databinding.FragmentStudentHistoryBinding

class StudentList : Fragment(){

    private lateinit var binding : FragmentStudentHistoryBinding
    private lateinit var viewModel: StudentListViewModel
    private lateinit var viewModelFactory : StudentListViewModelFactory
    private lateinit var adapter : StudentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_student_history,container,false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = StudentDatabase.getInstance(application).studentDatabaseDao
        viewModelFactory = StudentListViewModelFactory(dataSource,application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(StudentListViewModel::class.java)
        binding.lifecycleOwner=this

        adapter = StudentListAdapter(Listener{ student ->
            viewModel.onStudentClicked(student)
        })
        binding.studentHistList.adapter=adapter
        viewModel.students.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.student.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                this.findNavController().navigate(
                    StudentListDirections.actionStudentListToStudentHistoryFragment(it)
                )
            }
        })
        return binding.root
    }

}