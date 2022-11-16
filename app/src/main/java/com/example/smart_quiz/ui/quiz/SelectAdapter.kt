package com.example.smart_quiz.ui.quiz

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.GameActivity
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

        val _holder = ViewHolder(view)
        view.setOnClickListener {
            itemClickListener?.onItemClick(_holder)
        }
        return _holder


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = Details[position].title
        holder.title.text = title

        Log.d("Select-Recyclerview", "Finish onBindViewHolder")

    }

    override fun getItemCount() = Details.size

    var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder)
    }
}