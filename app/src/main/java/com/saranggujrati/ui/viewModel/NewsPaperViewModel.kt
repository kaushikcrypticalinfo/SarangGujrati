package com.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saranggujrati.AppClass
import com.saranggujrati.model.*
import com.saranggujrati.repositories.AllNewsPaperListRepository
import com.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch

class NewsPaperViewModel(private val repository: AllNewsPaperListRepository):BaseViewModel(AppClass.instance) {

    //TopCities
    private val _newsPaperResponse: MutableLiveData<Resource<NewsPaperListResponse>> = MutableLiveData()
    val newsPaperResponse: LiveData<Resource<NewsPaperListResponse>>
        get() = _newsPaperResponse




    fun getAllNewsPaper(page: String) =
        viewModelScope.launch {
            _newsPaperResponse.value=  Resource.Loading
            _newsPaperResponse.value = repository.getNewsPaprList(page)}
}