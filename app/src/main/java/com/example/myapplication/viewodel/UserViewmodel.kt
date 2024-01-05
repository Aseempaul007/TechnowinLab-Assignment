package com.example.myapplication.viewodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserViewmodel @Inject constructor(val repository: UserRepository): ViewModel(){

    fun createUser(
        apiKey: String,
        bearerToken: String,
        name: RequestBody,
        about: RequestBody,
        profile: MultipartBody.Part
    ): Call<ResponseBody> {
        return repository.createUser(apiKey, bearerToken, name, about, profile)
    }
}