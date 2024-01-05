package com.example.jbmatrixassignment.viewmodel

import androidx.lifecycle.ViewModel
import com.example.jbmatrixassignment.data.remote.model.VideoResponse
import com.example.jbmatrixassignment.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class VideoViewmodel @Inject constructor(private val repository: VideoRepository): ViewModel() {

    var pos: Int = 1
    var tab: Int = 10

    fun getVideos(tabGroupId: Int): Call<VideoResponse?>? {
        return repository.getVideos(tabGroupId)
    }
}