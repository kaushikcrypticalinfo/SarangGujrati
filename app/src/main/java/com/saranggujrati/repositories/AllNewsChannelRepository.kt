package com.saranggujrati.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.saranggujrati.model.NewsData
import com.saranggujrati.paging.ApiPagingSource
import com.saranggujrati.utils.NETWORK_PAGE_SIZE
import com.saranggujrati.webservice.ApiService
import kotlinx.coroutines.flow.Flow

class AllNewsChannelRepository(private val api: ApiService) : BaseRepository() {

    suspend fun getNewsChannel(page: String) =
        safeApiCall { api.getLiveNewsChannelList(page) }

    fun getNewChannelPaging(): Flow<PagingData<NewsData>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                ApiPagingSource(api)
            }
        ).flow
    }

}