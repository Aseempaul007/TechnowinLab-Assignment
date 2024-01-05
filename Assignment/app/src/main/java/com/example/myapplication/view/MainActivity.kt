package com.example.myapplication.view

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.viewodel.UserViewmodel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var viewmodel: UserViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewmodel = ViewModelProvider(this@MainActivity).get(UserViewmodel::class.java)

        val apiKey = "lkcMuYllSgc3jsFi1gg896mtbPxIBzYkEL"
        val bearerToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIzIiwianRpIjoiYTA5ZmUxODZkYjgzYWJmNDU0YmMyNzhjNDJhZDliZWM5Njk1OTVjM2Q4YmVlZTI1ZTFjMTlhMjA5MmVmMWFmMGI3OWI5MTEzYTBmODlkMjQiLCJpYXQiOjE3MDQ0MzU0Nj"

        val name = "Test "
        val about = "Test About "

        val drawableUri: Uri = Uri.parse("android.resource://com.example.myapplication.view/R.drawable.image")

        val imageFile = uriToFile(drawableUri)

        val requestFile = imageFile?.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val profilePart = MultipartBody.Part.createFormData("profile", imageFile!!.name, requestFile!!)

        val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val aboutRequestBody = about.toRequestBody("text/plain".toMediaTypeOrNull())

        val call = viewmodel.createUser(apiKey, bearerToken, nameRequestBody, aboutRequestBody, profilePart)


        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    Toast.makeText(this@MainActivity, responseBody, Toast.LENGTH_SHORT).show()
                    Log.d("MYTAG", responseBody.toString())
                } else {
                    Toast.makeText(this@MainActivity, "User not created", Toast.LENGTH_SHORT).show()
                    Log.d("MYTAG", "User not created")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
                Log.d("MYTAG", t.toString())

            }
        })

    }

    private fun getDrawableUri(drawableResourceId: Int): Uri {
        return Uri.parse("android.resource://${packageName}/${drawableResourceId}")
    }

    private fun uriToFile(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()

        val columnIndex: Int? = cursor?.getColumnIndex(filePathColumn[0])
        val filePath: String? = columnIndex?.let { cursor.getString(it) }
        cursor?.close()

        return filePath?.let { File(it) }
    }
}