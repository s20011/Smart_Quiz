package com.example.smart_quiz

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class SimpleMarkerView(context: Context, layoutResource: Int)
    : MarkerView(context, layoutResource) {

    private lateinit var textView: TextView
    override fun refreshContent(e: Entry?, highlight: Highlight){
        textView = findViewById(R.id.tvSimple)
        textView.text = "日付：${e?.x} \$ point：${e?.y}"
        super.refreshContent(e,highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat() - 20f)
    }


}