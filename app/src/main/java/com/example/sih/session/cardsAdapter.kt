package com.example.sih.session

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sih.databinding.CardStudentListBinding

class CardsAdapter() : ListAdapter<String, RecyclerView.ViewHolder>(DiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val student = getItem(position)
        when(holder){
            is ViewHolder ->{
                holder.bind(student)
            }
        }
    }

    class ViewHolder private constructor(val binding: CardStudentListBinding) :
            RecyclerView.ViewHolder(binding.root){

        fun bind(student : String){
                binding.student = student
                binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardStudentListBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

}

class DiffCallBack() : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem==newItem
    }
}