package com.example.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.model.*
import com.example.saranggujrati.model.rssFeed.RssFeed

import com.example.saranggujrati.repositories.AllBlogListRepository
import com.example.saranggujrati.webservice.Resource
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

}