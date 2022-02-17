package com.example.saranggujrati.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.saranggujrati.model.NewsData
import com.example.saranggujrati.utils.STARTING_PAGE_INDEX
import com.example.saranggujrati.webservice.ApiService
import retrofit2.HttpException
import java.io.IOException

class ApiPagingSource(val apiService: ApiService) :
    PagingSource<Int, NewsData>() {

    override val keyReuseSupported: Boolean
        get() = true

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsData> {
        //for first case it will be null, then we can pass some default value, in our case it's 1
        val pageIndex = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = apiService.getLiveWithPaging(
                page = pageIndex.toString()
            )
            val movies = response.data.data.toMutableList()
            val nextKey =
            // By default, initial load size = 3 * NETWORK PAGE SIZE
                // ensure we're not requesting duplicating items at the 2nd request
                if (movies.isEmpty()) null else pageIndex + 1

            LoadResult.Page(
                data = movies,
                prevKey = if (pageIndex > 1) pageIndex - 1 else null,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NewsData>): Int? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.id
        }
    }


}