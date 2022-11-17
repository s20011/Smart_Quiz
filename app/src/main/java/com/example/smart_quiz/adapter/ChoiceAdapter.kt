package com.example.smart_quiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.R

class ChoiceAdapter(private val choices: MutableList<String>)
    :RecyclerView.Adapter<ChoiceAdapter.ViewHolder>(){

        inner class ViewHolder(ListItemView: View): RecyclerView.ViewHolder(ListItemView){
            val choice = ListItemView.findViewById<TextView>(R.id.tx_choice)
            val row = ListItemView.findViewById<ConstraintLayout>(R.id.row)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.choice_row, parent, false)

        val holder = ViewHolder(view)
        view.setOnClickListener {
            itemClickListener?.onItemClick(holder)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val choice = choices[position]
        holder.choice.text = choice
    }

    override fun getItemCount() = choices.size

    var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder)
    }
}