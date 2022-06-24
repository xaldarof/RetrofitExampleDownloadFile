package com.example.advansedretrofit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = RetrofitProvider.getInstance().create(RetrofitService::class.java)
        retrofit.downloadBook().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { writeResponseBodyToDisk(it) }
                } else {
                    Log.d("res", "Error")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun calulateProgress(totalSize: Double, downloadSize: Double): Double {
        return ((downloadSize / totalSize) * 100)
    }

    fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        val filename = "book.epub"
        val apkFile =
            File(this.filesDir,
                filename)

        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            val fileReader = ByteArray(4096)
            val fileSize = body.contentLength()
            var fileSizeDownloaded: Long = 0
            inputStream = body.byteStream()
            outputStream = FileOutputStream(apkFile)
            while (true) {
                val read = inputStream.read(fileReader)
                if (read == -1) {
                    break
                }
                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read.toLong()

                calulateProgress(fileSize.toDouble(), fileSizeDownloaded.toDouble())
                println("file downloading $fileSizeDownloaded of $fileSize")
                outputStream.flush()

                return true
            }
        } catch (e: Exception) {
            println(e.toString())
            return false
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
        return true
    }
}