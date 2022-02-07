package com.example.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.model.*
import com.example.saranggujrati.repositories.AllBlogListRepository
import com.example.saranggujrati.repositories.AllNewsChannelRepository
import com.example.saranggujrati.repositories.HomeRepository
import com.example.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch

class FeatureBlogListViewModel(private val repository: HomeRepository):BaseViewModel(AppClass.instance) {

    //TopCities
    private val _featureBlogListResponse: MutableLiveData<Resource<BlogFeatureList>> = MutableLiveData()
    val featureBlogListResponse: LiveData<Resource<BlogFeatureList>>
        get() = _featureBlogListResponse




    fun getFeatureBlogList() =
        viewModelScope.launch {
            _featureBlogListResponse.value=  Resource.Loading
            _featureBlogListResponse.value = repository.getfeatureList()}
}