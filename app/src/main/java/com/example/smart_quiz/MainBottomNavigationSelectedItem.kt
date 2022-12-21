package com.example.smart_quiz

enum class MainBottomNavigationSelectedItem {
    HOME,
    QUIZ,
    EDIT;

    fun isHome(): Boolean = (this == HOME)
    fun isQuiz(): Boolean = (this == QUIZ)
    fun isEdit(): Boolean = (this == EDIT)
}