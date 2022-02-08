package com.example.saranggujrati

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitcoroutineexample.data.api.RetrofitBuilder
import com.example.retrofitcoroutineexample.data.api.RetrofitBuilder.apiService
import com.example.saranggujrati.repositories.*
import com.example.saranggujrati.ui.viewModel.*
import com.example.saranggujrati.webservice.ApiService


class ViewModelFactory(application: Application, private val api: ApiService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(LoginRepository(apiService))
               isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(MainRepository(apiService))
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(HomeRepository(apiService))
                isAssignableFrom(NewsChannelViewModel::class.java) ->
                    NewsChannelViewModel(AllNewsChannelRepository(apiService))
                isAssignableFrom(EditProfilelViewModel::class.java) ->
                    EditProfilelViewModel(EditProfileRepository(apiService))
                isAssignableFrom(AllBlogListViewModel::class.java) ->
                    AllBlogListViewModel(AllBlogListRepository(apiService))
                isAssignableFrom(NewsPaperViewModel::class.java) ->
                    NewsPaperViewModel(AllNewsPaperListRepository(apiService))
                isAssignableFrom(FeatureBlogListViewModel::class.java) ->
                    FeatureBlogListViewModel(HomeRepository(apiService))
                isAssignableFrom(CityCatBlogDetailViewModel::class.java) ->
                    CityCatBlogDetailViewModel(CityCategoryBlogDetailRepository(apiService))
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null


        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    application,
                    apiService
                ).also { INSTANCE = it }
            }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }

    }
}
