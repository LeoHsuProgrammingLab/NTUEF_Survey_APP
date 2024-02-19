package com.example.ntufapp.api

import com.example.ntufapp.api.dataType.Connection
import com.example.ntufapp.api.dataType.TokenRequest
import com.example.ntufapp.api.dataType.TokenRequestWithBody
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

const val testToken = "NDRFQzBFQjctNjg2OS00MEE5LTg0NDctRUU2OTg2RjE3QkYz"

interface CatalogueApi {
    @Headers(
        "Content-Type:application/json",
        "ver:1.0.0",
        "api-authorization-key:3e9cae238ea34699aa2d56d6e3d29512",
        "api-os-type:android",
        "app-client-type:1"
    )

    @POST("/NTU_Forest_API/ForestSampleCatalogue") // Should be a GET request, but the API is designed by the other company SMH
    suspend fun getCatalogue(@Body requestBody: RequestBody): Response<ResponseBody>
}

interface PlotApi {
    @Headers(
        "Content-Type:application/json",
        "ver:1.0.0",
        "api-authorization-key:63fc17cb0b1144a9aa9f4cf9304dc565",
        "api-os-type:android",
        "app-client-type:1"
    )

    @POST("/NTU_Forest_API/ForestSampleInfo") // Should be a GET request, but the API is designed by the other company SMH
    suspend fun getPlotInfo(@Body requestBody: TokenRequestWithBody): Response<ResponseBody> // TODO
}

interface SystemCodeApi {
    @Headers(
        "Content-Type:application/json",
        "ver:1.0.0",
        "api-authorization-key:1eebc8e027e1448e86da782e028e1dcb",
        "api-os-type:android",
        "app-client-type:1"
    )

    @POST("/NTU_Forest_API/ForestSystemItemCodeList") // Should be a GET request, but the API is designed by the other company SMH
    suspend fun getUserAndConditionCodeList(@Body requestBody: RequestBody): Response<ResponseBody>
}

interface UploadPlotApi {
    @Headers(
        "Content-Type:application/json",
        "ver:1.0.0",
        "api-authorization-key:878bead909ee470f84dab6909556617d",
        "api-os-type:android",
        "app-client-type:1"
    )

    @POST("/NTU_Forest_API/CreateForestLocationInvestigationRecord")
    suspend fun createInvestigationRecord(@Body surveyData: Any): Response<ResponseBody> // TODO
}