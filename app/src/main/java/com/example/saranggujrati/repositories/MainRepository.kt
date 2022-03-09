package com.example.saranggujrati.repositories

import com.example.saranggujrati.webservice.ApiService

class MainRepository(val api:ApiService):BaseRepository() {


    suspend fun fullScreenCardList() =
        safeApiCall { api.fullScreenCardList() }

}