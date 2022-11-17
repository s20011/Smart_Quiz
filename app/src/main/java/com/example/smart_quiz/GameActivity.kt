package com.example.smart_quiz

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.adapter.ChoiceAdapter
import com.example.smart_quiz.databinding.ActivityGameBinding
import com.example.smart_quiz.model.Quiz

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    var quizCount = 0 //何問目かの情報
    var correct = mutableListOf<String>() //クイズの正解
    val userSelect = mutableListOf<String>() //userが選択した選択肢
    val choices = mutableListOf<String>()
    val result = mutableListOf<String>() // 問題の結果
    private lateinit var recyclerView: RecyclerView

    //sample
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
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameStart()

        binding.btNext.setOnClickListener {
            quizCount++
            gameStart()
        }

        //if(quizCount + 1 == sampleQuizList.size) binding.btNext.isClickable = false


    }

    //問題ごとにviewを更新する
    fun gameStart() {
        correct.clear()
        userSelect.clear()
        choices.clear()

        if(quizCount + 1 == sampleQuizList.size) finish()
        binding.txCount.text = "${quizCount + 1}"
        binding.txSentence.text = sampleQuizList[quizCount].sentence
        correct.add(sampleQuizList[quizCount].correct)

        choices.let {
            it.add(sampleQuizList[quizCount].choice1)
            it.add(sampleQuizList[quizCount].choice2)
            it.add(sampleQuizList[quizCount].choice3)
            it.add(sampleQuizList[quizCount].correct)
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






    }

    fun Alert() {

    }
}