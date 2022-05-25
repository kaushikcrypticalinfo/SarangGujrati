package com.example.saranggujrati.repositories

import com.example.saranggujrati.webservice.ApiService

class AllBlogListRepository(private val api: ApiService) : BaseRepository() {
    suspend fun getRssFeedList(id: String) =
        safeApiCall { api.getRssFeedList(id) }

}