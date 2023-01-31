package com.example.smart_quiz

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.smart_quiz.databinding.ActivityGraphBinding
import com.example.smart_quiz.model.Score
import com.github.mikephil.charting.charts.LineChart
//import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
//import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GraphActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGraphBinding
    private lateinit var mChart: LineChart
    private var maxPoint: Float = 0f
    private lateinit var dId: String
    private lateinit var auth: FirebaseAuth
    private lateinit var dataset: LineDataSet

    private val dataList = mutableListOf<Int>()
    private val dateList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        dId = intent.getStringExtra("dId").toString()
        getScore()

        val toolbar = binding.graphToolbar
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.graph)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun getScore() {
        val refUser = Firebase.database.getReference("users")
        val refScore = Firebase.database.getReference("Score")
        val uid = auth.currentUser!!.uid

        refUser.child(uid).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val scoreId = snapshot.child("s_id")
                    .getValue(String::class.java).toString()
                refScore.child(scoreId).orderByChild("d_id").startAt(dId).endAt(dId)
                    .addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(data in snapshot.children){
                                val item = data.getValue(Score::class.java)
                                Log.d("GraphActivity", "Score => $item")
                                dataList.add(item!!.point)
                                val date = item.date.toString()
                                val year = date.substring(0, 4)
                                val month = date.substring(4,6)
                                val day = date.substring(6)
                                val format = "$year/$month/$day"
                                Log.d("GraphActivity", "Date = $format")
                                dateList.add(format)
                            }

                            createChart()

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun createChart(){
        //マックスの目盛り
        maxPoint = 160f
        mChart = binding.chart

//        val formatter = object: ValueFormatter() {
//            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
//                return dateList[value.toInt()]
//            }
//        }

        //Grid背景色
        mChart.setDrawGridBackground(true)
        val xAxis = mChart.xAxis
        xAxis.isEnabled = false

        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(dateList)
        xAxis.setLabelCount(dateList.size, false)

        val marker = SimpleMarkerView(
            this@GraphActivity,
            R.layout.simple_marker_view,
            dateList
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

        for(i in 0..dataList.size - 1){
            entries.add(Entry(i.toFloat(), dataList[i].toFloat()))
        }

        dataset = LineDataSet(entries, "# of Calls")

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
}