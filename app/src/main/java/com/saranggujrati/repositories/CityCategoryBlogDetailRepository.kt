package com.saranggujrati.repositories

import com.saranggujrati.webservice.ApiService

class CityCategoryBlogDetailRepository(private val api: ApiService) : BaseRepository() {

    suspend fun getCityCatBlogDetail(id: String) =
        safeApiCall { api.getCitiesCategoriesDetailBlog(id) }

    suspend fun getRssFeedList(parentId: String, id: String, exclude_ids: String) =
        safeApiCall { api.getRssFeedList(parentId, id, exclude_ids) }

    suspend fun getFeedLiveData(url: String) =
        safeApiCall { api.getFeedLiveData(url) }

}