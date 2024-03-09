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
import com.example.ntufapp.api.plotApi
import com.example.ntufapp.api.uploadPlotDataApi
import com.example.ntufapp.ui.theme.LayoutDivider
import com.example.ntufapp.ui.widget.ChoosePlotToDownloadDialog
import com.example.ntufapp.utils.showMessage

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
            val showDialog = remember { mutableStateOf(false) }
            val listOfPlots = remember { mutableStateOf(emptyMap<String, List<Pair<String, String>>>()) }
            Button(
                modifier = Modifier.padding(5.dp),
                onClick = {
                    Log.d(tag, "Button clicked")

                    catalogueApi(coroutineScope, tag) { response: PlotsCatalogueResponse?, log: String ->
                        if (response != null) {
                            val updatedMap = response.body.data_list.associate {data ->
                                val plotName = data.dept_name + data.area_name + " (" + data.area_kinds_name + ") "
                                val locationList = data.location_list.map { location ->
                                    Pair(location.location_name, location.location_mid)
                                }
                                plotName to locationList
                            }
                            listOfPlots.value = updatedMap
                            Log.d(tag, "listOfPlots: $listOfPlots")
                            showDialog.value = true
                        } else {
                            Log.d(tag, log)
                            showMessage(context, log)
                        }
                    }

                    // TODO: Dialog with DropdownMenu to select plot and button to get plot data (V)
                    // TODO: plotApi(coroutineScope, tag, location_mid) (V)
                    // TODO: show plot data & save to local storage
                    // TODO: Get the correct data
                }
            ) {
                Text("下載樣區資料")
            }

            if (showDialog.value) {
                ChoosePlotToDownloadDialog(
                    allPlotsInfo = listOfPlots.value,
                    onDownload = { plotApi(coroutineScope, tag, it) },
                    onDismiss = {},
                    onCancelClick = { showDialog.value = false }
                )
            }

            Button(
                modifier = Modifier.padding(5.dp),
                onClick = {
                    Log.d(tag, "Button clicked")
                    uploadPlotDataApi(coroutineScope, tag)
                }
            ) {
                Text("上傳樣區資料")
            }
        }
    }
}