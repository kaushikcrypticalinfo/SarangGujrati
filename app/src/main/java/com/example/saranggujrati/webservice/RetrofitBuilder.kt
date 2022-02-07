package com.example.retrofitcoroutineexample.data.api

import com.example.saranggujrati.AppClass
import com.example.saranggujrati.BuildConfig
import com.example.saranggujrati.ui.SavedPrefrence
import com.example.saranggujrati.webservice.ApiService
import com.test.pausernew.api.factory.QualifiedTypeConverterFactory
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBuilder {

    private const val BASE_URL = "https://sarangnews.app/gujarati/api/"


    //print responce in json format
    private fun getRetrofitClient(authenticator: Authenticator? = null): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    if(SavedPrefrence.getApiToken(AppClass.appContext)!=""){
                        it.addHeader("Authorization", "Bearer ${SavedPrefrence.getApiToken(AppClass.appContext)}")
                    }
                }.build())
            }
            .also { client ->
                authenticator?.let { client.authenticator(it) }
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.connectTimeout(60, TimeUnit.SECONDS).build()
    }


    //create retrofit instance
    private fun getRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(QualifiedTypeConverterFactory(GsonConverterFactory.create(),Simpl))
            .addConverterFactory(GsonConverterFactory.create())
            .client(getRetrofitClient())
            .build()

    }
    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}





