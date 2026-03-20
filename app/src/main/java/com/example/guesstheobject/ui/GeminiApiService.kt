package com.example.guesstheobject.ui

import android.graphics.Bitmap
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

object GeminiApiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private const val API_KEY = "AIzaSyBt3-aXYipijP7iBqqGv2n52feoUNbrbqA"

    private const val API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$API_KEY"

    suspend fun guessDrawing(bitmap: Bitmap): String {
        return withContext(Dispatchers.IO) {
            try {
                // Convert bitmap to base64
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
                val base64Image = Base64.encodeToString(
                    outputStream.toByteArray(),
                    Base64.NO_WRAP
                )

                // Build request body
                val requestBody = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", "This is a child's drawing. In one short fun sentence, what object do you think this is a drawing of? Start with 'I think this is a drawing of...' Keep it simple and encouraging for kids.")
                                })
                                put(JSONObject().apply {
                                    put("inline_data", JSONObject().apply {
                                        put("mime_type", "image/png")
                                        put("data", base64Image)
                                    })
                                })
                            })
                        })
                    })
                }.toString()

                val request = Request.Builder()
                    .url(API_URL)
                    .post(requestBody.toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                android.util.Log.d("GEMINI", "Response code: ${response.code}, body: $responseBody")

                if (response.isSuccessful && responseBody != null) {
                    val json = JSONObject(responseBody)
                    json.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                        .trim()
                } else {
                    "Hmm, I am not sure what that is — great drawing though!"
                }
            }  catch (e: Exception) {
                android.util.Log.e("GEMINI", "Error: ${e.message}", e)
                "Hmm, I am not sure what that is — great drawing though!"
            }
        }
    }
}
