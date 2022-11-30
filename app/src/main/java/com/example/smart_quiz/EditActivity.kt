package com.example.smart_quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smart_quiz.databinding.ActivityEditBinding
import com.example.smart_quiz.model.Quiz

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private val createQuizList: MutableList<Quiz> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}