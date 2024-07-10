package com.toy.project.emodiary.model.data

object UserData {
    var nickname: String? = null
    var email: String? = null

    fun setUserData(email: String, nickname: String) {
        this.email = email
        this.nickname = nickname
    }

    fun clearUserData() {
        this.email = null
        this.nickname = null
    }
}