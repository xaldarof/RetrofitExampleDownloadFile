package com.example.advansedretrofit

import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitService {

    @GET("Advanced-Accessibility-Tests-Media-Overlays-v1.0.0.epub")
    suspend fun downloadFile(): ResponseBody

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