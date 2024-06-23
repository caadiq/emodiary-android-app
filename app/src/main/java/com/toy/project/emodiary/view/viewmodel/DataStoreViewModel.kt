package com.toy.project.emodiary.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.toy.project.emodiary.model.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(private val repository: DataStoreRepository) : ViewModel() {
    val saveId = repository.getSaveId().asLiveData()

    val email = repository.getEmail().asLiveData()

    fun setSaveId(saveId: Boolean) {
        viewModelScope.launch {
            repository.saveSaveId(saveId)
        }
    }

    fun setEmail(email: String) {
        viewModelScope.launch {
            repository.saveEmail(email)
        }
    }

    fun deleteEmail() {
        viewModelScope.launch {
            repository.deleteEmail()
        }
    }
}