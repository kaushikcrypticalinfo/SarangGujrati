package com.saranggujrati.repositories

import com.saranggujrati.webservice.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

import com.google.gson.JsonSyntaxException
import java.lang.IllegalStateException
import timber.log.Timber
import java.lang.IllegalArgumentException


abstract class BaseRepository {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            Resource.Loading
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                Timber.e(throwable)
                when (throwable) {
                    is HttpException -> {
                        if (throwable.code() == 401) {
                            Resource.Failure(true, null, 0)
                        } else {
                            Resource.Failure(false, apiCall.invoke(), throwable.code())
                        }
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