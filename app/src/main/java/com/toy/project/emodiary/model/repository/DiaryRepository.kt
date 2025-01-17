package com.toy.project.emodiary.model.repository

import com.toy.project.emodiary.model.api.DiaryApi
import com.toy.project.emodiary.model.dto.DiaryAddDto
import com.toy.project.emodiary.model.dto.DiaryEditDto
import com.toy.project.emodiary.model.dto.DiaryListDto
import com.toy.project.emodiary.model.dto.MessageDto
import com.toy.project.emodiary.model.dto.MyInfoDto
import com.toy.project.emodiary.model.utils.NetworkUtil.handleResponse
import com.toy.project.emodiary.model.utils.ResultUtil
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Named

class DiaryRepository @Inject constructor(@Named("auth") retrofitWithAuth: Retrofit) {
    private val diaryApi: DiaryApi = retrofitWithAuth.create(DiaryApi::class.java)

    suspend fun getDiaryList(year: Int, month: Int): DiaryListDto? {
        return diaryApi.getDiaryList(year, month).awaitResponse().body()
    }

    fun addDiary(dto: DiaryAddDto, callback: (ResultUtil<MessageDto>) -> Unit) {
        handleResponse(diaryApi.addDiary(dto), callback)
    }

    fun editDiary(diaryId: Int, dto: DiaryEditDto, callback: (ResultUtil<MessageDto>) -> Unit) {
        handleResponse(diaryApi.editDiary(diaryId, dto), callback)
    }

    fun deleteDiary(diaryId: Int, callback: (ResultUtil<MessageDto>) -> Unit) {
        handleResponse(diaryApi.deleteDiary(diaryId), callback)
    }

    suspend fun getMyInformation(): MyInfoDto? {
        return diaryApi.getMyInformation().awaitResponse().body()
    }
}