package com.example.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.model.*
import com.example.saranggujrati.repositories.AllNewsChannelRepository
import com.example.saranggujrati.repositories.AllNewsPaperListRepository
import com.example.saranggujrati.webservice.Resource
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