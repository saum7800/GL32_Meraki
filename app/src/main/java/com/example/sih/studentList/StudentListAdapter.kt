package com.example.sih.studentList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sih.databinding.StudentListItemBinding
import com.example.sih.session.Student

class StudentListAdapter(private val clickListener: Listener) : ListAdapter<Student,
        RecyclerView.ViewHolder>(DiffCallBack())
{

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ViewHolder -> {
                val student=getItem(position)
                holder.bind(clickListener, student)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: StudentListItemBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: Listener, student: Student){
            binding.student = student
            binding.clickListener=clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StudentListItemBinding.inflate(layoutInflater,parent,false)

                return ViewHolder(binding)
            }
        }
    }

}

class Listener(val clickListener: (student : Student) -> Unit){
    fun onClick(student: Student) = clickListener(student)
}

class DiffCallBack() : DiffUtil.ItemCallback<Student>(){
    override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem == newItem
    }
}