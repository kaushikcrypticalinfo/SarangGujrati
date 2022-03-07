package com.example.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.model.*
import com.example.saranggujrati.model.onDemand.OnDemandRes
import com.example.saranggujrati.repositories.HomeRepository
import com.example.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository):BaseViewModel(AppClass.instance) {

    //TopCities
    private val _topCitiesResponse: MutableLiveData<Resource<CitCategoryListResponse>> = MutableLiveData()
    val topCitiesResponse: LiveData<Resource<CitCategoryListResponse>>
        get() = _topCitiesResponse

    //TopCities
    private val _topCategoryResponse: MutableLiveData<Resource<CitCategoryListResponse>> = MutableLiveData()
    val topCategoryResponse: LiveData<Resource<CitCategoryListResponse>>
        get() = _topCategoryResponse

    //FeatureList
    private val _featureListResponse: MutableLiveData<Resource<BlogFeatureList>> = MutableLiveData()
    val featureListResponse: LiveData<Resource<BlogFeatureList>>
        get() = _featureListResponse

    private val _onDemandList: MutableLiveData<Resource<OnDemandRes>> = MutableLiveData()
    val onDemandList: LiveData<Resource<OnDemandRes>>
        get() = _onDemandList


    fun gettTopCitiesCategories() =
        viewModelScope.launch {
            _topCitiesResponse.value=  Resource.Loading
            _topCitiesResponse.value = repository.topCitiesCategories()}

    fun gettTopCategories() =
        viewModelScope.launch {
            _topCategoryResponse.value=  Resource.Loading
            _topCategoryResponse.value = repository.topCitiesCategories()}


    fun gettFeatureList() =
        viewModelScope.launch {
            _featureListResponse.value=  Resource.Loading
            _featureListResponse.value = repository.getfeatureList()}

    fun getOnDemandList() =
        viewModelScope.launch {
            _onDemandList.value=  Resource.Loading
            _onDemandList.value = repository.getOnDemandList()}
}