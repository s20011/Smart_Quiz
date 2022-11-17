package com.example.smart_quiz.model

import java.text.ChoiceFormat

data class Quiz(
    val choice1: String = "",
    val choice2: String = "",
    val choice3: String = "",
    val correct: String = "",
    val sentence: String = ""
)
