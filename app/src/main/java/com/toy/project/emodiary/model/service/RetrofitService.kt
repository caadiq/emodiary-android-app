package com.toy.project.emodiary.model.service

import com.toy.project.emodiary.BuildConfig
import com.toy.project.emodiary.model.repository.DataStoreRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private var retrofit: Retrofit? = null
    private var requestInterceptor: RequestInterceptor? = null

    fun getRetrofit(): Retrofit {
        return retrofit ?: Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRetrofitWithAuth(dataStoreRepository: DataStoreRepository): Retrofit {
        if (requestInterceptor == null) {
            requestInterceptor = RequestInterceptor(dataStoreRepository)
        }

        val interceptorClient = OkHttpClient().newBuilder()
            .addInterceptor(requestInterceptor!!)
            .build()

        return retrofit ?: Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(interceptorClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

// 요청을 가로챔 (Access Token을 추가하기 위해)
class RequestInterceptor(private val dataStoreRepository: DataStoreRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            dataStoreRepository.getAccessToken()
        }

        // 헤더에 Access Token 추가
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(request)
    }
}