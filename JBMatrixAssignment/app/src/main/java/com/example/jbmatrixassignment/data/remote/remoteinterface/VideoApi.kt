package com.example.jbmatrixassignment.data.remote.remoteinterface

import com.example.jbmatrixassignment.data.remote.model.VideoResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


interface VideoApi {
    @Headers(
        "Keydata: 435643653467655"
    )
    @FormUrlEncoded
    @POST("fetch_video_tab_group_wise")
    fun getVideos(
        @Field("tab_group_id") tabGroupId: Int?
    ): Call<VideoResponse?>?

//    @FormUrlEncoded
//    @POST("fetch_video_tab_group_wise")
//    fun getVideos(
//        @Header("Keydata") keydata: String?,
//        @Field("tab_group_id") tabGroupId: String?
//    ): Call<VideoResponse?>?

}