package com.toy.project.emodiary.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toy.project.emodiary.model.dto.DiaryAddDto
import com.toy.project.emodiary.model.dto.DiaryEditDto
import com.toy.project.emodiary.model.dto.DiaryListDto
import com.toy.project.emodiary.model.dto.MessageDto
import com.toy.project.emodiary.model.dto.MyInfoDto
import com.toy.project.emodiary.model.repository.DiaryRepository
import com.toy.project.emodiary.model.utils.Event
import com.toy.project.emodiary.model.utils.ResultUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(private val repository: DiaryRepository) : ViewModel() {
    private val _diaryList = MutableLiveData<DiaryListDto>()
    val diaryList: LiveData<DiaryListDto> = _diaryList

    private val _currentYear = MutableLiveData<Int>()
    val currentYear: LiveData<Int> = _currentYear

    private val _currentMonth = MutableLiveData<Int>()
    val currentMonth: LiveData<Int> = _currentMonth

    private val _isWritten = MutableLiveData<Boolean>()
    val isWritten: LiveData<Boolean> = _isWritten

    private val _isFirstTime = MutableLiveData<Boolean>()
    val isFirstTime: LiveData<Boolean> = _isFirstTime

    private val _addDiary = MutableLiveData<MessageDto>()
    val addDiary: LiveData<MessageDto> = _addDiary

    private val _editDiary = MutableLiveData<MessageDto>()
    val editDiary: LiveData<MessageDto> = _editDiary

    private val _deleteDiary = MutableLiveData<MessageDto>()
    val deleteDiary: LiveData<MessageDto> = _deleteDiary

    private val _myInfo = MutableLiveData<MyInfoDto>()
    val myInfo: LiveData<MyInfoDto> = _myInfo

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun getDiaryList(year: Int, month: Int) {
        viewModelScope.launch {
            _diaryList.value = repository.getDiaryList(year, month)
        }
    }

    fun setCurrentYear(year: Int) {
        _currentYear.value = year
    }

    fun setCurrentMonth(month: Int) {
        _currentMonth.value = month
    }

    fun setIsWritten(isWritten: Boolean) {
        _isWritten.value = isWritten
    }

    fun setIsFirstTime(isFirstTime: Boolean) {
        _isFirstTime.value = isFirstTime
    }

    fun addDiary(dto: DiaryAddDto) {
        repository.addDiary(dto) {
            handleResult(it, _addDiary, _errorMessage)
        }
    }

    fun editDiary(diaryId: Int, dto: DiaryEditDto) {
        repository.editDiary(diaryId, dto) {
            handleResult(it, _editDiary, _errorMessage)
        }
    }

    fun deleteDiary(diaryId: Int) {
        repository.deleteDiary(diaryId) {
            handleResult(it, _deleteDiary, _errorMessage)
        }
    }

    fun getMyInformation() {
        viewModelScope.launch {
            _myInfo.value = repository.getMyInformation()
        }
    }

    private fun handleResult(result: ResultUtil<MessageDto>, successLiveData: MutableLiveData<MessageDto>, errorLiveData: MutableLiveData<Event<String>>) {
        when (result) {
            is ResultUtil.Success -> { successLiveData.postValue(result.data) }
            is ResultUtil.Error -> { errorLiveData.postValue(Event(result.error)) }
            is ResultUtil.NetworkError -> { errorLiveData.postValue(Event("네트워크 오류가 발생했습니다.")) }
        }
    }
}