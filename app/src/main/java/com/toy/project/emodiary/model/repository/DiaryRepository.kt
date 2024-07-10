package com.toy.project.emodiary.model.repository

import com.toy.project.emodiary.model.api.DiaryApi
import com.toy.project.emodiary.model.dto.DiaryListDto
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Named

class DiaryRepository @Inject constructor(@Named("auth") retrofitWithAuth: Retrofit) {
    private val diaryApi: DiaryApi = retrofitWithAuth.create(DiaryApi::class.java)

    suspend fun getDiaryList(year: Int, month: Int): DiaryListDto? {
        return diaryApi.getDiaryList(year, month).awaitResponse().body()
    }
}