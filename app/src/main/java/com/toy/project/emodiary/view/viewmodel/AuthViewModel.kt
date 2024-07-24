package com.toy.project.emodiary.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.toy.project.emodiary.model.dto.MessageDto
import com.toy.project.emodiary.model.dto.SignInDto
import com.toy.project.emodiary.model.dto.SignInSuccessDto
import com.toy.project.emodiary.model.dto.SignUpDto
import com.toy.project.emodiary.model.dto.UserInfoDto
import com.toy.project.emodiary.model.repository.AuthRepository
import com.toy.project.emodiary.model.utils.Event
import com.toy.project.emodiary.model.utils.NetworkUtil.handleResult
import com.toy.project.emodiary.model.utils.ResultUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    private val _signIn = MutableLiveData<SignInSuccessDto>()
    val signIn: LiveData<SignInSuccessDto> = _signIn

    private val _signUp = MutableLiveData<ResultUtil<MessageDto>>()
    val signUp: LiveData<ResultUtil<MessageDto>> = _signUp

    private val _signOut = MutableLiveData<MessageDto>()
    val signOut: LiveData<MessageDto> = _signOut

    private val _userInfo = MutableLiveData<UserInfoDto>()
    val userInfo: LiveData<UserInfoDto> = _userInfo

    fun signIn(dto: SignInDto) {
        repository.signIn(dto) { result ->
            handleResult(result, _signIn, _errorMessage)
        }
    }

    fun signUp(dto: SignUpDto) {
        repository.signUp(dto) { result ->
            _signUp.postValue(result)
        }
    }

    fun signOut() {
        repository.signOut { result ->
            handleResult(result, _signOut, _errorMessage)
        }
    }

    fun getUserInfo() {
        repository.getUserInfo { result ->
            handleResult(result, _userInfo, _errorMessage)
        }
    }
}