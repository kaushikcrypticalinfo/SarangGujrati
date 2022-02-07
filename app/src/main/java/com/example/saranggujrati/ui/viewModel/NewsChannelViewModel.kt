package com.example.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.model.*
import com.example.saranggujrati.repositories.AllNewsChannelRepository
import com.example.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch

class NewsChannelViewModel(private val repository: AllNewsChannelRepository):BaseViewModel(AppClass.instance) {

    //TopCities
    private val _newsChannelResponse: MutableLiveData<Resource<NewsChannelListRespnse>> = MutableLiveData()
    val newsChannelResponse: LiveData<Resource<NewsChannelListRespnse>>
        get() = _newsChannelResponse




    fun getAllNewsChannel(page: String) =
        viewModelScope.launch {
            _newsChannelResponse.value=  Resource.Loading
            _newsChannelResponse.value = repository.getNewsChannel(page)}
}