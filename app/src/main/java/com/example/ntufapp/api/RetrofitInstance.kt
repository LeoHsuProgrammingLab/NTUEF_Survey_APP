package com.example.ntufapp.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    val uploadPlotApiService: UploadPlotApi by lazy {
        retrofit.create(UploadPlotApi::class.java)
    }
}