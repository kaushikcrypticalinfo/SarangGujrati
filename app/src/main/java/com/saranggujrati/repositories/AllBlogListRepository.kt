package com.saranggujrati.repositories

import com.saranggujrati.webservice.ApiService

class AllBlogListRepository(private val api: ApiService) : BaseRepository() {
    suspend fun getRssFeedList(id: String) =
        safeApiCall { api.getRssFeedList() }

    suspend fun getRssLatestNews() =
        safeApiCall { api.getRssLatestNews() }
}