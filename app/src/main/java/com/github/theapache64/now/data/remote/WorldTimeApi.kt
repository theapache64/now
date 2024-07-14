package com.github.theapache64.now.data.remote

import retrofit2.http.GET

interface WorldTimeApi {
    @GET("timezone/Asia/Kolkata")
    suspend fun getTime(): TimeResponse
}