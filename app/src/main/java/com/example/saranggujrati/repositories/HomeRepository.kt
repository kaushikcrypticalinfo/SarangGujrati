package com.example.saranggujrati.repositories

import com.example.saranggujrati.webservice.ApiService

class HomeRepository(private val api:ApiService):BaseRepository() {

    suspend fun topCitiesCategories()=
        safeApiCall { api.getTopCitiesCategories() }


    suspend fun getfeatureList()=
        safeApiCall { api.getFeatureList() }

}