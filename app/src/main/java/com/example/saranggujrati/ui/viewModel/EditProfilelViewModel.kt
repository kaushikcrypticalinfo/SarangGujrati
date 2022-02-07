package com.example.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.model.*
import com.example.saranggujrati.repositories.AllNewsChannelRepository
import com.example.saranggujrati.repositories.EditProfileRepository
import com.example.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditProfilelViewModel(private val repository: EditProfileRepository):BaseViewModel(AppClass.instance) {

    //GetProfile
    private val _getProfileResponse: MutableLiveData<Resource<GetEditProfileResponse>> = MutableLiveData()
    val getProfileResponse: LiveData<Resource<GetEditProfileResponse>>
        get() = _getProfileResponse

    //DeleteAccount
    private val _deleteAccountResponse: MutableLiveData<Resource<DeleteAccountResponse>> = MutableLiveData()
    val deleteAccountResponse: LiveData<Resource<DeleteAccountResponse>>
        get() = _deleteAccountResponse

    //EditProfile
    private val _editProfileResponse: MutableLiveData<Resource<GetEditProfileResponse>> = MutableLiveData()
    val editProfileResponse: LiveData<Resource<GetEditProfileResponse>>
        get() = _editProfileResponse

    //UpdateProfilePhoto
    private val _updateProfilePhotoResponse: MutableLiveData<Resource<GetEditProfileResponse>> = MutableLiveData()
    val updateProfilePhotoResponse: LiveData<Resource<GetEditProfileResponse>>
        get() = _updateProfilePhotoResponse




    fun getProfile(id: String) =
        viewModelScope.launch {
            _getProfileResponse.value=  Resource.Loading
            _getProfileResponse.value = repository.getProfile(id)}

    fun deleteAccount(id: String) =
        viewModelScope.launch {
            _deleteAccountResponse.value=  Resource.Loading
            _deleteAccountResponse.value = repository.deleteAccount(id)}

    fun editProfileWithPassword(id: String,email:String,password:String,phone:String,name:String) =
        viewModelScope.launch {
            _editProfileResponse.value=  Resource.Loading
            _editProfileResponse.value = repository.editProfileWithPassword(id,email,password,phone,name)}

    fun editProfileWithoutPassword(id: String,email:String,phone:String,name:String) =
        viewModelScope.launch {
            _editProfileResponse.value=  Resource.Loading
            _editProfileResponse.value = repository.editProfileWithoutPassword(id,email,phone,name)}

    fun updateProfilePhoto(id: RequestBody, photo: MultipartBody.Part) =
        viewModelScope.launch {
            _updateProfilePhotoResponse.value=  Resource.Loading
            _updateProfilePhotoResponse.value = repository.updateProfilePhoto(id,photo)}
}