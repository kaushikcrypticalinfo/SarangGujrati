package com.saranggujrati.repositories

import com.saranggujrati.webservice.ApiService

class HomeRepository(private val api:ApiService):BaseRepository() {

    suspend fun topCitiesCategories()=
        safeApiCall { api.getTopCitiesCategories() }


    suspend fun getfeatureList()=
        safeApiCall { api.getFeatureList() }

  suspend fun getOnDemandList()=
        safeApiCall { api.getOnDemandList() }

}