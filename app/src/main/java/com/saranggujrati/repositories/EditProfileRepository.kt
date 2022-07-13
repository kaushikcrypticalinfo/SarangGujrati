package com.saranggujrati.repositories

import com.saranggujrati.webservice.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditProfileRepository(private val api:ApiService):BaseRepository() {

    suspend fun getProfile(id: String)=
        safeApiCall { api.getProfile(id) }

    suspend fun editProfileWithPassword(id: String,email:String,password:String,phone:String,name:String)=
        safeApiCall { api.editProfile(id,email,password,phone,name) }

    suspend fun editProfileWithoutPassword(id: String,email:String,phone:String,name:String)=
        safeApiCall { api.editProfileWithoutPassword(id,email,phone,name) }

    suspend fun deleteAccount(id: String)=
        safeApiCall { api.deleteAccount(id) }

    suspend fun updateProfilePhoto(id:RequestBody,photo: MultipartBody.Part)=
        safeApiCall { api.updateProfilePhoto(id,photo) }

}