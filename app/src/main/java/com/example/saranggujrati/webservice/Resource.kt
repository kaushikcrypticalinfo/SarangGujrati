package com.example.saranggujrati.webservice

import okhttp3.ResponseBody



sealed class Resource <out T> {
    data class Success<out T>(val value: T) : Resource<T>()
/*
    data class Failure(val isNetworkError: Boolean, val errorCode: Int?, val errorBody: ResponseBody?,val message : String) : Resource<Nothing>()
*/
    data class Failure<out T>(val isNetworkError: Boolean,val value: T,val errorCode:Int) : Resource<T>()

    object Loading : Resource<Nothing>()

 }
