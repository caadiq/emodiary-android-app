package com.toy.project.emodiary.model.repository

import com.toy.project.emodiary.model.api.AuthApi
import com.toy.project.emodiary.model.dto.MessageDto
import com.toy.project.emodiary.model.dto.SignInDto
import com.toy.project.emodiary.model.dto.SignInSuccessDto
import com.toy.project.emodiary.model.dto.SignUpDto
import com.toy.project.emodiary.model.utils.NetworkUtil.handleResponse
import com.toy.project.emodiary.model.utils.ResultUtil
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class AuthRepository @Inject constructor(
    retrofit: Retrofit,
    @Named("auth") retrofitWithAuth: Retrofit
) {
    private val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    private val authApiAuth: AuthApi = retrofitWithAuth.create(AuthApi::class.java)

    fun signUp(dto: SignUpDto, callback: (ResultUtil<MessageDto>) -> Unit) {
        handleResponse(authApi.signUp(dto), callback)
    }

    fun signIn(dto: SignInDto, callback: (ResultUtil<SignInSuccessDto>) -> Unit) {
        handleResponse(authApi.signIn(dto), callback)
    }
}