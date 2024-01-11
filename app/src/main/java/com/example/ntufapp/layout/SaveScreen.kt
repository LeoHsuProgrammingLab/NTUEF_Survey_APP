package com.example.ntufapp.layout

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.R
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.LayoutDivider
import com.example.ntufapp.utils.DisableBackButtonHandler
import com.example.ntufapp.utils.ExternalStoragePermissionHandler
import com.example.ntufapp.utils.saveFile
import com.example.ntufapp.utils.showMessage

@Composable
fun SaveScreen(
    newPlotData: PlotData,
    outputFilename: String,
    onBackButtonClick: () -> Unit,
) {
    DisableBackButtonHandler(backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher!!)

    val context = LocalContext.current
    val modifier = Modifier.padding(10.dp)
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val image = painterResource(id = R.drawable.database_download_svgrepo_com)
        Image(painter = image,
            contentDescription = "save data screen",
            modifier = Modifier.size(200.dp, 200.dp)
        )
        LayoutDivider()
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val showDialog = remember { mutableStateOf(false) }
//            val permissionGranted = remember { mutableStateOf(false) }
//            ExternalStoragePermissionHandler { permissionGranted.value = true }

            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    modifier = modifier,
                    onClick = onBackButtonClick
                ) {
                    Text(text = "返回主頁", fontSize = 20.sp)
                }
                OutlinedButton(
                    modifier = modifier,
                    onClick = {
                        saveFile(context, newPlotData, filename = outputFilename, toCSV = true)
                        showDialog.value = true
                    }
                ) {
                    Text(text = "儲存樣區資料", fontSize = 20.sp)
                }

//                if (showDialog.value) {
//                    SaveJsonDialog(
//                        onDismiss = {},
//                        onSaveClick = {
//                            showDialog.value = false
//                            saveJsonFile(context, newPlotData, toCSV = true)
//                        },
//                        onCancelClick = { showDialog.value = false }
//                    )
//                }
            }
        }
    }
}

