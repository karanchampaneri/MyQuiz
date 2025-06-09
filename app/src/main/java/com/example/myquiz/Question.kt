package com.example.myquiz

data class Question(
    val id: Int,
    val text: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctIndex: Int
)

