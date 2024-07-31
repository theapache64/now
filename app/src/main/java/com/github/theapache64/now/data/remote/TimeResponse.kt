package com.github.theapache64.now.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimeResponse(
    @Json(name = "datetime")
    val datetime: String, // 2021-10-30T03:11:34.870411+05:30
)