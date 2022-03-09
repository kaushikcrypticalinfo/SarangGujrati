package com.example.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.model.CardListRes
import com.example.saranggujrati.repositories.LoginRepository
import com.example.saranggujrati.repositories.MainRepository
import com.example.saranggujrati.webservice.Resource
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