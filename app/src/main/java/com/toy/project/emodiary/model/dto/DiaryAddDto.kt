package com.toy.project.emodiary.model.dto

data class DiaryAddDto(
    val createdDate: String,
    val title: String,
    val content: String,
    val lat: Double?,
    val lon: Double?
)
