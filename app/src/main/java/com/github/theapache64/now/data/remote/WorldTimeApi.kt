package com.github.theapache64.now.data.remote

import retrofit2.http.GET

const val TIME_URL = "timezone/Asia/Kolkata"

interface WorldTimeApi {
    @GET(TIME_URL)
    suspend fun getTime(): TimeResponse
}