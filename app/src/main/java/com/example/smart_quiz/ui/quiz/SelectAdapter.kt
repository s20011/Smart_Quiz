package com.example.smart_quiz.ui.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.R
import com.example.smart_quiz.model.Detail

class SelectAdapter(private val Details: MutableList<Detail>)
    :RecyclerView.Adapter<SelectAdapter.ViewHolder>(){

        inner class ViewHolder(ListItemView: View): RecyclerView.ViewHolder(ListItemView){
            val title = ListItemView.findViewById<TextView>(R.id.tx_select_title)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_row, parent, false)

        return ViewHolder(view)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = Details[position].title
    }

    override fun getItemCount() = Details.size
}