package com.example.saranggujrati.repositories

import com.example.saranggujrati.webservice.ApiService

class AllNewsChannelRepository(private val api:ApiService):BaseRepository() {

    suspend fun getNewsChannel(page: String)=
        safeApiCall { api.getLiveNewsChannelList(page) }

}