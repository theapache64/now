package com.github.theapache64.now.di


import android.os.StrictMode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.util.Date
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FakeUrlModule {

    @Provides
    @Singleton
    @Named("world_time_api_url")
    fun provideWorldTimeApiUrl(): String {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        val server = MockWebServer()
        server.dispatcher = object : Dispatcher() {

            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/timezone/Asia/Kolkata" -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(
                                """
                              {"datetime":"${Date()}"}
                          """.trimIndent()
                            )
                    }

                    else -> throw IllegalStateException("Unknown path `${request.path}`")
                }
            }
        }
        server.start()
        return server.url("").toString()
    }
}