package com.example.jbmatrixassignment.ui.activity

import android.app.DownloadManager
import android.app.Service
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.jbmatrixassignment.data.remote.model.Data
import com.example.jbmatrixassignment.data.remote.model.VideoResponse
import com.example.jbmatrixassignment.databinding.ActivityMainBinding
import com.example.jbmatrixassignment.viewmodel.VideoViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var viewModel: VideoViewmodel? = null
    private var videoView: VideoView? = null
    private var mediaController: MediaController? = null
    var downloadManager: DownloadManager? = null
    var data: List<Data>? = null
    val MY_CAMERA_PERMISSION_CODE = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(VideoViewmodel::class.java)
        videoView = binding.videoView
        mediaController = MediaController(this@MainActivity)
        mediaController?.setAnchorView(videoView)

        var pos = viewModel?.pos
        var tab = viewModel?.tab

        binding.progressBar?.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            getAdvertisement(tab!!, pos!!)
        }

        videoView?.setOnCompletionListener {
            if (pos!! < data!!.size - 1) {
                pos = pos!! + 1
                playVideoByPosition(pos!!)
            } else {
                tab = tab!! + 1
                pos = 0
                CoroutineScope(Dispatchers.IO).launch {
                    getAdvertisement(tab!!, pos!!)
                }
            }
            true
        }

        videoView?.setOnErrorListener { mp, what, extra ->
            Toast.makeText(this, extra.toString(), Toast.LENGTH_SHORT).show()
            false
        }

        mediaController?.setPrevNextListeners({
            binding.progressBar?.visibility = View.VISIBLE
            if (pos!! < data!!.size - 1) {
                pos = pos!! + 1
                playVideoByPosition(pos!!)
            } else {
                tab = tab!! + 1
                pos = 0
                CoroutineScope(Dispatchers.IO).launch {
                    getAdvertisement(tab!!, pos!!)
                }
            }
        }) {
            binding.progressBar?.visibility = View.VISIBLE
            if (pos!! > 0) {
                pos = pos!! - 1
                playVideoByPosition(pos!!)
            }
        }

        videoView?.setMediaController(mediaController)

        binding.download?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_CAMERA_PERMISSION_CODE
                )
            } else {
                downloadFileFromUrl(pos!!)
            }

        }
    }

    private fun downloadFile(uri: Uri, fileStorageDestinationUri: String, fileName: String): Long {
        var downloadReference: Long = 0
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        try {
            val request = DownloadManager.Request(uri)
            request.setTitle(fileName)
            request.setDescription("Your file is downloading")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(fileStorageDestinationUri, fileName)
            downloadReference = downloadManager!!.enqueue(request)
        } catch (e: IllegalArgumentException) {
            Toast.makeText(
                this,
                "Download link is broken or not availale for download",
                Toast.LENGTH_SHORT
            ).show()
        }
        return downloadReference
    }

    private fun downloadFileFromUrl(pos: Int) {
        var videoBaseUrl: String = "https://app.screenzy.in/screenzyapp/"
        videoBaseUrl += data?.get(pos)?.video.toString()

        val downloadFileRef = downloadFile(
            Uri.parse(videoBaseUrl),
            Environment.DIRECTORY_DOWNLOADS,
            "fileName.mp4"
        )
        if (downloadFileRef != 0L) {
            Toast.makeText(this, "Starting download...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "File is not available for download", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                var pos = viewModel?.pos
                downloadFileFromUrl(pos!!)
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun getAdvertisement(tab: Int, pos: Int) {
        val res = viewModel?.getVideos(tab)
        withContext(Dispatchers.Main) {
            res?.enqueue(object : Callback<VideoResponse?> {
                override fun onResponse(
                    call: Call<VideoResponse?>,
                    response: Response<VideoResponse?>
                ) {
                    binding.progressBar?.visibility = View.INVISIBLE
                    Log.d("MYTAG", "Success" + response.body().toString())
                    data = response.body()?.data
                    playVideoByPosition(pos)
                }

                override fun onFailure(call: Call<VideoResponse?>, t: Throwable) {
                    binding.progressBar?.visibility = View.INVISIBLE
                    Log.d("MYTAG", "Failed" + t.toString())
                }
            })
        }
    }

    fun playVideoByPosition(position: Int) {
        var videoBaseUrl: String = "https://app.screenzy.in/screenzyapp/"
        videoBaseUrl += data?.get(position)?.video.toString()

        videoView?.setVideoURI(
            Uri.parse(videoBaseUrl)
        )
        binding.progressBar?.visibility = View.INVISIBLE
        binding.companyName?.text = data?.get(position)?.company_name.toString()
        binding.title?.text = data?.get(position)?.title.toString()
        videoView?.requestFocus()
        videoView?.start()
    }

}