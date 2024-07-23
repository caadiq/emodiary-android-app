package com.toy.project.emodiary.model.dto

data class DiaryListDto(
    val years : List<Years>,
    val diary: List<Diary>
)

data class Years(
    val year: Int,
    val count: Int
)

data class Diary(
    val diaryId: Int,
    val title: String,
    val content: String,
    val emotionUrl: String?,
    val weatherUrl: String?,
    val wordCloudUrl: String?,
    val createdDate: String
)