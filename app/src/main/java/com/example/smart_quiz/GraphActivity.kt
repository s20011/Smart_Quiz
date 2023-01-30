package com.example.smart_quiz

import android.graphics.Color
import android.graphics.DashPathEffect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smart_quiz.databinding.ActivityGraphBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class GraphActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGraphBinding
    private lateinit var mChart: LineChart
    private var maxPoint: Float = 0f
    private lateinit var dId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)


        dId = intent.getStringExtra("dId").toString()

        val toolbar = binding.graphToolbar
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.graph)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        maxPoint = 160f
        mChart = binding.chart

        val quarters = mutableListOf<String>(
            "5/3", "5/6", "5/7", "6/12", "6/15", "6/30", "7/2", "7/8", "7/19", "7/20", "7/26",
            "7/30", "7/31", "8/4", "8/9", "8/13", "8/15", "8/17", "8/20"
        )

        val formatter = object: ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return quarters[value.toInt()]
            }
        }

        //Grid背景色
        mChart.setDrawGridBackground(true)
        val xAxis = mChart.xAxis
        xAxis.isEnabled = false

        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(quarters)
        xAxis.setLabelCount(quarters.size, false)

        val marker: SimpleMarkerView = SimpleMarkerView(
            this@GraphActivity,
            R.layout.simple_marker_view,
            quarters
        )

        marker.chartView = mChart
        mChart.marker = marker



        //Grid縦軸を破線
        //xAxis.enableGridDashedLine(10f, 10f, 0f)
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val leftAxis = mChart.axisLeft
        //Y軸最大値・最小値設定
        leftAxis.axisMaximum = maxPoint
        leftAxis.axisMinimum = 0f
        //Grid横軸を破線
        //leftAxis.enableGridDashedLine(10f, 10f, 0f)
        leftAxis.setDrawZeroLine(true)

        //右側の目盛り
        mChart.axisRight.isEnabled = false


        //グラフのデータリスト
        val entries: ArrayList<Entry> = ArrayList()

        val datas = mutableListOf<Int>(
            116, 111, 112, 121, 102, 83, 99, 101, 74, 105, 120, 112, 109,
            102, 107, 93, 82, 99, 110
        )

        for(i in 0..datas.size - 1){
            entries.add(Entry(i.toFloat(), datas[i].toFloat()))
        }
//        entries.let {
//            it.add(Entry(4f, 0f))
//            it.add(Entry(8f, 1f))
//            it.add(Entry(6f, 2f))
//            it.add(Entry(2f, 3f))
//            it.add(Entry(18f, 4f))
//            it.add(Entry(9f, 5f))
//            it.add(Entry(16f, 6f))
//            it.add(Entry(5f, 7f))
//            it.add(Entry(3f, 8f))
//            it.add(Entry(7f, 10f))
//            it.add(Entry(9f, 11f))
//        }

        val dataset = LineDataSet(entries, "# of Calls")

        dataset.let {
            it.setColor(Color.BLACK)
            it.setDrawIcons(false)
            it.setCircleColor(Color.BLACK)
            it.lineWidth = 1f
            it.circleRadius = 3f
            it.setDrawCircleHole(false)
            it.valueTextSize = 0f
            it.setDrawFilled(true)
            it.formLineWidth = 1f
//            it.formLineDashEffect = DashPathEffect()
            it.formSize = 15f
            it.fillColor = Color.BLUE
        }
        val data = LineData(dataset)

        mChart.data = data
        mChart.animateY(2500)

        

        mChart.invalidate()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}