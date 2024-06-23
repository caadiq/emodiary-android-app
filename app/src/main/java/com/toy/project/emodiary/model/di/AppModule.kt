package com.toy.project.emodiary.model.di

import android.content.Context
import com.toy.project.emodiary.model.data.DataStoreModule
import com.toy.project.emodiary.model.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStoreModule(@ApplicationContext context: Context) = DataStoreModule(context)

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStoreModule: DataStoreModule) = DataStoreRepository(dataStoreModule)
}