package com.toy.project.emodiary.model.api

import com.toy.project.emodiary.model.dto.MessageDto
import com.toy.project.emodiary.model.dto.SignInDto
import com.toy.project.emodiary.model.dto.SignInSuccessDto
import com.toy.project.emodiary.model.dto.SignUpDto
import com.toy.project.emodiary.model.dto.TokenDto
import com.toy.project.emodiary.model.dto.UserInfoDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    // 회원가입
    @POST("/auth/signup")
    fun signUp(@Body signUpDto: SignUpDto): Call<MessageDto>

    // 로그인
    @POST("/auth/signin")
    fun signIn(@Body signInDto: SignInDto): Call<SignInSuccessDto>

    @POST("/auth/logout")
    fun signOut(): Call<MessageDto>

    // 로그인 유저 정보
    @GET("/auth/users")
    fun userInfo(): Call<UserInfoDto>

    // Access Token 재발급
    @POST("/auth/token/access")
    fun reissueAccessToken(@Header("AccessToken") accessToken: String, @Header("RefreshToken") refreshToken: String): Call<TokenDto>

    // Refresh Token 재발급
    @POST("/auth/token/refresh")
    fun reissueRefreshToken(@Header("Authorization") accessToken: String): Call<TokenDto>
}
