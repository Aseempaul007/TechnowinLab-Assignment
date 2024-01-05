package com.example.jbmatrixassignment.repository

import com.example.jbmatrixassignment.data.remote.model.VideoResponse
import com.example.jbmatrixassignment.data.remote.remoteinterface.VideoApi
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.Header
import javax.inject.Inject

class VideoRepository @Inject constructor(val api: VideoApi) {
    fun getVideos(
        tabGroupId: Int,
    ): Call<VideoResponse?>? {
        return api.getVideos(tabGroupId)
    }
}