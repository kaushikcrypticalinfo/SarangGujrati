package com.saranggujrati.repositories

import com.saranggujrati.webservice.ApiService

class MainRepository(val api:ApiService):BaseRepository() {


    suspend fun fullScreenCardList() =
        safeApiCall { api.fullScreenCardList() }

}