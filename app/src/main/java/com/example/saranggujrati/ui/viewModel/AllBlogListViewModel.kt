package com.example.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.model.*
import com.example.saranggujrati.repositories.AllBlogListRepository
import com.example.saranggujrati.repositories.AllNewsChannelRepository
import com.example.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch

class AllBlogListViewModel(private val repository: AllBlogListRepository):BaseViewModel(AppClass.instance) {

    //TopCities
    private val _allBlogListResponse: MutableLiveData<Resource<AllBlogListResponse>> = MutableLiveData()
    val allBlogListResponse: LiveData<Resource<AllBlogListResponse>>
        get() = _allBlogListResponse




    fun getAllBlogList() =
        viewModelScope.launch {
            _allBlogListResponse.value=  Resource.Loading
            _allBlogListResponse.value = repository.getAllBlogList()}
}