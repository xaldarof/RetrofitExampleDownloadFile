package com.example.advansedretrofit

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = RetrofitProvider.getInstance().create(RetrofitService::class.java)
        retrofit.downloadBook().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val file = File(this@MainActivity.filesDir,"book.epub")
                    val fileOutputStream = FileOutputStream(file)
                    fileOutputStream.write(response.body()?.bytes())
                } else {
                    Log.d("res", "Error")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}

interface RetrofitService {
    @GET("/Advanced-Accessibility-Tests-Media-Overlays-v1.0.0.epub")
    fun downloadBook(): Call<ResponseBody>
}


object RetrofitProvider {

    fun getInstance(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://epubtest.org/books/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}