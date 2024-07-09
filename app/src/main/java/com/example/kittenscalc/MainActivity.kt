package com.example.kittenscalc

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kittenscalc.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    private suspend fun fetchAppState(): String {
        return withContext(Dispatchers.IO) {
            val url = "https://reqres.in/api/users/2"
            val request = Request.Builder().url(url).build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) response.body?.string() ?: ""
                else throw IOException("unexpected code ${response.code}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_main)
        val activityMain = ActivityMainBinding.inflate(layoutInflater)

        lifecycleScope.launch(Dispatchers.Main) {
            val response = fetchAppState()
            activityMain.tvResponseText.text = response
            setContentView(activityMain.root)
        }
        Log.d("MainActivity", "onCreate completed")
    }
}