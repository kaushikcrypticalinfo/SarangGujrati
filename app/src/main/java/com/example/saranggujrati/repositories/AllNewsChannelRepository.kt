package com.example.saranggujrati.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.saranggujrati.model.NewsData
import com.example.saranggujrati.model.NewsPaperData
import com.example.saranggujrati.paging.ApiPagingSource
import com.example.saranggujrati.utils.NETWORK_PAGE_SIZE
import com.example.saranggujrati.webservice.ApiService
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