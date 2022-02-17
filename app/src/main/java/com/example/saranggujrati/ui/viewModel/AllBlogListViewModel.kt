package com.example.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.model.*
import com.example.saranggujrati.model.rssFeed.RssFeed
import com.example.saranggujrati.repositories.AllBlogListRepository
import com.example.saranggujrati.repositories.AllNewsChannelRepository
import com.example.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch

class AllBlogListViewModel(private val repository: AllBlogListRepository) :
    BaseViewModel(AppClass.instance) {

    private val _feedList: MutableLiveData<Resource<FeedResponse>> =
        MutableLiveData()
    val feedList: LiveData<Resource<FeedResponse>>
        get() = _feedList

    private val _feedLiveData: MutableLiveData<Resource<RssFeed>> =
        MutableLiveData()
    val feedLiveData: LiveData<Resource<RssFeed>>
        get() = _feedLiveData

    fun getRssfeedList(id: String) =
        viewModelScope.launch {
            _feedList.value = Resource.Loading
            _feedList.value = repository.getRssfeedList(id)
        }

    fun getLiveData(url: String) =
        viewModelScope.launch {
            _feedLiveData.value = Resource.Loading
            _feedLiveData.value = repository.getFeedLiveData(url)
        }

}