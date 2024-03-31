package com.example.ntufapp.api

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

fun createRequestBody(jsonContent: List<Pair<String, String>>): RequestBody {
    val json = JSONObject().apply {
        jsonContent.forEach {
            put(it.first, it.second)
        }
    }
    val jsonString = json.toString()
    return jsonString.toRequestBody("application/json".toMediaTypeOrNull())
}