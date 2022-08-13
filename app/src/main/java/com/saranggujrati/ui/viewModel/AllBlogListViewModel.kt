package com.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saranggujrati.AppClass
import com.saranggujrati.model.*

import com.saranggujrati.repositories.AllBlogListRepository
import com.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch

class AllBlogListViewModel(private val repository: AllBlogListRepository) :
    BaseViewModel(AppClass.instance) {

    private val _feedList: MutableLiveData<Resource<FeedResponse>> =
        MutableLiveData()
    val feedList: LiveData<Resource<FeedResponse>>
        get() = _feedList

    fun getRssFeedList(id: String) =
        viewModelScope.launch {
            _feedList.value = Resource.Loading
            _feedList.value = repository.getRssFeedList(id)
        }
  fun getRssLatestNews() =
        viewModelScope.launch {
            _feedList.value = Resource.Loading
            _feedList.value = repository.getRssLatestNews()
        }

}