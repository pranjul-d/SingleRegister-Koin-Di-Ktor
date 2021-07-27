package com.softradix.singlekoin.koinDiExample.repository

import com.softradix.singlekoin.koinDiExample.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getUsers() = apiHelper.getUsers()
}