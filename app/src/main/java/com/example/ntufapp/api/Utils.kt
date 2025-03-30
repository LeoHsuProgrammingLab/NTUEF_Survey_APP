package com.example.ntufapp.api

import android.content.Context
import com.example.ntufapp.api.dataType.plotInfoResponse.PlotInfoResponse
import com.example.ntufapp.api.dataType.surveyDataForUpload.InvestigationRecord
import com.example.ntufapp.api.dataType.surveyDataForUpload.Photo
import com.example.ntufapp.api.dataType.surveyDataForUpload.SurveyDataForUpload
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.ntufapp.data.DataSource
import com.example.ntufapp.model.PlotData
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

fun transformPlotInfoResponseToPlotData(
    response: PlotInfoResponse,
    context: Context
): PlotData? {
    val locationInfo = response.body.location_info
    val newestInvestigation = response.body.newest_investigation
    val newestLocation = response.body.newest_location

    val surveyors = newestInvestigation.investigation_user_list.associate { it.user_id to it.user_name }
    val htSurveyor = Pair(newestInvestigation.investigation_treeHeight_user_list.user_id, newestInvestigation.investigation_treeHeight_user_list.user_name) // Assuming tree height surveyor is singular

    val plotData = PlotData(
        Date = newestInvestigation.investigation_date,
        Year = newestInvestigation.investigation_year,
        ManageUnit = locationInfo.area_name,
        SubUnit = locationInfo.location_name,
        location_code = locationInfo.location_code,

        AreaKind = locationInfo.area_kinds_name,
        AreaNum = locationInfo.area_code,
        PlotType = locationInfo.location_type_name,

        PlotArea = 0.0,
        TWD97_X = formatWxWy(locationInfo.location_wx),
        TWD97_Y = formatWxWy(locationInfo.location_wy),

        Altitude = locationInfo.area_elevation.toDoubleOrNull() ?: 0.0, // Using elevation as altitude for demonstration
        Slope = locationInfo.area_slope.toDoubleOrNull() ?: 0.0,
        Aspect = locationInfo.area_aspect,

        // Surveyor doesn't need to be assigned
        // HtSurveyor = htSurveyor,
        PlotTrees = mutableListOf(), // Additional logic needed to populate trees if applicable

        userList = response.userList,
        area_id = locationInfo.area_id,
        area_investigation_setup_id = locationInfo.area_investigation_setup_id,
        area_investigation_setup_list = locationInfo.area_investigation_setup_list.associateBy({ it.investigation_item_name }, { it.investigation_item_code }).toMutableMap(),
        location_mid = response.location_mid,
        investigation_user_map = newestInvestigation.investigation_user_list.associateBy({ it.user_id }, { it.user_name }).toMutableMap(),
        area_compart = locationInfo.area_compart.toString(),
        speciesList = response.species_list
    )
    // init tree data
    plotData.initPlotTrees(response.body.newest_location_count, newestLocation.map { it.location_sid })
    // find corresponding investigation item code
    val investigationDBHCode = locationInfo.area_investigation_setup_list
        .firstOrNull { it.investigation_item_name == "胸徑" }
        ?.investigation_item_code
    val investigationHeightCode = locationInfo.area_investigation_setup_list
        .firstOrNull { it.investigation_item_name == "樹高" }
        ?.investigation_item_code
    val investigationStateCode = locationInfo.area_investigation_setup_list
        .firstOrNull { it.investigation_item_name == "生長狀態" }
        ?.investigation_item_code
//    val investigationForkedHeightCode = locationInfo.area_investigation_setup_list
//        .firstOrNull { it.investigation_item_name == "分岔樹高" }
//        ?.investigation_item_code
//    val investigationBaseDiameterCode = locationInfo.area_investigation_setup_list
//        .firstOrNull { it.investigation_item_name == "基徑" }
//        ?.investigation_item_code

    // get data from newest investigation record by the corresponding investigation item code
    // Step 1: Create a map of location_sid to Tree objects
    val locationSidToTreeMap = plotData.PlotTrees.associateBy { it.location_sid }

    // Step 2: Iterate through newestInvestigation.investigation_record_list and find the corresponding Tree object
    newestInvestigation.investigation_record_list.forEach { record ->
        val tree = locationSidToTreeMap[record.location_sid]
        if (tree != null) {
            // Step 3: Assign the DBH, MeasHeight, and State values to the found Tree object
            tree.DBH = record.investigation_result_list
                .firstOrNull { it.investigation_item_code == investigationDBHCode }?.investigation_result?.toDoubleOrNull() ?: 0.0
            tree.MeasHeight = record.investigation_result_list
                .firstOrNull { it.investigation_item_code == investigationHeightCode }?.investigation_result?.toDoubleOrNull() ?: 0.0

            val growthStateCodeList = record.investigation_result_list
                .firstOrNull { it.investigation_item_code == investigationStateCode }?.investigation_result?.split(",") ?: emptyList()
            tree.State = DataSource.GrowthCodeList.filter { it.code in growthStateCodeList }.map { it.code_name }.toMutableList()
        }
    }

    newestLocation.forEach { location ->
        val tree = plotData.PlotTrees.find { it.location_sid == location.location_sid }
        if (tree != null) {
            tree.location_wx = formatWxWy(location.location_wx)
            tree.location_wy = formatWxWy(location.location_wy)
            tree.Species = location.location_breed_name
        }
    }

    // check if the data is intact
    if (plotData.checkPlotData(context)) {
        return plotData
    } else {
        return null
    }


}

