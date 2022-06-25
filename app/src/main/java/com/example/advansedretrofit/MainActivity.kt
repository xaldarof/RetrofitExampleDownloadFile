package com.example.advansedretrofit

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        CoroutineScope(Dispatchers.IO).launch {
            downloadBook()
        }
    }

    private suspend fun downloadBook(): File? {
        val retrofit = RetrofitProvider.getInstance().create(RetrofitService::class.java)

        return try {
            val requestBody = retrofit.downloadFile()
            val file = requestBody.byteStream().toContent(this, "book")

            file
        } catch (e: Exception) {
            Log.d("res", "ERROR DOWNLOAD = ${e.printStackTrace()}")
            null
        }
    }


    private fun InputStream.toContent(
        context: Context,
        fileName: String = "bookfile",
    ): File {
        use {
            val file = File(context.filesDir, fileName)
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return file
        }
    }
}