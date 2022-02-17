package com.example.saranggujrati.repositories

import com.example.saranggujrati.webservice.ApiService

class AllBlogListRepository(private val api:ApiService):BaseRepository() {

    suspend fun getAllBlogList()=
        safeApiCall { api.getAllBlogList() }

    suspend fun getFeedLiveData(url: String) =
        safeApiCall { api.getFeedLiveData(url) }

    suspend fun getRssfeedList(id: String) =
        safeApiCall { api.getRssfeedList(id) }

}