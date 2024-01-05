package com.example.myapplication.data.remote.apiinterface

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserApi {

    @POST("/job_app/api/job_seeker_details")
    @Multipart
    fun createUser(
        @Header("X-API-Key") apiKey: String,
        @Header("Authorization") bearerToken: String,
        @Part("name") name: RequestBody,
        @Part("about") about: RequestBody,
        @Part profile: MultipartBody.Part
    ): Call<ResponseBody>
}