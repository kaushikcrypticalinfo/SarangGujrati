package com.example.retrofitcoroutineexample.data.api


import com.saranggujrati.BuildConfig
import com.saranggujrati.utils.gujarati_flavors
import com.saranggujrati.utils.kathiyawadi_khamir
import com.saranggujrati.utils.odia
import com.saranggujrati.webservice.ApiService
import com.saranggujrati.webservice.factory.QualifiedTypeConverterFactory
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBuilder {

//    private const val BASE_URL = "https://sarangnews.app/gujarati/api/"
//    private const val BASE_URL = "http://newsnmore.in/api/"
    private const val BASE_URL = "https://kathiyawadikhamir.in/api/"

    //print responce in json format
    private fun getRetrofitClient(authenticator: Authenticator? = null): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    /*it.addHeader(
                        "Authorization",
                        "Bearer ${SavedPrefrence.getApiToken(AppClass.appContext)}"
                    )*/
                    it.addHeader(
                        "siteid",
                        when (BuildConfig.FLAVOR) {
                            gujarati_flavors -> "1"
                            kathiyawadi_khamir -> "7"
                            odia -> "6"
                            else -> "4"
                        }
                    )
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
            .addConverterFactory(
                QualifiedTypeConverterFactory(
                    GsonConverterFactory.create(),
                    SimpleXmlConverterFactory.create()
                )
            )
            .addConverterFactory(GsonConverterFactory.create())
            .client(getRetrofitClient())
            .build()

    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}





