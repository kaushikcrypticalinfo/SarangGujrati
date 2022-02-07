package com.example.saranggujrati.webservice

import com.example.saranggujrati.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    //login Api
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    //SignUp Api
    @FormUrlEncoded
    @POST("register")
    suspend fun signUp(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("name") name: String,
    ): SignUpResponse

    //Forgot Password Api
    @FormUrlEncoded
    @POST("forgot-password")
    suspend fun forgotPassword(
        @Field("email") email: String,
    ): ForgotPasswordResponse


    //Reset Password Api
    @FormUrlEncoded
    @POST("reset-password")
    suspend fun resetPassword(
        @Field("id") id: String,
        @Field("otp") otp: String,
        @Field("email") email: String,
        @Field("cpassword") changePass: String,
        @Field("password") password: String,
    ): ResetPasswordResponse


    //Social Login Api
    @FormUrlEncoded
    @POST("socialMediaLogin")
    suspend fun socialLogin(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("image") image: String,
        @Field("google_token") googleToken: String,
        @Field("device_token") deviceToken: String,
        @Field("login_from") loginFrom: String,
    ): SocialLoginResponse

    //city & category value list
    @GET("category-list")
    suspend fun getTopCitiesCategories(): CitCategoryListResponse

    //city & category detail
    @GET("blog-category-list/{id}")
    suspend fun getCitiesCategoriesDetailBlog(@Path("id") id: String): CityCategoryBlogDetailResponse



    //Featured List
    @GET("blog-list")
    suspend fun getFeatureList(): BlogFeatureList

    //news channel list
    @GET("live-news-list")
    suspend fun getLiveNewsChannelList( @Query("page") page: String,): NewsChannelListRespnse


    //news paper list
    @GET("news-paper-list")
    suspend fun getNewsPaperlList( @Query("page") page: String): NewsPaperListResponse


    //All Blog Data
    @GET("blog-all-list")
    suspend fun getAllBlogList(): AllBlogListResponse


    //GetProfile Api
    @FormUrlEncoded
    @POST("getProfile")
    suspend fun getProfile(
        @Field("id") id: String,
    ): GetEditProfileResponse

    //EditProfile Api
    @FormUrlEncoded
    @POST("updateProfile")
    suspend fun editProfile(
        @Field("id") id: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("name") name: String,
    ): GetEditProfileResponse

    //EditProfile Api
    @FormUrlEncoded
    @POST("updateProfile")
    suspend fun editProfileWithoutPassword(
        @Field("id") id: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("name") name: String,
    ): GetEditProfileResponse
    //EditProfilePicture Api
    @Multipart
    @POST("updateProfilePicture")
    suspend fun updateProfilePhoto(
        @Part("id") id: RequestBody,
        @Part photo: MultipartBody.Part,
    ): GetEditProfileResponse

    //DeleteAccount Api
    @FormUrlEncoded
    @POST("deleteAccount")
    suspend fun deleteAccount(
        @Field("id") id: String,
    ): DeleteAccountResponse
}