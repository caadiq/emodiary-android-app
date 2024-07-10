package com.toy.project.emodiary.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toy.project.emodiary.model.dto.DiaryListDto
import com.toy.project.emodiary.model.repository.DiaryRepository
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

    private val _isFirstTime = MutableLiveData<Boolean>()
    val isFirstTime: LiveData<Boolean> = _isFirstTime

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

    fun setIsFirstTime(isFirstTime: Boolean) {
        _isFirstTime.value = isFirstTime
    }
}