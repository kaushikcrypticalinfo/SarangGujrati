package com.example.saranggujrati.repositories

import com.example.saranggujrati.webservice.ApiService

class CityCategoryBlogDetailRepository(private val api: ApiService) : BaseRepository() {

    suspend fun getCityCatBlogDetail(id: String) =
        safeApiCall { api.getCitiesCategoriesDetailBlog(id) }

    suspend fun getRssfeedList(id: String) =
        safeApiCall { api.getRssfeedList(id) }

    suspend fun getFeedLiveData(url: String) =
        safeApiCall { api.getFeedLiveData(url) }

}