package com.softradix.singlekoin.koinDiExample.api

import com.softradix.singlekoin.koinDiExample.model.User
import retrofit2.Response

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun getUsers(): Response<List<User>> = apiService.getUsers()

}