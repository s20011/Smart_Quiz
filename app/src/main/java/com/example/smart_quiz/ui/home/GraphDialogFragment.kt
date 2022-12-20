package com.example.smart_quiz.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.smart_quiz.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class GraphDialogFragment: DialogFragment() {

    private lateinit var mChart: LineChart

    companion object {
        @JvmStatic
        fun newInstance(): GraphDialogFragment {
            val dialog = GraphDialogFragment()


            val args = Bundle()
            dialog.arguments = args

            return dialog
        }
    }



    override fun onStart() {
        super.onStart()

//        dialog?.window?.setBackgroundDrawable(null)
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, height)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        val createDialog = inflater?.inflate(R.layout.graph_dialog, null)



        mChart = createDialog!!.findViewById(R.id.line_chart)

        //グラフのデータリスト
        val entries: ArrayList<Entry> = ArrayList()
        entries.let {
            it.add(Entry(4f, 0f))
            it.add(Entry(8f, 1f))
            it.add(Entry(6f, 2f))
            it.add(Entry(2f, 3f))
            it.add(Entry(18f, 4f))
            it.add(Entry(9f, 5f))
            it.add(Entry(16f, 6f))
            it.add(Entry(5f, 7f))
            it.add(Entry(3f, 8f))
            it.add(Entry(7f, 10f))
            it.add(Entry(9f, 11f))
        }

        val dataset = LineDataSet(entries, "# of Calls")
        dataset.setColor(Color.BLACK)
        val data = LineData(dataset)

        mChart.data = data
        mChart.animateY(5000)

        mChart.invalidate()



        val builder = AlertDialog.Builder(activity)
        builder.setView(createDialog)
        val alertDialog = builder.create()

        return alertDialog
    }
}