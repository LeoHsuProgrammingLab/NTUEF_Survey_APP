package com.example.ntufapp.api

import com.example.ntufapp.api.dataType.plotInfoResponse.PlotInfoResponse
import com.example.ntufapp.api.dataType.responseToSurveyData.InvestigationRecord
import com.example.ntufapp.api.dataType.responseToSurveyData.Photo
import com.example.ntufapp.api.dataType.responseToSurveyData.SurveyDataForUpload
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import android.util.Base64
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun createRequestBody(jsonContent: List<Pair<String, String>>): RequestBody {
    val json = JSONObject().apply {
        jsonContent.forEach {
            put(it.first, it.second)
        }
    }
    val jsonString = json.toString()
    return jsonString.toRequestBody("application/json".toMediaTypeOrNull())
}

fun transformToUploadData(response: PlotInfoResponse): SurveyDataForUpload {
    val body = response.body
    val newestInvestigation = body.newest_investigation
    val investigationRecordList = newestInvestigation.investigation_record_list.flatMap { record ->
        record.investigation_result_list.map {result ->
            InvestigationRecord(
                investigation_item_code = result.investigation_item_code,
                investigation_item_result = result.investigation_result,
                location_sid = record.location_sid,
                location_wx = body.location_info.location_wx,
                location_wy = body.location_info.location_wy
            )
        }
    }

    return SurveyDataForUpload(
        area_id = body.location_info.area_id,
        area_investigation_setup_id = body.location_info.area_investigation_setup_id,
        investigation_date = newestInvestigation.investigation_date,
        investigation_record_list = investigationRecordList,
        investigation_treeHeight_user = newestInvestigation.investigation_treeHeight_user_list.user_id,
        investigation_user = newestInvestigation.investigation_user_list.firstOrNull()?.user_name?: "", // Use empty string if no user is found
        investigation_year = newestInvestigation.investigation_year,
        location_mid = response.location_mid,
        photo_list = emptyList() // Assuming no photos are updated; adjust as needed
    )
}

fun encodeImageToBase64(imagePath: String): String {
    val file = File(imagePath)
    val bytes = FileInputStream(file).use { it.readBytes() }
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

fun createPhotoObject(areaCode: String, areaName: String, file: File): Photo {
    val dateFormat = SimpleDateFormat("yyyyMM", Locale.getDefault())
    val datePart = dateFormat.format(Date())
    val serial = "001" // This should be dynamically generated or tracked
    val fileName = "${areaCode}_${areaName}_${datePart}_$serial.jpg"
    val fileData = encodeImageToBase64(file.path)

    return Photo(file_name = fileName, file_data = fileData)
}

fun getTodayDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date())
}

fun extractNumber(s: String): Int {
    val regex = "\\d+".toRegex()
    return regex.find(s)?.value?.toInt() ?: 0
}