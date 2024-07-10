package com.toy.project.emodiary.model.api

import com.toy.project.emodiary.model.dto.DiaryListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DiaryApi {
    @GET("/diary/diaryMonthList")
    fun getDiaryList(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Call<DiaryListDto>
}