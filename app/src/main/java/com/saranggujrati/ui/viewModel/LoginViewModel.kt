package com.saranggujrati.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saranggujrati.AppClass
import com.saranggujrati.model.*
import com.saranggujrati.repositories.LoginRepository
import com.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository):BaseViewModel(AppClass.instance) {

    //Login
    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse


    //SignUp
    private val _signUpResponse: MutableLiveData<Resource<SignUpResponse>> = MutableLiveData()
    val signUpResponse: LiveData<Resource<SignUpResponse>>
        get() = _signUpResponse


    //SocialLogin
    private val _socialLoginResponse: MutableLiveData<Resource<SocialLoginResponse>> =
        MutableLiveData()
    val socialLoginResponse: LiveData<Resource<SocialLoginResponse>>
        get() = _socialLoginResponse

    //ForgotPassword
    private val _forgotPasswordResponse: MutableLiveData<Resource<ForgotPasswordResponse>> =
        MutableLiveData()
    val forgotPasswordResponse: LiveData<Resource<ForgotPasswordResponse>>
        get() = _forgotPasswordResponse

    //ResetPassword
    private val _resetPasswordResponse: MutableLiveData<Resource<ResetPasswordResponse>> =
        MutableLiveData()
    val resetPasswordResponse: LiveData<Resource<ResetPasswordResponse>>
        get() = _resetPasswordResponse


    fun login(email:String,password:String) =
        viewModelScope.launch {

            _loginResponse.value=  Resource.Loading
            _loginResponse.value = repository.login(email,password)}

    fun signUp(email:String,password:String,phone:String,name: String) =
        viewModelScope.launch {
            _signUpResponse.value=  Resource.Loading
            _signUpResponse.value = repository.signUp(email,password,phone,name)}

    fun socialLogin(email:String,name:String,image:String,googleToken:String,deviceToken:String,loginFrom:String) =
        viewModelScope.launch {
            _socialLoginResponse.value=  Resource.Loading
            _socialLoginResponse.value = repository.socialLogin(email,name,image,googleToken,deviceToken,loginFrom)}


    fun forgotPassword(email:String) =
        viewModelScope.launch {
            _forgotPasswordResponse.value=  Resource.Loading
            _forgotPasswordResponse.value = repository.forgotPassword(email)}


    fun resetPassword(id:String,otp:String,email:String,cpassword:String,password:String) =
        viewModelScope.launch {
            _resetPasswordResponse.value=  Resource.Loading
            _resetPasswordResponse.value = repository.resetPassword(id,otp,email,cpassword,password)}


}