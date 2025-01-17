package com.toy.project.emodiary.model.api

import com.toy.project.emodiary.model.dto.DiaryAddDto
import com.toy.project.emodiary.model.dto.DiaryEditDto
import com.toy.project.emodiary.model.dto.DiaryListDto
import com.toy.project.emodiary.model.dto.MessageDto
import com.toy.project.emodiary.model.dto.MyInfoDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface DiaryApi {
    @GET("/diary/diaryMonthList")
    fun getDiaryList(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Call<DiaryListDto>

    @POST("/diary")
    fun addDiary(@Body dto: DiaryAddDto): Call<MessageDto>

    @PUT("/diary/{diaryId}")
    fun editDiary(
        @Path("diaryId") diaryId: Int,
        @Body dto: DiaryEditDto
    ): Call<MessageDto>

    @DELETE("/diary/{diaryId}")
    fun deleteDiary(@Path("diaryId") diaryId: Int): Call<MessageDto>

    @GET("/diary/myinformation")
    fun getMyInformation(): Call<MyInfoDto>
}