package com.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saranggujrati.AppClass
import com.saranggujrati.model.*
import com.saranggujrati.model.onDemand.OnDemandRes
import com.saranggujrati.repositories.HomeRepository
import com.saranggujrati.webservice.Resource
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

    private val _apiRecord: MutableLiveData<Resource<ApiRecordResponse>> =
        MutableLiveData()
    val apiRecord: LiveData<Resource<ApiRecordResponse>>
        get() = _apiRecord


    fun gettTopCitiesCategories() =
        viewModelScope.launch {
            _topCitiesResponse.value=  Resource.Loading
            _topCitiesResponse.value = repository.topCitiesCategories()}

    fun gettTopCategories() =
        viewModelScope.launch {
            _topCategoryResponse.value=  Resource.Loading
            _topCategoryResponse.value = repository.topCitiesCategories()}


    fun getFeatureList() =
        viewModelScope.launch {
            _featureListResponse.value=  Resource.Loading
            _featureListResponse.value = repository.getFeatureList()}

    fun getOnDemandList() =
        viewModelScope.launch {
            _onDemandList.value=  Resource.Loading
            _onDemandList.value = repository.getOnDemandList()}

    fun getApiRecordFound() =
        viewModelScope.launch {
            _apiRecord.value = Resource.Loading
            _apiRecord.value = repository.apiRecordFound()
        }
}