package com.toy.project.emodiary.model.repository

import com.toy.project.emodiary.model.data.DataStoreModule
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreRepository @Inject constructor(private val dataStore: DataStoreModule) {
    suspend fun saveSaveId(saveId: Boolean) {
        dataStore.saveSaveId(saveId)
    }

    fun getSaveId() = dataStore.getSaveId()

    suspend fun saveEmail(email: String) {
        dataStore.saveEmail(email)
    }

    fun getEmail() = dataStore.getEmail()

    suspend fun deleteEmail() {
        dataStore.deleteEmail()
    }

    suspend fun saveAccessToken(accessToken: String) {
        dataStore.saveAccessToken(accessToken)
    }

    fun getAccessToken(): Flow<String?> {
        return dataStore.getAccessToken()
    }

    suspend fun deleteAccessToken() {
        dataStore.deleteAccessToken()
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.saveRefreshToken(refreshToken)
    }

    fun getRefreshToken(): Flow<String?> {
        return dataStore.getRefreshToken()
    }

    suspend fun deleteRefreshToken() {
        dataStore.deleteRefreshToken()
    }
}