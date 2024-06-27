package com.toy.project.emodiary.model.dto

data class SignInSuccessDto(
    val user: UserInfoDto,
    val token: TokenDto
)