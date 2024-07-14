package com.github.theapache64.now.di.module


import com.github.theapache64.now.data.remote.WorldTimeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideWorldTimeApi(
        @Named("world_time_api_url") worldTimeApiUrl: String
    ): WorldTimeApi {
        return Retrofit.Builder()
            .baseUrl(worldTimeApiUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(WorldTimeApi::class.java)
    }
}