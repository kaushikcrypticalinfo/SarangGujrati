package com.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saranggujrati.AppClass
import com.saranggujrati.model.CardListRes
import com.saranggujrati.repositories.MainRepository
import com.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch

class MainViewModel(val repository: MainRepository):BaseViewModel(AppClass.instance) {


    private val _cardData: MutableLiveData<Resource<CardListRes>> =
        MutableLiveData()
    val cardLiveData: LiveData<Resource<CardListRes>>
        get() = _cardData


    fun fullScreenCardList() =
        viewModelScope.launch {
            _cardData.value = Resource.Loading
            _cardData.value = repository.fullScreenCardList()
        }
}