package com.toy.project.emodiary.model.di

import android.content.Context
import com.toy.project.emodiary.model.data.DataStoreModule
import com.toy.project.emodiary.model.repository.DataStoreRepository
import com.toy.project.emodiary.model.service.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = RetrofitService.getRetrofit()

    @Provides
    @Singleton
    @Named("auth")
    fun provideRetrofitWithAuth(dataStoreRepository: DataStoreRepository): Retrofit = RetrofitService.getRetrofitWithAuth(dataStoreRepository)

    @Provides
    @Singleton
    fun provideDataStoreModule(@ApplicationContext context: Context) = DataStoreModule(context)

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStoreModule: DataStoreModule) = DataStoreRepository(dataStoreModule)
}