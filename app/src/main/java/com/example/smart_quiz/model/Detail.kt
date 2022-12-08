package com.example.smart_quiz.model

import org.jetbrains.annotations.NotNull

data class Detail(
    @NotNull
    val likeNum: Int = 0,
    val q_id: String = "",
    val title: String = ""
    )
