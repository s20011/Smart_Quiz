package com.example.smart_quiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.R
import com.example.smart_quiz.model.RecordQuiz

class RecordAdapter(private val Record: MutableList<RecordQuiz>)
    : RecyclerView.Adapter<RecordAdapter.ViewHolder>(){

        inner class ViewHolder(ListItemView: View): RecyclerView.ViewHolder(ListItemView){
            val title = ListItemView.findViewById<TextView>(R.id.tx_record_title)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.record_row, parent, false)

        val _holder = ViewHolder(view)
        view.setOnClickListener {
            itemClickListener?.onItemClick(_holder)
        }

        return _holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = Record[position].title
        holder.title.text = title
    }

    override fun getItemCount(): Int = Record.size

    var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder)
    }
}