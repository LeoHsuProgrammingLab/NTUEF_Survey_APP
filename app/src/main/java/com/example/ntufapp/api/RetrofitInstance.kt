package com.example.ntufapp.api

import com.example.ntufapp.BuildConfig
import com.example.ntufapp.api.dataType.plotInfoResponse.PlotInfoResponse
import com.example.ntufapp.api.dataType.plotsCatalogueResponse.PlotsCatalogueResponse
import com.example.ntufapp.api.dataType.surveyDataForUpload.SurveyDataForUpload
import com.example.ntufapp.api.dataType.uploadDataResponse.uploadDataResponse
import com.example.ntufapp.api.dataType.userAndConditionCodeResponse.UserAndConditionCodeResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = BuildConfig.BASE_URL

    private val retrofit by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val okClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okClient)
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

fun catalogueApi(coroutineScope: CoroutineScope, callback: (PlotsCatalogueResponse?, String) -> Unit) {
    coroutineScope.launch {
        val requestBody = createRequestBody(listOf("token" to BuildConfig.API_PARAMS_TOKEN))

        try {
            val response = RetrofitInstance.catalogueApiService.getCatalogue(requestBody)
            if (response.isSuccessful) {
                val gson = Gson()
                val catalogueRsp = gson.fromJson(response.body()?.string(), PlotsCatalogueResponse::class.java)
                callback(catalogueRsp, "")
            } else {
                callback(null, "資料庫回應錯誤")
            }
        } catch (e: IOException) {
            callback(null, "網路連線錯誤")
            return@launch
        } catch (e: HttpException) {
            callback(null, "網路協定錯誤")
            return@launch
        }
    }
}

suspend fun plotApi(coroutineScope: CoroutineScope, locationMid: String): PlotInfoResponse? {
    return coroutineScope.async {
        val requestBody = createRequestBody(listOf("token" to BuildConfig.API_PARAMS_TOKEN, "location_mid" to locationMid))

        try {
            val response = withContext(Dispatchers.Default) {
                RetrofitInstance.plotApiService.getPlotInfo(requestBody)
            }
            val responseBody = response.body()?.string()

            if (response.isSuccessful) {
                val gson = Gson()
                val plotInfoRsp = gson.fromJson(responseBody, PlotInfoResponse::class.java)
                plotInfoRsp.location_mid = locationMid
                return@async plotInfoRsp
            } else {
                return@async null
            }
        } catch (e: SocketTimeoutException) {
            return@async null
        } catch (e: IOException) {
            return@async null
        } catch (e: HttpException) {
            return@async null
        }
    }.await()
}

suspend fun userAndConditionCodeApi(coroutineScope: CoroutineScope): UserAndConditionCodeResponse? {
    return coroutineScope.async {
        val requestBody = createRequestBody(listOf("token" to BuildConfig.API_PARAMS_TOKEN))
        try {
            val response = withContext(Dispatchers.Default) {
                RetrofitInstance.systemCodeApiService.getUserAndConditionCodeList(requestBody)
            }
            val responseBody = response.body()?.string()

            if (response.isSuccessful) {
                val gson = Gson()
                return@async gson.fromJson<UserAndConditionCodeResponse?>(
                    responseBody,
                    UserAndConditionCodeResponse::class.java
                )
            } else {
                return@async null
            }
        } catch (e: IOException) {
            return@async null
        } catch (e: HttpException) {
            return@async null
        }
    }.await()
}

suspend fun uploadPlotDataApi(coroutineScope: CoroutineScope, surveyData: SurveyDataForUpload): uploadDataResponse? {
    return coroutineScope.async {
        surveyData.token = BuildConfig.API_PARAMS_TOKEN
        val gson = Gson()
        val surveyDataJson = gson.toJson(surveyData)
        val requestBody = surveyDataJson.toRequestBody("application/json".toMediaTypeOrNull())

        try {
            val response = withContext(Dispatchers.Default) {
                RetrofitInstance.uploadPlotDataApiService.createInvestigationRecord(requestBody)
            }
            val responseBody = response.body()?.string()

            if (response.isSuccessful) {
                val uploadDataRsp = gson.fromJson(responseBody, uploadDataResponse::class.java)
                return@async uploadDataRsp
            } else {
                return@async null
            }
        } catch (e: IOException) {
            return@async null
        } catch (e: HttpException) {
            return@async null
        }
    }.await()
}