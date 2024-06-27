package com.toy.project.emodiary.model.api

import com.toy.project.emodiary.model.dto.MessageDto
import com.toy.project.emodiary.model.dto.SignInDto
import com.toy.project.emodiary.model.dto.SignInSuccessDto
import com.toy.project.emodiary.model.dto.SignUpDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    // 회원가입
    @POST("/auth/signup")
    fun signUp(@Body signUpDto: SignUpDto): Call<MessageDto>

    // 로그인
    @POST("/auth/signin")
    fun signIn(@Body signInDto: SignInDto): Call<SignInSuccessDto>
}
