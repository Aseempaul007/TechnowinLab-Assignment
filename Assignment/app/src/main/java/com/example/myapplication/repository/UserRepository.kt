package com.example.myapplication.repository

import com.example.myapplication.data.remote.apiinterface.UserApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Part
import javax.inject.Inject

class UserRepository @Inject constructor(val api: UserApi) {

    fun createUser(
        apiKey: String,
        bearerToken: String,
        name: RequestBody,
        about: RequestBody,
        profile: MultipartBody.Part
    ): Call<ResponseBody> {
        return api.createUser(apiKey, bearerToken, name, about, profile)
    }
}