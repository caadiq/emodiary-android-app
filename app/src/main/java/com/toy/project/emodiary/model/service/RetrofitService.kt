package com.toy.project.emodiary.model.service

import android.util.Log
import com.auth0.android.jwt.JWT
import com.toy.project.emodiary.BuildConfig
import com.toy.project.emodiary.model.api.AuthApi
import com.toy.project.emodiary.model.data.UserData
import com.toy.project.emodiary.model.repository.DataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

object RetrofitService {
    private var retrofit: Retrofit? = null

    fun getRetrofit(): Retrofit {
        return retrofit ?: Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRetrofitWithAuth(dataStoreRepository: DataStoreRepository): Retrofit {
        val interceptorClient = OkHttpClient().newBuilder()
            .addInterceptor(RequestInterceptor(dataStoreRepository, getRetrofit().create(AuthApi::class.java)))
            .addInterceptor(ResponseInterceptor(dataStoreRepository, getRetrofit().create(AuthApi::class.java)))
            .build()

        return retrofit ?: Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(interceptorClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

// 요청을 가로챔 (Access Token을 추가하기 위해)
class RequestInterceptor(private val dataStoreRepository: DataStoreRepository, private val authApi: AuthApi) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { dataStoreRepository.getAccessToken().first() }
        val refreshToken = runBlocking { dataStoreRepository.getRefreshToken().first()?.let { JWT(it) } }

        refreshToken?.expiresAt?.time?.let { expiresAt ->
            // 한국시간 기준으로 Refresh Token의 만료 시간을 계산
            val currentTimeMillis = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli()
            val refreshTokenRemain = expiresAt - currentTimeMillis

            // Refresh Token의 만료가 2일 이내로 남았을 경우
            if (refreshTokenRemain < 2 * 24 * 60 * 60 * 1000) {
                // Refresh Token 재발급 요청
                val response = authApi.reissueRefreshToken("Bearer $accessToken").execute()

                if (response.isSuccessful) { // RefreshToken 재발급 성공 시
                    response.body()?.refreshToken?.let { newRefreshToken ->
                        runBlocking {
                            dataStoreRepository.saveRefreshToken(newRefreshToken)
                        }
                    }
                }
            }
        }

        // 헤더에 Access Token 추가
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(request)
    }
}

// 응답을 가로챔 (Access Token 만료 시 재발급을 위해)
class ResponseInterceptor @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val authApi: AuthApi
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 응답을 받음
        val originalResponse = chain.proceed(chain.request())
        // 응답 본문(Body)을 String으로 변환
        val responseBodyString = originalResponse.body?.string() ?: ""

        // 응답 코드가 401(Unauthorized)일 경우
        if (originalResponse.code == 401) {
            // Body를 JSONObject로 변환 후 에러 메시지를 가져옴
            val errorMessage = JSONObject(responseBodyString).optString("message")

            // AccessToken이 만료되었을 경우
            if (errorMessage == "JWT 토큰이 만료되었습니다.") {
                // DataStore에서 AccessToken과 RefreshToken을 가져옴
                val accessToken = runBlocking { dataStoreRepository.getAccessToken().first() }
                val refreshToken = runBlocking { dataStoreRepository.getRefreshToken().first() }

                if (accessToken != null && refreshToken != null) {
                    // AccessToken과 RefreshToken을 이용해 AccessToken 재발급 요청
                    val response = authApi.reissueAccessToken(accessToken, refreshToken).execute()

                    if (response.isSuccessful) { // AccessToken 재발급 성공 시
                        response.body()?.accessToken?.let { newAccessToken ->
                            runBlocking {
                                dataStoreRepository.saveAccessToken(newAccessToken)
                            }

                            val newRequest = chain.request().newBuilder()
                                .removeHeader("Authorization")
                                .addHeader("Authorization", "Bearer $newAccessToken")
                                .build()

                            return chain.proceed(newRequest)
                        }
                    } else { // RefreshToken이 만료되었을 경우
                        runBlocking {
                            // DataStore에 저장된 AccessToken과 RefreshToken을 삭제
                            dataStoreRepository.deleteAccessToken()
                            dataStoreRepository.deleteRefreshToken()

                            // 저장되어 있는 유저 정보를 삭제
                            UserData.clearUserData()
                        }
                    }
                }
            }
        }

        // Body를 다시 ResponseBody로 변환 (Body를 String으로 변환할 경우 스트림이 닫혀 사용이 불가능해짐)
        val responseBody = responseBodyString.toResponseBody(originalResponse.body?.contentType())
        // 변환된 ResponseBody로 다시 응답을 생성해서 반환
        return originalResponse.newBuilder().body(responseBody).build()
    }
}