package com.example.sih.studentHistory

import com.example.sih.databinding.StudentHistoryItemBinding
import com.example.sih.session.Student

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sih.databinding.CardStudentListBinding

class StudentHistAdapter : ListAdapter<StudentHist, RecyclerView.ViewHolder>(DiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
            is ViewHolder ->{
                val student = getItem(position)
                holder.bind(student)
            }
        }
    }

    class ViewHolder private constructor(val binding: StudentHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(student : StudentHist){
            binding.student = student
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StudentHistoryItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

}

class DiffCallBack() : DiffUtil.ItemCallback<StudentHist>(){

    override fun areItemsTheSame(oldItem: StudentHist, newItem: StudentHist): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: StudentHist, newItem: StudentHist): Boolean {
        return oldItem == newItem
    }
}