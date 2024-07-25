package com.toy.project.emodiary.model.dto

data class MyInfoDto(
    val nickname: String,
    val firstDiaryDate: String?,
    val percentage: String,
    val emotions: List<Emotions>
)

data class Emotions(
    val day: String,
    val emotion: Int
)