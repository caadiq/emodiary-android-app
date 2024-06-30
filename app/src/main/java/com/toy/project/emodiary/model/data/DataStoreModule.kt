package com.toy.project.emodiary.model.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreModule(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "MyPrefs")

    private val saveIdKey = booleanPreferencesKey("save_id")
    private val emailKey = stringPreferencesKey("email")

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")

    suspend fun saveSaveId(saveId: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[saveIdKey] = saveId
        }
    }

    fun getSaveId(): Flow<Boolean?> {
        return context.dataStore.data.map { prefs ->
            prefs[saveIdKey]
        }
    }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[emailKey] = email
        }
    }

    fun getEmail(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[emailKey]
        }
    }

    suspend fun deleteEmail() {
        context.dataStore.edit { preferences ->
            preferences.remove(emailKey)
        }
    }

    // Access Token 저장
    suspend fun saveAccessToken(accessToken: String) {
        context.dataStore.edit { preferences ->
            preferences[accessTokenKey] = accessToken
        }
    }

    // Access Token 불러오기
    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[accessTokenKey]
        }
    }

    // Access Token 삭제
    suspend fun deleteAccessToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(accessTokenKey)
        }
    }

    // Refresh Token 저장
    suspend fun saveRefreshToken(refreshToken: String) {
        context.dataStore.edit { preferences ->
            preferences[refreshTokenKey] = refreshToken
        }
    }

    // Refresh Token 불러오기
    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[refreshTokenKey]
        }
    }

    // Refresh Token 삭제
    suspend fun deleteRefreshToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(refreshTokenKey)
        }
    }
}