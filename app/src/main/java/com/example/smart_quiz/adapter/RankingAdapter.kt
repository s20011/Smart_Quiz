package com.example.smart_quiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.R
import com.example.smart_quiz.model.Rank

class RankingAdapter(private val RankingList: MutableList<Rank>)
    :RecyclerView.Adapter<RankingAdapter.ViewHolder>(){

        inner class ViewHolder(ListItemView: View): RecyclerView.ViewHolder(ListItemView){
            val userName = ListItemView.findViewById<TextView>(R.id.tx_rank_user_name)
            val point = ListItemView.findViewById<TextView>(R.id.tx_point)
            val rank = ListItemView.findViewById<TextView>(R.id.tx_rank)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ranking_row, parent, false)

        val holder = ViewHolder(view)

        view.setOnClickListener {
            itemClickListener?.onItemClick(holder)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rank.text = "${position + 1}"
        holder.point.text = "${RankingList[position].point}"
        holder.userName.text = "${RankingList[position].name}"

    }

    override fun getItemCount() = RankingList.size

    var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder)
    }
}

