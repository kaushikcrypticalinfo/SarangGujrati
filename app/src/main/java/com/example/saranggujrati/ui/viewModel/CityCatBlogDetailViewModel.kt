package com.example.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.model.*
import com.example.saranggujrati.repositories.AllBlogListRepository
import com.example.saranggujrati.repositories.AllNewsChannelRepository
import com.example.saranggujrati.repositories.CityCategoryBlogDetailRepository
import com.example.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch

class CityCatBlogDetailViewModel(private val repository: CityCategoryBlogDetailRepository):BaseViewModel(AppClass.instance) {

    //CityCategoryBlogDetail
    private val _cityCatBlogDetailResponse: MutableLiveData<Resource<CityCategoryBlogDetailResponse>> = MutableLiveData()
    val cityCatBlogDetailResponse: LiveData<Resource<CityCategoryBlogDetailResponse>>
        get() = _cityCatBlogDetailResponse




    fun getCityCatBlogDetail(id:String) =
        viewModelScope.launch {
            _cityCatBlogDetailResponse.value=  Resource.Loading
            _cityCatBlogDetailResponse.value = repository.getCityCatBlogDetail(id)}
}