package com.example.ntufapp.layout

import android.util.Log
import androidx.activity.compose.BackHandler
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
import com.example.ntufapp.api.extractNumber
import com.example.ntufapp.api.getTodayDate
import com.example.ntufapp.api.plotApi
import com.example.ntufapp.api.transformToUploadData
import com.example.ntufapp.api.uploadPlotDataApi
import com.example.ntufapp.data.ntufappInfo.Companion.outputDir
import com.example.ntufapp.ui.theme.LayoutDivider
import com.example.ntufapp.ui.widget.dialog.ChoosePlotToDownloadDialog
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
                    Log.d(tag, "Button clicked")

                    catalogueApi(coroutineScope, tag) { response: PlotsCatalogueResponse?, log: String ->
                        if (response != null) {
                            val structuredMap = response.body.data_list.groupBy { it.dept_name }.mapValues { (dept, dataList) ->
                                dataList.map { data ->
                                    val areaName = "${data.area_name} (${data.area_kinds_name})"
                                    val locationList = data.location_list.map { location ->
                                        Pair(location.location_name, location.location_mid)
                                    }
                                    areaName to locationList
                                }.toMap()
                            }
                            listOfPlots.value = structuredMap
//                            Log.d(tag, "listOfPlots: $listOfPlots")
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
                    onDownload = {
                        coroutineScope.launch {
                            val plotInfoRsp = plotApi(coroutineScope, tag, it)
                            val today = getTodayDate()
                            var plotName = "調查樣區_${today}"
                            if (plotInfoRsp?.body?.location_info?.location_name != null) {
                                plotName = plotInfoRsp.body.location_info.location_name + "_" + plotInfoRsp.body.newest_investigation.investigation_date
                            }

                            // save data to json
                            val saveJson = transformToUploadData(plotInfoRsp!!)
                            try {
                                val file = File(outputDir, "$plotName.json")
                                val gson = Gson()
                                val myJson = gson.toJson(saveJson)
                                writeToJson(file, myJson)
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
            // TODO: Check if data is valid
            if (showUploadDialog.value) {
                uploadPlotDataApi(coroutineScope, tag)
            }
        }
    }
}