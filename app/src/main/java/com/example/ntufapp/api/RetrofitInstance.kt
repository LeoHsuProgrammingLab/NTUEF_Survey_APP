package com.example.ntufapp.api

import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitInstance {
    private const val BASE_URL = "http://211.79.114.51:8888/"
    private const val BASE_URL2 = "http://formis.exfo.ntu.edu.tw:8889/"

    private val retrofit by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val catalogueApiService: CatalogueApi by lazy {
        retrofit.create(CatalogueApi::class.java)
    }

    val plotApiService: PlotApi by lazy {
        retrofit.create(PlotApi::class.java)
    }

    val systemCodeApiService: SystemCodeApi by lazy {
        retrofit.create(SystemCodeApi::class.java)
    }

    val uploadPlotDataApiService: UploadPlotApi by lazy {
        retrofit.create(UploadPlotApi::class.java)
    }
}

fun catalogueApi(coroutineScope: CoroutineScope, tag: String) {
    coroutineScope.launch {
        val json = "{\"token\":\"NDRFQzBFQjctNjg2OS00MEE5LTg0NDctRUU2OTg2RjE3QkYz\"}"
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        val response = try {
            RetrofitInstance.catalogueApiService.getCatalogue(requestBody)
        } catch (e: IOException) {
            Log.e(tag, "IOException: ${e.message}, you might not have internet connection, you gotta check it")
            return@launch
        } catch (e: HttpException) {
            Log.e(tag, "HttpException: ${e.message}, unexpected http response")
            return@launch
        }

        if (response.isSuccessful) {
            Log.d(tag, "response: ${response.body()?.string()}")
        } else {
            Log.d(tag, "response: ${response}")
        }
    }
}

fun plotApi(coroutineScope: CoroutineScope, tag: String) {
    coroutineScope.launch {
        val json = "{\"token\":\"NDRFQzBFQjctNjg2OS00MEE5LTg0NDctRUU2OTg2RjE3QkYz\"}"
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        val response = try {
            RetrofitInstance.catalogueApiService.getCatalogue(requestBody)
        } catch (e: IOException) {
            Log.e(tag, "IOException: ${e.message}, you might not have internet connection, you gotta check it")
            return@launch
        } catch (e: HttpException) {
            Log.e(tag, "HttpException: ${e.message}, unexpected http response")
            return@launch
        }

        if (response.isSuccessful) {
            Log.d(tag, "response: ${response.body()?.string()}")
        } else {
            Log.d(tag, "response: ${response}")
        }
    }
}

fun systemCodeApi(coroutineScope: CoroutineScope, tag: String) {
    coroutineScope.launch {
        val json = "{\"token\":\"NDRFQzBFQjctNjg2OS00MEE5LTg0NDctRUU2OTg2RjE3QkYz\"}"
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        val response = try {
            RetrofitInstance.systemCodeApiService.getUserAndConditionCodeList(requestBody)
        } catch (e: IOException) {
            Log.e(tag, "IOException: ${e.message}, you might not have internet connection, you gotta check it")
            return@launch
        } catch (e: HttpException) {
            Log.e(tag, "HttpException: ${e.message}, unexpected http response")
            return@launch
        }

        if (response.isSuccessful) {
            Log.d(tag, "response: ${response.body()?.string()}")
        } else {
            Log.d(tag, "response: ${response}")
        }
    }
}

fun uploadPlotDataApi() {

}