fun transformPlotDataToSurveyDataForUpload(plotData: PlotData): SurveyDataForUpload {
    val investigationRecordList = plotData.PlotTrees.flatMap { tree ->
        listOfNotNull(
            InvestigationRecord(
                investigation_item_code = plotData.getHeightCode(),
                investigation_item_result = tree.MeasHeight.toString(),
                location_sid = tree.location_sid,
                location_wx = tree.location_wx,
                location_wy = tree.location_wy
            ),
            InvestigationRecord(
                investigation_item_code = plotData.getDBHCode(),
                investigation_item_result = tree.DBH.toString(),
                location_sid = tree.location_sid,
                location_wx = tree.location_wx,
                location_wy = tree.location_wy
            ),
            InvestigationRecord(
                investigation_item_code = plotData.getStateCode(),
                investigation_item_result = extractStateCodeFromStateString(tree.State),
                location_sid = tree.location_sid,
                location_wx = tree.location_wx,
                location_wy = tree.location_wy
            ),
            InvestigationRecord(
                investigation_item_code = plotData.getForkedHeightCode(),
                investigation_item_result = tree.ForkHeight.toString(),
                location_sid = tree.location_sid,
                location_wx = tree.location_wx,
                location_wy = tree.location_wy
            ),
//            InvestigationRecord(
//                investigation_item_code = plotData.getBaseDiameterCode(),
//                investigation_item_result = "0",
//                location_sid = tree.location_sid,
//                location_wx = tree.location_wx,
//                location_wy = tree.location_wy
//            ),
            InvestigationRecord(
                investigation_item_code = plotData.getVisHeightCode(),
                investigation_item_result = tree.VisHeight.toString(),
                location_sid = tree.location_sid,
                location_wx = tree.location_wx,
                location_wy = tree.location_wy
            ),
            InvestigationRecord(
                investigation_item_code = plotData.getTreeSpeciesCode(),
                investigation_item_result = tree.Species,
                location_sid = tree.location_sid,
                location_wx = tree.location_wx,
                location_wy = tree.location_wy
            )
        )
    }

    return SurveyDataForUpload(
        area_id = plotData.area_id,
        area_investigation_setup_id = plotData.area_investigation_setup_id,
        investigation_date = plotData.Date,
        investigation_record_list = investigationRecordList,
        investigation_treeHeight_user = if (plotData.HtSurveyor == null) {
            plotData.userList.first().user_code.toInt()
        } else {
            plotData.HtSurveyor!!.first
        },
        investigation_user = if (plotData.Surveyor.keys.isEmpty()) {
            plotData.userList.first().user_code
        } else {
            plotData.Surveyor.keys.joinToString(",")
        },
        investigation_year = plotData.Date.substring(0, 4),
        location_mid = plotData.location_mid,
        photo_list = emptyList(), // Assuming no photos are updated; adjust as needed
        update_user = plotData.userList.first().user_code.toInt()
    )
}

fun formatWxWy(w: String): String {
    if (w.isEmpty()) {
        return "X"
    }
    return String.format("%.0f", w.toDouble())
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

fun extractStateCodeFromStateString(stateList: MutableList<String>): String {
    return DataSource.GrowthCodeList.filter { it.code_name in stateList || stateList.any { state -> state.contains(it.code_name) } }.joinToString(",") { it.code }
}
