package com.example.saranggujrati.repositories

import android.content.Context
import com.example.saranggujrati.webservice.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException
import retrofit2.HttpException
import android.widget.Toast

import android.net.NetworkInfo

import androidx.core.content.ContextCompat.getSystemService

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody.Companion.toResponseBody
import java.lang.IllegalStateException
import org.json.JSONObject
import retrofit2.Call
import java.lang.IllegalArgumentException


abstract class BaseRepository {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            Resource.Loading
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        Resource.Failure(false, apiCall.invoke(), throwable.code())
                    }
                    is IllegalStateException -> {
                        Resource.Failure(false, apiCall.invoke(), 0)
                    }
                    is JsonSyntaxException -> {
                        Resource.Failure(false, apiCall.invoke(), 0)
                    }
                    is IllegalArgumentException -> {
                        Resource.Failure(false, apiCall.invoke(), 0)
                    }
                    else -> {
                        Resource.Failure(true, null, 0)
                    }
                }
            } as Resource<T>


        }
    }
}


/*  is HttpException -> {
                                                          Resource.Failure(false, throwable.code(), throwable.response()?.errorBody(),throwable.message(),apiCall.invoke())
                                                      }

                                                      is IllegalStateException -> {
                                                          Resource.Failure(false, null, throwable.message?.toResponseBody(),
                                                              throwable.message.toString(),apiCall.invoke())
                                                      }

                                                      is JsonSyntaxException -> {
                                                          Resource.Failure(false, null, throwable.message.toResponseBody(),throwable.message.toString(),apiCall.invoke())
                                                      }

                                                      else -> {
                                                          Resource.Failure(true, null, null,"",null)
                                                      }*/