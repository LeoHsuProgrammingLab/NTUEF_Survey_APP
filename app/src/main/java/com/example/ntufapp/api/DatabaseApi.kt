package com.example.ntufapp.api

import com.example.ntufapp.api.dataType.Connection
import com.example.ntufapp.api.dataType.TokenRequest
import com.example.ntufapp.api.dataType.TokenRequestWithBody
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

    @POST("/ForestSampleCatalogue") // Should be a GET request, but the API is designed by the other company SMH
    fun getCatalogue(@Body requestBody: TokenRequest = TokenRequest(testToken)): Response<Connection>
}

interface PlotApi {
    @Headers(
        "Content-Type:application/json",
        "ver:1.0.0",
        "api-authorization-key:63fc17cb0b1144a9aa9f4cf9304dc565",
        "api-os-type:android",
        "app-client-type:1"
    )

    @POST("/ForestSampleInfo") // Should be a GET request, but the API is designed by the other company SMH
    fun getPlotInfo(@Body requestBody: TokenRequestWithBody): Response<Connection>
}

interface SystemCodeApi {
    @Headers(
        "Content-Type:application/json",
        "ver:1.0.0",
        "api-authorization-key:1eebc8e027e1448e86da782e028e1dcb",
        "api-os-type:android",
        "app-client-type:1"
    )

    @POST("/ForestSystemItemCodeList") // Should be a GET request, but the API is designed by the other company SMH
    fun getUserAndConditionCodeList(@Body requestBody: TokenRequest = TokenRequest(testToken)): Response<Connection>
}

interface UploadPlotApi {
    @Headers(
        "Content-Type:application/json",
        "ver:1.0.0",
        "api-authorization-key:878bead909ee470f84dab6909556617d",
        "api-os-type:android",
        "app-client-type:1"
    )

    @POST("/CreateForestLocationInvestigationRecord")
    fun createInvestigationRecord(@Body surveyData: Any): Response<Connection>
}