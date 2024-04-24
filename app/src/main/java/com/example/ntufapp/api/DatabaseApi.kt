package com.example.ntufapp.api

import com.example.ntufapp.BuildConfig
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CatalogueApi {
    @Headers(
        "Content-Type:application/json",
        "ver:1.0.0",
        "api-authorization-key:" + BuildConfig.CATALOGUE_API_KEY,
        "api-os-type:android",
        "app-client-type:1"
    )

    @POST("/ForestSampleCatalogue") // Should be a GET request, but the API is designed by the other company SMH
    suspend fun getCatalogue(@Body requestBody: RequestBody): Response<ResponseBody>
}

interface PlotApi {
    @Headers(
        "Content-Type:application/json",
        "ver:1.0.0",
        "api-authorization-key:" + BuildConfig.PLOT_API_KEY,
        "api-os-type:android",
        "app-client-type:1"
    )

    @POST("/ForestSampleInfo") // Should be a GET request, but the API is designed by the other company SMH
    suspend fun getPlotInfo(@Body requestBody: RequestBody): Response<ResponseBody>
    // requestBody: {"token":"NDRFQzBFQjctNjg2OS00MEE5LTg0NDctRUU2OTg2RjE3QkYz", "location_mid":"99FD5CB8-6EFE-4242-9E3F-D78BEB04283D"} // 西川柳杉生長量試驗
}

interface SystemCodeApi {
    @Headers(
        "Content-Type:application/json",
        "ver:1.0.0",
        "api-authorization-key:" + BuildConfig.CODE_API_KEY,
        "api-os-type:android",
        "app-client-type:1"
    )

    @POST("/ForestSystemItemCodeList") // Should be a GET request, but the API is designed by the other company SMH
    suspend fun getUserAndConditionCodeList(@Body requestBody: RequestBody): Response<ResponseBody>
    // requestBody: {"token":"NDRFQzBFQjctNjg2OS00MEE5LTg0NDctRUU2OTg2RjE3QkYz"}
}

interface UploadPlotApi {
    @Headers(
        "Content-Type:application/json",
        "ver:1.0.0",
        "api-authorization-key:" + BuildConfig.CREATE_API_KEY,
        "api-os-type:android",
        "app-client-type:1"
    )

    @POST("/CreateForestLocationInvestigationRecord")
    suspend fun createInvestigationRecord(@Body surveyData: RequestBody): Response<ResponseBody>
}