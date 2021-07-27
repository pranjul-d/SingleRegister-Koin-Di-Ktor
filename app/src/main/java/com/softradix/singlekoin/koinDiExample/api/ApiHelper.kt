package com.softradix.singlekoin.koinDiExample.api

import com.softradix.singlekoin.koinDiExample.model.User
import retrofit2.Response

interface ApiHelper {

    suspend fun getUsers(): Response<List<User>>
}