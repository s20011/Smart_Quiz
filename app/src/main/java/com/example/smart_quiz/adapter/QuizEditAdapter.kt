package com.example.smart_quiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.R
import com.example.smart_quiz.model.Quiz

class QuizEditAdapter(private val quizList: MutableList<Quiz>)
    :RecyclerView.Adapter<QuizEditAdapter.ViewHolder>(){
        inner class ViewHolder(ListItemView: View): RecyclerView.ViewHolder(ListItemView){
            val titleRow = ListItemView.findViewById<TextView>(R.id.tx_edit_sentence)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.quiz_edit_row, parent, false)

        val holder = ViewHolder(view)
        view.setOnClickListener {
            itemClickListener?.onItemClick(holder)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = quizList[position].sentence
        holder.titleRow.text = title

    }

    override fun getItemCount() = quizList.size

    var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder)
    }
}