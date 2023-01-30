package com.example.smart_quiz.model

import java.text.ChoiceFormat

data class Quiz(
    var choice1: String = "",
    var choice2: String = "",
    var choice3: String = "",
    var correct: String = "",
    var sentence: String = ""
)
