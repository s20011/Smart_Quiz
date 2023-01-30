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
import com.example.smart_quiz.model.Score
import com.example.smart_quiz.model.makeRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
    private lateinit var auth: FirebaseAuth



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

        val toolbar = binding.resToolbar
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.result)

        field_id = intent.getStringExtra("fieldId").toString()
        d_id = intent.getStringExtra("d_Id").toString()
        Log.d("ResActivity", "field_id = $field_id")
        Log.d("ResActivity", "d_id = $d_id")
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser




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

        recordScore()
        updateRank(count)
        createRankList(9)

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


        binding.btUpdateRank.setOnClickListener {
            Log.d("ResActivity", "reset rank")
            rankList.clear()
            createRankList(9)
        }

        binding.txTotalpoint.text = count.toString()



    }

    private fun createRankList(limit: Int){
        Log.d("ResActivity", "start createRankList")
        val refRank = Firebase.database.getReference("Details")
        val refUser = Firebase.database.getReference("users")

        refRank.child(field_id).child(d_id).child("ranking")
            .orderByChild("point").startAt(1.0).limitToLast(10)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dbsnapshot: DataSnapshot) {
                    Log.d("ResActivity", "Start createRankList onDataChange")
                    Log.d("ResActivity", "${dbsnapshot.children}")
                    Log.d("ResActivity", "snapshot item count => ${dbsnapshot.childrenCount.toInt()}")
                    val counter = dbsnapshot.childrenCount.toInt()

                    for(data in dbsnapshot.children){
                        val userId = data.child("u_id").getValue(String::class.java)
                        Log.d("ResActivity", "userId = ${userId.toString()}")
                        val point = data.child("point").getValue(Int::class.java)
                        refUser.child(userId.toString())
                            .addListenerForSingleValueEvent(object: ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    Log.d("ResActivity", "Start createRankList onDataChange2")
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
                                    if(rankList.size == counter){
                                        rankList.sortWith(
                                            compareBy<Rank> { it.point }
                                                .thenBy { it.name }
                                        )
                                        rankList.reverse()
                                        binding.rvRanking.adapter?.notifyDataSetChanged()
                                        Log.d("ResActivity DataChange", "$rankList")
                                    }
                                    Log.d("ResActivity", "Rank => $rankList")
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

    //Scoreを記録
    private fun recordScore() {
        Log.d("ResActivity", "Start recordScore")
        val database = FirebaseDatabase.getInstance()
        val refUser = database.getReference("users")
        val uid = auth.currentUser!!.uid.toString()

        val formatter = DateTimeFormatter.ofPattern("YYYYMMdd")
        val date = ZonedDateTime.now()
        val formatDate = date.format(formatter).toLong()
        Log.d("ResActivity", "now Date $formatDate")


        refUser.child(uid).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("ResActivity", "Start recordScore onDataChange")
                val scoreId = snapshot.child("s_id").getValue(String::class.java)
                val refScore = database.getReference("Score/$scoreId")
                refScore.push().setValue(
                    Score(d_id = d_id, point = count, date = formatDate)
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ResActivity", "Not read RealtimeDatabase")
            }
        })

        refUser.child(uid).child("record").orderByChild("d_id").startAt(d_id).endAt(d_id)
            .addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var record: makeRecord? = null
                Log.d("ResActivity", "record読み込み")
                snapshot.children.forEach {record = it!!.getValue(makeRecord::class.java)}
                if(record?.d_id == null){
                    Log.d("ResActivity", "record = $record")
                    pushRecord(d_id, field_id)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

    //Rankを更新する
    private fun updateRank(point: Int): Boolean{
        Log.d("ResActivity", "Start updateRank")
        val uid = auth.currentUser!!.uid
        val database = FirebaseDatabase.getInstance()
        val refDetail = database.getReference("Details")


        refDetail.child(field_id).child(d_id).child("ranking")
            .orderByChild("u_id").startAt(uid).endAt(uid)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot){
                    Log.d("ResActivity", "Start updateRank OnDataChange")
                    var rankInfo: RankInfo? = null
                    snapshot.children.forEach { rankInfo = it!!.getValue(RankInfo::class.java) }
                    Log.d("ResActivity", "rankPoint => $rankInfo")
                    if(rankInfo?.u_id == null){
                        Log.d("ResActivity", "Push RankInfo")
                        pushRank(point, uid)
                        return

                    }else if(point > rankInfo!!.point){
                        val updatePoint = hashMapOf<String, Any>()
                        var key: String? = null
                        snapshot.children.forEach { key = it.key.toString() }
                        updatePoint["$key/point"] = point
                        Log.d("ResActivity", "Update RankInfo")
                        refDetail.child(field_id).child(d_id)
                          .child("ranking").updateChildren(updatePoint)
                    } else {
                        Log.d("ResActivity", "Not Update for Rank")

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("ResActivity", "Not UpdateRank")
                }
            })

        return true
    }

    private fun pushRank(point: Int, uid: String){
        Log.d("ResActivity", "Start pushRank")
        val database = FirebaseDatabase.getInstance()
        val refRank = database.getReference("Details/$field_id/$d_id/")
        val newPost = refRank.child("ranking").push()
        newPost.setValue(
            RankInfo(point = point, u_id = uid)
        )
    }

    private fun pushRecord(dId: String, field: String){
        val database = FirebaseDatabase.getInstance()
        val uid = auth.currentUser!!.uid
        val refUser = database.getReference("users/$uid")

        refUser.child("record").push().setValue(
            makeRecord(d_id = dId, field = field)
        )
    }




}