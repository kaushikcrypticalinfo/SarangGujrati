package com.example.saranggujrati.repositories

import com.example.saranggujrati.webservice.ApiService

class LoginRepository(private val api:ApiService):BaseRepository() {

    suspend fun login(email:String,password:String)=
        safeApiCall { api.login(email,password) }

    suspend fun signUp(email:String,password:String,phone:String,name: String)=
        safeApiCall { api.signUp(email,password,phone,name) }


    suspend fun forgotPassword(email:String)=
        safeApiCall { api.forgotPassword(email) }

    suspend fun resetPassword(id:String,otp:String,email:String,channgePassword:String,password:String)=
        safeApiCall { api.resetPassword(id,otp,email,channgePassword,password) }

    suspend fun socialLogin(email:String,name:String,image:String,googleToken:String,deviceToken:String,loginFrom:String)=
        safeApiCall { api.socialLogin(email,name,image,googleToken,deviceToken,loginFrom) }
}