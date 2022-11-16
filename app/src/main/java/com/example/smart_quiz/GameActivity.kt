package com.example.smart_quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smart_quiz.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}