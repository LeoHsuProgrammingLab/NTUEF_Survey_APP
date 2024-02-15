package com.example.ntufapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://211.79.114.51:8888/NTU_Forest_API/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
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