package com.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saranggujrati.AppClass
import com.saranggujrati.model.*
import com.saranggujrati.repositories.HomeRepository
import com.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch

class FeatureBlogListViewModel(private val repository: HomeRepository):BaseViewModel(AppClass.instance) {

    //TopCities
    private val _featureBlogListResponse: MutableLiveData<Resource<BlogFeatureList>> = MutableLiveData()
    val featureBlogListResponse: LiveData<Resource<BlogFeatureList>>
        get() = _featureBlogListResponse


    fun getFeatureBlogList() =
        viewModelScope.launch {
            _featureBlogListResponse.value=  Resource.Loading
            _featureBlogListResponse.value = repository.getFeatureList()}

}