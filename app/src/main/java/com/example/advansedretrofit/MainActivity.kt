package com.example.advansedretrofit

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        CoroutineScope(Dispatchers.IO).launch {
            launch {
                downloadBook()
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            launch {
                findViewById<TextView>(androidx.core.R.id.text).text = "Success"
            }
        }
    }

    private suspend fun downloadBook(): File? {
        val retrofit = RetrofitProvider.getInstance().create(RetrofitService::class.java)

        return try {
            val requestBody = retrofit.downloadFile()
            val file = requestBody.byteStream().toContent()

            file
        } catch (e: Exception) {
            Log.d("res", "ERROR DOWNLOAD = ${e.printStackTrace()}")
            null
        }
    }




    private fun InputStream.toContent(): File {
        use {
            val file = File(filesDir, "book.epub")

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