package com.saranggujrati.repositories

import com.saranggujrati.webservice.ApiService

class AllNewsPaperListRepository(private val api:ApiService):BaseRepository() {

    suspend fun getNewsPaprList(page: String)=
        safeApiCall { api.getNewsPaperlList(page) }

}