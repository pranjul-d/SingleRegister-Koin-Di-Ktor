package com.softradix.singlekoin.koinDiExample.api

import com.softradix.singlekoin.koinDiExample.model.User
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {


    @GET("users")
    suspend fun getUsers(): Response<List<User>>
}