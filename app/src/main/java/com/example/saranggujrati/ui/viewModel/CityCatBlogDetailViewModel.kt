package com.example.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.model.*
import com.example.saranggujrati.model.rssFeed.RssFeed

import com.example.saranggujrati.repositories.CityCategoryBlogDetailRepository
import com.example.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch

class CityCatBlogDetailViewModel(private val repository: CityCategoryBlogDetailRepository) :
    BaseViewModel(AppClass.instance) {

    //CityCategoryBlogDetail
    private val _cityCatBlogDetailResponse: MutableLiveData<Resource<CityCategoryBlogDetailResponse>> =
        MutableLiveData()
    val cityCatBlogDetailResponse: LiveData<Resource<CityCategoryBlogDetailResponse>>
        get() = _cityCatBlogDetailResponse

    private val _feedList: MutableLiveData<Resource<FeedResponse>> =
        MutableLiveData()
    val feedList: LiveData<Resource<FeedResponse>>
        get() = _feedList

    private val _feedLiveData: MutableLiveData<Resource<RssFeed>> =
        MutableLiveData()
    val feedLiveData: LiveData<Resource<RssFeed>>
        get() = _feedLiveData

    fun getCityCatBlogDetail(id: String) =
        viewModelScope.launch {
            _cityCatBlogDetailResponse.value = Resource.Loading
            _cityCatBlogDetailResponse.value = repository.getCityCatBlogDetail(id)
        }

    fun getRssfeedList(parentId: String, id: String) =
        viewModelScope.launch {
            _feedList.value = Resource.Loading
            _feedList.value = repository.getRssfeedList(parentId,id)
        }

    fun getLiveData(url: String) =
        viewModelScope.launch {
            _feedLiveData.value = Resource.Loading
            _feedLiveData.value = repository.getFeedLiveData(url)
        }
}