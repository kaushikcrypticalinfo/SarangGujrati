package com.saranggujrati.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.android.youtube.player.internal.e
import com.saranggujrati.model.NewsData
import com.saranggujrati.utils.STARTING_PAGE_INDEX
import com.saranggujrati.webservice.ApiService
import retrofit2.HttpException
import java.io.IOException

class ApiPagingSource(val apiService: ApiService) :
    PagingSource<Int, NewsData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsData> {

        return try {
            //for first case it will be null, then we can pass some default value, in our case it's 1
            val pageIndex = params.key ?: STARTING_PAGE_INDEX

            // Suspending network load via Retrofit. This doesn't need to be wrapped in a
            // withContext(Dispatcher.IO) { ... } block since Retrofit's Coroutine
            // CallAdapter dispatches on a worker thread.
            val response = apiService.getLiveChannelWithPaging(
                page = pageIndex.toString()
            )
            val movies = response.data.data.toMutableList()

            // Since 0 is the lowest page number, return null to signify no more pages should
            // be loaded before it.
            val prevKey = if (pageIndex > STARTING_PAGE_INDEX) pageIndex - 1 else null

            // By default, initial load size = 3 * NETWORK PAGE SIZE
            // ensure we're not requesting duplicating items at the 2nd request
            val nextKey = if (movies.isEmpty()) null else pageIndex + 1

            LoadResult.Page(
                data = movies,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NewsData>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}