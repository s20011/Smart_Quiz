package com.example.smart_quiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.R

class ResAdapter(private val res: MutableList<String>)
    :RecyclerView.Adapter<ResAdapter.ViewHolder>(){

        inner class ViewHolder(ListItemView: View):RecyclerView.ViewHolder(ListItemView){
            val strRes = ListItemView.findViewById<TextView>(R.id.tx_res)
            val number = ListItemView.findViewById<TextView>(R.id.tx_counter)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.res_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val str = res[position]
        holder.strRes.text = str
        holder.number.text = "${position + 1}"
    }

    override fun getItemCount() = res.size
}