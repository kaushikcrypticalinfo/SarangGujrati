package com.saranggujrati

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitcoroutineexample.data.api.RetrofitBuilder.apiService
import com.saranggujrati.repositories.*
import com.saranggujrati.ui.viewModel.*


class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {

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

        @JvmStatic
        @Volatile
        private var INSTANCE: ViewModelFactory? = null


        fun getInstance() =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory().also { INSTANCE = it }
            }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }

    }
}
