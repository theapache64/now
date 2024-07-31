package com.github.theapache64.now.data.repo

import com.github.theapache64.now.data.remote.WorldTimeApi
import javax.inject.Inject

interface DateTimeRepo {
    suspend fun getCurrentDateTime(): String
}

class DateTimeRepoImpl @Inject constructor(
    private val worldTimeApi: WorldTimeApi
) : DateTimeRepo {
    override suspend fun getCurrentDateTime(): String {
        return worldTimeApi.getTime().datetime
    }
}