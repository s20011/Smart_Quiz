package com.example.smart_quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.adapter.ResAdapter
import com.example.smart_quiz.databinding.ActivityResBinding

class ResActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResBinding
    private var count = 0
    private val strRes = mutableListOf<String>()
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getIntegerArrayListExtra("Result")
        for(i in result!!){
            if(i != 0){
                strRes.add("◎")
                Log.d("ResActivity", "Res => ◎")
                Log.d("ResActivity", "count => ${count + i}")
                count += i
            }else {
                strRes.add("✕")
                Log.d("ResActivity", "Res => ✕")
            }
        }

        binding.btFinish.setOnClickListener {
            finish()
        }

        recyclerView = binding.resRecyclerview
        val adapter = ResAdapter(strRes)

        recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this@ResActivity)
            it.itemAnimator?.changeDuration = 0
        }
        Log.d("ResActivity", "total => $count")

        binding.txTotalpoint.text = count.toString()

    }
}