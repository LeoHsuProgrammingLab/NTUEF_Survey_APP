package com.example.ntufapp.layout

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ntufapp.R
import com.example.ntufapp.api.catalogueApi
import com.example.ntufapp.api.dataType.plotsCatalogueResponse.PlotsCatalogueResponse
import com.example.ntufapp.api.dataType.userAndConditionCodeResponse.User
import com.example.ntufapp.api.dataType.userAndConditionCodeResponse.UserCode
import com.example.ntufapp.api.getTodayDate
import com.example.ntufapp.api.plotApi
import com.example.ntufapp.api.uploadPlotDataApi
import com.example.ntufapp.api.userAndConditionCodeApi
import com.example.ntufapp.data.ntufappInfo.Companion.outputDir
import com.example.ntufapp.ui.theme.LayoutDivider
import com.example.ntufapp.ui.widget.dialog.ChoosePlotToDownloadDialog
import com.example.ntufapp.ui.widget.dialog.UploadFileDialog
import com.example.ntufapp.utils.getFileName
import com.example.ntufapp.utils.parseJsonToSurveyDataForUpload
import com.example.ntufapp.utils.showMessage
import com.example.ntufapp.utils.writeToJson
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun DownloadUploadJsonScreen () {
    val coroutineScope = rememberCoroutineScope()
    BackHandler(enabled = true) {}
    val tag = "LoadJsonScreen"

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//                Spacer(Modifier.padding(40.dp))
        val image = painterResource(id = R.drawable.forest_mountain_svgrepo_com)
        Image(
            painter = image,
            contentDescription = "forest start screen",
            modifier = Modifier.size(200.dp, 200.dp)
//                        .border(2.dp, Color.Black, CircleShape)
        )
        LayoutDivider()
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val context = LocalContext.current
            val showDownloadDialog = remember { mutableStateOf(false) }
            val showUploadDialog = remember { mutableStateOf(false) }
            val listOfPlots = remember { mutableStateOf(emptyMap<String, Map<String, List<Pair<String, String>>>>()) }
            Button(
                modifier = Modifier.padding(5.dp),
                onClick = {
                    Log.d(tag, "Download Info Button clicked")

                    catalogueApi(coroutineScope, tag) { response: PlotsCatalogueResponse?, log: String ->
                        if (response != null) {
                            val structuredMap = response.body.data_list.groupBy { it.dept_name }.mapValues { (dept, dataList) ->
                                dataList.associate { data ->
                                    val areaName = "${data.area_name} (${data.area_kinds_name})"
                                    val locationList = data.location_list.map { location ->
                                        Pair(location.location_name, location.location_mid)
                                    }
                                    areaName to locationList
                                }
                            }
                            listOfPlots.value = structuredMap
                            showDownloadDialog.value = true
                        } else {
                            Log.d(tag, log)
                            showMessage(context, log)
                        }
                    }
                }
            ) {
                Text("下載樣區資料")
            }

            if (showDownloadDialog.value) {
                ChoosePlotToDownloadDialog(
                    allPlotsInfo = listOfPlots.value,
                    onDownload = { location_mid, dept_name ->
                        coroutineScope.launch {
                            val userAndConditionCodeResponse = userAndConditionCodeApi(coroutineScope, tag)
                            val plotInfoRsp = plotApi(coroutineScope, tag, location_mid)
                            val today = getTodayDate()
                            var plotName = "${dept_name}_調查樣區_${today}"
                            if (plotInfoRsp?.body?.location_info?.location_name != null) {
                                plotName = plotInfoRsp.body.location_info.area_name + plotInfoRsp.body.location_info.location_name + "_" + plotInfoRsp.body.newest_investigation.investigation_date
                            }

                            if (plotInfoRsp != null) {
                                plotInfoRsp.userList = extractUserListWithDeptName(userAndConditionCodeResponse?.body?.user_code_list!!, dept_name)
                            }

                            // save data to json
                            try {
                                val file = File(outputDir, "$plotName.json")
                                val gson = Gson()
                                val myJson = gson.toJson(plotInfoRsp)
                                writeToJson(context, file, myJson)
                                showMessage(context, "檔案${file.absoluteFile.name}儲存成功！\n${file.absoluteFile}")
                            } catch (e: Exception) {
                                showMessage(context, "儲存失敗：${e.message}")
                                e.printStackTrace()
                            }
                        }
                     },
                    onDismiss = {},
                    onCancelClick = { showDownloadDialog.value = false }
                )
            }

            Button(
                modifier = Modifier.padding(5.dp),
                onClick = {
                    showUploadDialog.value = true
                }
            ) {
                Text("上傳樣區資料")
            }

            val selectedFileUri = remember { mutableStateOf<Uri?>(null) }
            val buttonText = remember { mutableStateOf("請選擇JSON檔案") }
            val filePickerLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()) { uri: Uri? ->
                selectedFileUri.value = uri
                buttonText.value = getFileName(context, uri).takeIf { it.isNotEmpty() } ?: "請選擇JSON檔案"
            }

            if (showUploadDialog.value) {
                UploadFileDialog(
                    filePicker = filePickerLauncher,
                    header = "請上傳完成調查之樣區",
                    mainButtonText = buttonText.value,
                    nextButtonText = "上傳",
                    onDismiss = {},
                    onSendClick = {
                        if (selectedFileUri.value != null) {
                            coroutineScope.launch {
                                try {
                                    val surveyDataForUpload = parseJsonToSurveyDataForUpload(selectedFileUri.value!!, context)
                                    Log.d(tag, "surveyDataForUpload: ${surveyDataForUpload?.investigation_treeHeight_user}, ${surveyDataForUpload?.update_user}")
                                    Log.d(tag, "surveyDataForUpload: ${surveyDataForUpload?.investigation_user}, ${surveyDataForUpload?.location_mid}")
                                    val uploadResponse = uploadPlotDataApi(coroutineScope, tag, surveyDataForUpload!!)
                                    Log.d(tag, "uploadResponse: $uploadResponse")
                                } catch (e: Exception) {
                                    Log.i(tag, "exError: $e")
                                    showMessage(context, "檔案解析時發生錯誤！")
                                }
                            }
                        } else {
                            showMessage(context, "請選擇JSON檔案")
                        }
                        showUploadDialog.value = false
                    },
                    onCancelClick = {
                        buttonText.value = "請選擇JSON檔案"
                        showUploadDialog.value = false
                    }
                )
            }
        }
    }
}

fun extractUserListWithDeptName(userCodeList: List<UserCode>, TargetDeptName: String): List<User> {
    return userCodeList.filter {
        it.dept_name == TargetDeptName
    }.flatMap {
        it.user_list
    }
}