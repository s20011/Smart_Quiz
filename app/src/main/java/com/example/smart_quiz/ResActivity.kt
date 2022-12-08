package com.example.smart_quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.adapter.RankingAdapter
import com.example.smart_quiz.adapter.ResAdapter
import com.example.smart_quiz.databinding.ActivityResBinding
import com.example.smart_quiz.model.Rank
import com.example.smart_quiz.model.RankInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ResActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResBinding
    private var count = 0
    private val strRes = mutableListOf<String>()
    private lateinit var resRecyclerView: RecyclerView
    private val rankList = mutableListOf<Rank>()
    private lateinit var rankRecyclerView: RecyclerView
    private val rankInfoList = mutableListOf<RankInfo>()
    private lateinit var field_id: String
    private lateinit var d_id: String


    private val sampleList: MutableList<Rank> = mutableListOf(
        Rank(name = "test-user" , point = 100),
        Rank(name = "test-user2" , point = 90),
        Rank(name = "test-user3" , point = 80),
        Rank(name = "test-user4" , point = 70),
        Rank(name = "test-user5" , point = 60),
        Rank(name = "test-user6" , point = 50),
        Rank(name = "test-user7" , point = 40),
        Rank(name = "test-user8" , point = 30),
        Rank(name = "test-user9" , point = 20),
        Rank(name = "test-user10" , point = 10),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResBinding.inflate(layoutInflater)
        setContentView(binding.root)

        field_id = intent.getStringExtra("fieldId").toString()
        d_id = intent.getStringExtra("d_Id").toString()
        Log.d("ResActivity", "field_id = $field_id")
        Log.d("ResActivity", "d_id = $d_id")



        createRankList(9)
        val list = mutableListOf<Int>()
        for(i in 1..10){
            list.add(i)
            if(list.size == 10){
                list.reverse()
                Log.d("ResActivity", "$list")
            }
        }

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

        val layoutManager = LinearLayoutManager(this@ResActivity)

        //問題の結果を表示
        resRecyclerView = binding.resRecyclerview
        val resAdapter = ResAdapter(strRes)
        resRecyclerView.let {
            it.adapter = resAdapter
            it.layoutManager = layoutManager
            it.itemAnimator?.changeDuration = 0
        }
        Log.d("ResActivity", "total => $count")

        //Rankingを表示
        rankRecyclerView = binding.rvRanking
        val rankAdapter = RankingAdapter(rankList)
        rankRecyclerView.let {
            it.adapter = rankAdapter
            it.layoutManager = LinearLayoutManager(this@ResActivity)
            it.itemAnimator?.changeDuration = 0
        }



        binding.txTotalpoint.text = count.toString()



    }

    private fun createRankList(limit: Int){
        val refRank = Firebase.database.getReference("Details")
        val refUser = Firebase.database.getReference("users")

        refRank.child(field_id).child(d_id).child("ranking")
            .orderByChild("point").startAt(1.0).limitToLast(limit)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("ResActivity", "Start onDataChange")
                    Log.d("ResActivity", "${snapshot.children}")

                    for(data in snapshot.children){
                        val userId = data.child("u_id").getValue(String::class.java)
                        Log.d("ResActivity", "userId = ${userId.toString()}")
                        val point = data.child("point").getValue(Int::class.java)
                        refUser.child(userId.toString())
                            .addListenerForSingleValueEvent(object: ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val userName = snapshot.child("name")
                                        .getValue(String::class.java)
                                    Log.d("ResActivity", "userName = ${userName.toString()}")
                                    rankList.add(
                                        Rank(
                                            name = userName.toString(),
                                            point = point!!.toInt()
                                        )
                                    )

                                    //binding.rvRanking.adapter?.notifyItemInserted(rankList.size -1)
                                    if(rankList.size == limit){
                                        rankList.reverse()
                                        Log.d("ResActivity DataChange", "$rankList")
                                        binding.rvRanking.adapter?.notifyDataSetChanged()
                                    }
                                    Log.d("ResActivity", "$rankList")
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.d("ResActivity", "ERROR")
                                }
                            })

                    }
                    Log.d("ResActivity", "Finish onDataChange")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("ResActivity", "ERROR")
                }
            })
    }




}