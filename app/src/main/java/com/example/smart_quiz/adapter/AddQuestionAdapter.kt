package com.example.smart_quiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.R
import com.example.smart_quiz.model.Quiz

class AddQuestionAdapter(private val quizList: MutableList<Quiz>)
    :RecyclerView.Adapter<AddQuestionAdapter.ViewHolder>(){

        inner class ViewHolder(LIstItemView: View): RecyclerView.ViewHolder(LIstItemView){
            val title = LIstItemView.findViewById<TextView>(R.id.tx_add_sentence)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_row, parent, false)

        val _holder = ViewHolder(view)
        view.setOnClickListener{
            itemClickListener?.onItemClick(_holder)
        }

        return _holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = quizList[position].sentence
        holder.title.text = title
    }

    override fun getItemCount(): Int = quizList.size

    var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder)
    }
}