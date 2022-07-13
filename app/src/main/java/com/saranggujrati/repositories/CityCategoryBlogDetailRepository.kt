package com.saranggujrati.repositories

import com.saranggujrati.webservice.ApiService

class CityCategoryBlogDetailRepository(private val api: ApiService) : BaseRepository() {

    suspend fun getCityCatBlogDetail(id: String) =
        safeApiCall { api.getCitiesCategoriesDetailBlog(id) }

    suspend fun getRssFeedList(parentId: String, id: String) =
        safeApiCall { api.getRssFeedList(parentId,id) }

    suspend fun getFeedLiveData(url: String) =
        safeApiCall { api.getFeedLiveData(url) }

}