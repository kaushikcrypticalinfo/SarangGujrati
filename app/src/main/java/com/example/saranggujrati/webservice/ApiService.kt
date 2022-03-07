package com.example.saranggujrati.webservice

import com.example.saranggujrati.model.*
import com.example.saranggujrati.model.onDemand.OnDemandDataMain
import com.example.saranggujrati.model.onDemand.OnDemandRes
import com.example.saranggujrati.model.rssFeed.RssFeed
import com.test.pausernew.api.annot.Json
import com.test.pausernew.api.annot.Xml
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    //login Api
    @FormUrlEncoded
    @POST("login")
    @Json
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    //SignUp Api
    @FormUrlEncoded
    @POST("register")
    @Json
    suspend fun signUp(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("name") name: String,
    ): SignUpResponse

    //Forgot Password Api
    @FormUrlEncoded
    @POST("forgot-password")
    @Json
    suspend fun forgotPassword(
        @Field("email") email: String,
    ): ForgotPasswordResponse

    //Reset Password Api
    @FormUrlEncoded
    @POST("reset-password")
    @Json
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
    @Json
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
    @Json
    suspend fun getTopCitiesCategories(): CitCategoryListResponse

    //city & category detail
    @GET("blog-category-list/{id}")
    @Json
    suspend fun getCitiesCategoriesDetailBlog(@Path("id") id: String): CityCategoryBlogDetailResponse

    //Featured List
    @GET("blog-list")
    @Json
    suspend fun getFeatureList(): BlogFeatureList

    @GET("on-demand-list")
    @Json
    suspend fun getOnDemandList(): OnDemandRes

    //news channel list
    @GET("live-news-list")
    @Json
    suspend fun getLiveNewsChannelList(@Query("page") page: String): NewsChannelListRespnse

    @GET("live-news-list")
    @Json
    suspend fun getLiveChannelWithPaging(@Query("page") page: String): NewsChannelListRespnse


    //news paper list
    @GET("news-paper-list")
    @Json
    suspend fun getNewsPaperlList(@Query("page") page: String): NewsPaperListResponse


    //All Blog Data
    @GET("blog-all-list")
    @Json
    suspend fun getAllBlogList(): AllBlogListResponse


    //GetProfile Api
    @FormUrlEncoded
    @POST("getProfile")
    @Json
    suspend fun getProfile(
        @Field("id") id: String,
    ): GetEditProfileResponse

    //EditProfile Api
    @FormUrlEncoded
    @POST("updateProfile")
    @Json
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
    @Json
    suspend fun editProfileWithoutPassword(
        @Field("id") id: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("name") name: String,
    ): GetEditProfileResponse

    //EditProfilePicture Api
    @Multipart
    @POST("updateProfilePicture")
    @Json
    suspend fun updateProfilePhoto(
        @Part("id") id: RequestBody,
        @Part photo: MultipartBody.Part,
    ): GetEditProfileResponse

    //DeleteAccount Api
    @FormUrlEncoded
    @POST("deleteAccount")
    @Json
    suspend fun deleteAccount(
        @Field("id") id: String,
    ): DeleteAccountResponse


    @GET("rss-feed-list/{id}")
    @Json
    suspend fun getRssfeedList(
        @Path("id") id: String,
    ): FeedResponse

    @GET("full-screen-card-list")
    @Json
    suspend fun fullScreenCardList(): CardListRes


    @GET("{fullUrl}")
    @Xml
    suspend fun getFeedLiveData(
        @Path("fullUrl", encoded = true) fullUrl: String
    ): RssFeed

}