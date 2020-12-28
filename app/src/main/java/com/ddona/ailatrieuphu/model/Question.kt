package com.ddona.ailatrieuphu.model

data class Question(
    val id: Int,
    var level: Int,
    val question: String,
    val caseA: String,
    val caseB: String,
    val caseC: String,
    val caseD: String,
    val truecase: Int
)