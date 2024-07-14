package com.github.theapache64.now.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class UrlModule {

    @Provides
    @Named("world_time_api_url")
    fun provideWorldTimeApiUrl(): String {
        return "https://worldtimeapi.org/api/"
    }
}