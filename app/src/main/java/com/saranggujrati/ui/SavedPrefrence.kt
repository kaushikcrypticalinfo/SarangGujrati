package com.saranggujrati.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.saranggujrati.model.CardDataMain
import com.google.gson.Gson
import com.saranggujrati.adapter.NewsOnTheGoAdapter

object SavedPrefrence {

    const val EMAIL = "email"
    const val USERID = "id"
    const val TYPE = "type"
    const val LOGIN_FROM = "login_from"
    const val USERNAME = "name"
    const val PHOTO = "photo"
    const val PHONE = "phone"
    const val GENDER = "gender"
    const val FB_TOKEN = "fb_token"
    const val GOOGLE_TOKEN = "google_token"
    const val DEVICE_TOKEN = "device_token"
    const val LANG_CODE = "lang_code"
    const val DELETED_AT = "deleted_at"
    const val CREATED_AT = "created_at"
    const val UPDATED_AT = "updated_at"
    const val API_TOKEN = "api_token"
    const val OTP = "otp"
    const val USER = "user"
    const val ADS_CARD = "ads_card"
    const val is_guest = "is_guest"
    const val IS_DARK_MODE = "is_dark_mode"

    var is_LOGIN: Boolean = false
    var is_DARKMODE: Boolean = false
    var is_Guest: Boolean = false


    fun getIsGuest(context: Context) = getSharedPreference(
        context
    )?.getBoolean(is_guest, false)

    fun setGuest(context: Context, type: Boolean) {
        editor(context, is_guest, type)
    }

    fun getIsDarkMode(context: Context) = getSharedPreference(
        context
    )?.getBoolean(IS_DARK_MODE, false)

    fun setIsDarkMode(context: Context, type: Boolean) {
        editor(context, IS_DARK_MODE, type)
    }


    private fun getSharedPreference(ctx: Context?): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(ctx!!)
    }

    private fun editor(context: Context, const: String, string: String) {
        getSharedPreference(
            context
        )?.edit()?.putString(const, string)?.apply()
    }

    private fun editor(context: Context, const: String, string: Boolean) {
        getSharedPreference(
            context
        )?.edit()?.putBoolean(const, string)?.apply()
    }

    fun clearPrefrence(context: Context?) {
        val editor = getSharedPreference(context)?.edit()
        editor?.clear()
        editor?.apply()
    }


    fun getEmail(context: Context) = getSharedPreference(
        context
    )?.getString(EMAIL, "")

    fun setEmail(context: Context, email: String) {
        editor(
            context,
            EMAIL,
            email
        )
    }

    fun getUserId(context: Context) = getSharedPreference(
        context
    )?.getString(USERID, "")

    fun setUserId(context: Context, id: String) {
        editor(
            context,
            USERID,
            id
        )
    }

    fun getType(context: Context) = getSharedPreference(
        context
    )?.getString(TYPE, "")

    fun setType(context: Context, type: String) {
        editor(
            context,
            TYPE,
            type
        )
    }

    fun getLoginFrom(context: Context) = getSharedPreference(
        context
    )?.getString(LOGIN_FROM, "")

    fun setLoginFrom(context: Context, loginFrom: String) {
        editor(
            context,
            LOGIN_FROM,
            loginFrom
        )
    }

    fun getUserName(context: Context) = getSharedPreference(
        context
    )?.getString(USERNAME, "")

    fun setUserName(context: Context, name: String) {
        editor(
            context,
            USERNAME,
            name
        )
    }

    fun getPhoto(context: Context) = getSharedPreference(
        context
    )?.getString(PHOTO, "")

    fun setPhoto(context: Context, photo: String) {
        editor(
            context,
            PHOTO,
            photo
        )
    }

    fun getPhone(context: Context) = getSharedPreference(
        context
    )?.getString(PHONE, "")

    fun setPhone(context: Context, phone: String) {
        editor(
            context,
            PHONE,
            phone
        )
    }

    fun getGender(context: Context) = getSharedPreference(
        context
    )?.getString(GENDER, "")

    fun setGender(context: Context, gender: String) {
        editor(
            context,
            GENDER,
            gender
        )
    }

    fun getFbToken(context: Context) = getSharedPreference(
        context
    )?.getString(FB_TOKEN, "")

    fun setFbToken(context: Context, fbToken: String) {
        editor(
            context,
            FB_TOKEN,
            fbToken
        )
    }

    fun getGoogleToken(context: Context) = getSharedPreference(
        context
    )?.getString(GOOGLE_TOKEN, "")

    fun setGoogleToken(context: Context, googleToken: String) {
        editor(
            context,
            GOOGLE_TOKEN,
            googleToken
        )
    }

    fun getDeviceToken(context: Context) = getSharedPreference(
        context
    )?.getString(DEVICE_TOKEN, "")

    fun setDeviceToken(context: Context, deviceToken: String) {
        editor(
            context,
            DEVICE_TOKEN,
            deviceToken
        )
    }

    fun getDeletedAt(context: Context) = getSharedPreference(
        context
    )?.getString(DELETED_AT, "")

    fun setDeletedAt(context: Context, deletedAt: String) {
        editor(
            context,
            DELETED_AT,
            deletedAt
        )
    }

    fun getCreatedAt(context: Context) = getSharedPreference(
        context
    )?.getString(CREATED_AT, "")

    fun setCreatedAt(context: Context, CreatedAt: String) {
        editor(
            context,
            CREATED_AT,
            CreatedAt
        )
    }

    fun getUpdatedAt(context: Context) = getSharedPreference(
        context
    )?.getString(UPDATED_AT, "")

    fun setUpdatedAt(context: Context, updatedAt: String) {
        editor(
            context,
            UPDATED_AT,
            updatedAt
        )
    }

    fun getLangCode(context: Context) = getSharedPreference(
        context
    )?.getString(LANG_CODE, "")

    fun setLangCode(context: Context, languageCode: String) {
        editor(
            context,
            LANG_CODE,
            languageCode
        )
    }

    fun getApiToken(context: Context) = getSharedPreference(
        context
    )?.getString(API_TOKEN, "")

    fun setApiToken(context: Context, apiToken: String) {
        editor(
            context,
            API_TOKEN,
            apiToken
        )
    }

    fun getOtp(context: Context) = getSharedPreference(
        context
    )?.getString(OTP, "")

    fun setOtp(context: Context, otp: String) {
        editor(
            context,
            OTP,
            otp
        )
    }

    fun setUser(user: Any, context: Context?) {
        is_LOGIN = true
        val editor = getSharedPreference(context)?.edit()
        val gson: Gson = Gson()
        val userData = gson.toJson(user)
        editor?.putString(USER, userData)
        editor?.apply()

    }

    fun getUser(context: Context?): Any {
        val preferences = getSharedPreference(context)
        val gson = Gson()
        val json: String = preferences?.getString(USER, "").toString()
        val obj: Any = gson.fromJson(json, Any::class.java)
        return obj
    }

    fun setAdsCard(user: Any, context: Context?) {
        val editor = getSharedPreference(context)?.edit()
        val gson = Gson()
        val userData = gson.toJson(user)
        editor?.putString(ADS_CARD, userData)
        editor?.apply()
    }

    fun getAdsCard(context: Context?): CardDataMain? {
        val preferences = getSharedPreference(context)
        val gson = Gson()
        val json: String = preferences?.getString(ADS_CARD, "").toString()
        val obj = gson.fromJson(json, CardDataMain::class.java)
        return obj
    }

}