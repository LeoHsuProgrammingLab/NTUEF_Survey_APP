package com.example.ntufapp.api

import android.util.Log
import com.example.ntufapp.BuildConfig
import com.example.ntufapp.api.dataType.plotInfoResponse.PlotInfoResponse
import com.example.ntufapp.api.dataType.plotsCatalogueResponse.PlotsCatalogueResponse
import com.example.ntufapp.api.dataType.userAndConditionCodeResponse.UserAndConditionCodeResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
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

fun catalogueApi(coroutineScope: CoroutineScope, tag: String, callback: (PlotsCatalogueResponse?, String) -> Unit) {
    coroutineScope.launch {
        val requestBody = createRequestBody(listOf("token" to BuildConfig.API_PARAMS_TOKEN))

        Log.d(tag, "requestBody: $requestBody")
        try {
            val response = RetrofitInstance.catalogueApiService.getCatalogue(requestBody)
            if (response.isSuccessful) {
                val gson = Gson()
                val catalogueRsp = gson.fromJson(response.body()?.string(), PlotsCatalogueResponse::class.java)
                callback(catalogueRsp, "")
                Log.d(tag, "response: ${catalogueRsp.body}")
            } else {
                callback(null, "資料庫回應錯誤")
                Log.d(tag, "Unsuccessful! response: $response")
            }
        } catch (e: IOException) {
            Log.d(tag, "IOException: ${e.message}, you might not have internet connection, you gotta check it")
            callback(null, "沒有網路，請檢查網路連線")
            return@launch
        } catch (e: HttpException) {
            Log.d(tag, "HttpException: ${e.message}, unexpected http response")
            callback(null, "網路協定錯誤")
            return@launch
        }
    }
}

suspend fun plotApi(coroutineScope: CoroutineScope, tag: String, locationMid: String): PlotInfoResponse? {
    return coroutineScope.async {
        val requestBody = createRequestBody(listOf("token" to BuildConfig.API_PARAMS_TOKEN, "location_mid" to locationMid))

        try {
            val response = withContext(Dispatchers.Default) {
                RetrofitInstance.plotApiService.getPlotInfo(requestBody)
            }
//            Log.d(tag, "response: $response")
            val responseBody = response.body()?.string()

            if (response.isSuccessful) {
                if (responseBody != null) {
                    val jsonRsp = JSONObject(responseBody)
//                    TODO: save the data to local storage

                }
                val gson = Gson()
                val plotInfoRsp = gson.fromJson(responseBody, PlotInfoResponse::class.java)
                Log.d(tag, "response: ${plotInfoRsp.body}")
                return@async plotInfoRsp
            } else {
                Log.d(tag, "Unsuccessful! response: $responseBody")
                return@async null
            }
        } catch (e: SocketTimeoutException) {
            Log.e(tag, "SocketTimeoutException: ${e.message}, timeout")
            return@async null
        } catch (e: IOException) {
            Log.e(tag, "IOException: ${e.message}, you might not have internet connection, you gotta check it")
            return@async null
        } catch (e: HttpException) {
            Log.e(tag, "HttpException: ${e.message}, unexpected http response")
            return@async null
        }
    }.await()
}

fun userAndConditionCodeApi(coroutineScope: CoroutineScope, tag: String) {
    coroutineScope.launch {
        val requestBody = createRequestBody(listOf("token" to BuildConfig.API_PARAMS_TOKEN))

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
            val gson = Gson()
            val userAndConditionCodeRsp = gson.fromJson(response.body()?.string(), UserAndConditionCodeResponse::class.java)
            Log.d(tag, "response: ${userAndConditionCodeRsp.body}")
        } else {
            Log.d(tag, "Unsuccessful! response: $response")
        }
    }
}

fun uploadPlotDataApi(coroutineScope: CoroutineScope, tag: String, surveyData: String = "") {
    coroutineScope.launch {
        val requestBody = surveyData.toRequestBody("application/json".toMediaTypeOrNull())

        val response = try {
            RetrofitInstance.uploadPlotDataApiService.createInvestigationRecord(requestBody)
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
            Log.d(tag, "Unsuccessful! response: $response")
        }
    }

}