package com.example.smart_quiz

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.adapter.ChoiceAdapter
import com.example.smart_quiz.databinding.ActivityGameBinding
import com.example.smart_quiz.model.Quiz
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    var quizCount = 0 //何問目かの情報
    var correct = mutableListOf<String>() //クイズの正解
    val userSelect = mutableListOf<String>() //userが選択した選択肢
    val choices = mutableListOf<String>()
    val result = arrayListOf<Int>() // 問題の結果
    private lateinit var recyclerView: RecyclerView
    private lateinit var QuizList: MutableList<Quiz>

    //sample
    /*
    private val sampleQuizList: MutableList<Quiz> = mutableListOf(
        Quiz(
            choice1 = "1kalskfj", choice2 = "1gasda", choice3 = "1asgasji", correct = "1asfaatas",
            sentence = "1lllllldslllllll"
        ),
        Quiz(
            choice1 = "2sdklasdlf", choice2 = "2sdassaf", choice3 = "2asfda", correct = "2sfagas",
            sentence = "2lllllllllasaal"
        ),
        Quiz(
            choice1 = "3sdklasdlf", choice2 = "3sdassaf", choice3 = "3asfda", correct = "3sfagas",
            sentence = "3lllllllllasaal"
        ),
        Quiz(
            choice1 = "4sdklasdlf", choice2 = "4sdassaf", choice3 = "4sfda", correct = "4sfagas",
            sentence = "4lllllllllasaal"
        ),
        Quiz(
            choice1 = "5sdklasdlf", choice2 = "5sdassaf", choice3 = "5asfda", correct = "5sfagas",
            sentence = "5lllllllllasaal"
        )
    )*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra("ID").toString()
        reader(id)
        //gameStart()

        binding.btNext.setOnClickListener {
            quizCount++
            Alert()
        }
    }

    //問題ごとにviewを更新する
    fun gameStart(): Boolean {
        Log.d("GameActivity", "Start Game")
        correct.clear()
        userSelect.clear()
        choices.clear()

        binding.txCount.text = "${quizCount + 1}"
        binding.txSentence.text = QuizList[quizCount].sentence
        correct.add(QuizList[quizCount].correct)

        choices.let {
            it.add(QuizList[quizCount].choice1)
            it.add(QuizList[quizCount].choice2)
            it.add(QuizList[quizCount].choice3)
            it.add(QuizList[quizCount].correct)
            it.shuffle()
        }

        recyclerView = binding.choiceRecyclerview
        val adapter = ChoiceAdapter(choices)

        adapter.itemClickListener = object : ChoiceAdapter.OnItemClickListener {
            override fun onItemClick(holder: ChoiceAdapter.ViewHolder) {
                val choice = holder.choice.text.toString()
                if(choice in userSelect){
                    holder.row.setBackgroundColor(Color.WHITE)
                    userSelect.remove(choice)
                    Log.d("GameActivity", "select = ${println(choice)}")
                }else if(userSelect.size == correct.size){
                    if(choice in userSelect){
                        holder.row.setBackgroundColor(Color.WHITE)
                        userSelect.remove(choice)
                    }else {
                        Log.d("GameActivity", "msg == can not Select")
                    }
                }else {
                    holder.row.setBackgroundColor(Color.RED)
                    userSelect.add(choice)
                }
            }
        }

        recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this@GameActivity)
            it.itemAnimator?.changeDuration = 0
        }


        Log.d("GameActivity", "quizCount == $quizCount")
        return true



    }

    fun Alert() {
        var corNum = 0

        //正解が不正解
        for(i in correct){
            if(i in userSelect){
                corNum++
            }
        }

        var message: String
        if(corNum == userSelect.size){
            message = "正解　◎"
            result.add(3)
        }else {
            message = "不正解　✕"
            result.add(0)
        }

        //Dialogの表示
        if(quizCount  == QuizList.size){
            val builder = AlertDialog.Builder(this@GameActivity)
            builder.setTitle("結果")
                .setMessage(message)
                .setPositiveButton("結果画面") { _, _ ->
                    val intent = Intent(this@GameActivity, ResActivity::class.java)
                    intent.putIntegerArrayListExtra("Result", result)
                    startActivity(intent)
                    finish()
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()


        }else {
            val builder = AlertDialog.Builder(this@GameActivity)
            builder.setTitle("結果")
                .setMessage(message)
                .setPositiveButton("次へ") { _, _ ->
                    gameStart()
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        }
    }

    private fun reader(id: String){
        Log.d("GameActivity", "Start reader")
        val database = FirebaseDatabase.getInstance().reference
        val list: MutableList<Quiz> = mutableListOf()
        database.child("question").child(id)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(data in snapshot.children){
                        val item = data.getValue(Quiz::class.java)
                        list.add(
                            Quiz(choice1 = item!!.choice1,
                            choice2 = item.choice2,
                            choice3 = item.choice3,
                            correct = item.correct,
                            sentence = item.sentence)
                        )
                    }
                    Log.d("GameActivity", "Finish reader")
                    QuizList = list
                    gameStart()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("GameActivity", "ERROR")
                    list.add(
                        Quiz(choice1= "Not Found", choice2 = "Not Found",
                        choice3 = "Not Found", correct = "Not Found",
                        sentence = "Not Found")
                    )
                }
            })
    }



}