package com.example.jbmatrixassignment.di

import com.example.jbmatrixassignment.data.remote.remoteinterface.VideoApi
import com.example.jbmatrixassignment.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class Module {

    @Provides
    @Singleton
    fun getRetrofitInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun getVideoApi(retrofit: Retrofit): VideoApi{
        return retrofit.create(VideoApi::class.java)
    }
}