package com.example.ntufapp.layout

import androidx.activity.compose.BackHandler
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
import com.example.ntufapp.ui.GeneralConfirmDialog
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
//    DisableBackButtonHandler()
    BackHandler(enabled = true) {}
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
//            val permissionGranted = remember { mutableStateOf(false) }
//            ExternalStoragePermissionHandler { permissionGranted.value = true }

            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val showDialog = remember { mutableStateOf(false) }
                val showBackDialogue = remember { mutableStateOf(false) }
                OutlinedButton(
                    modifier = modifier,
                    onClick = { showBackDialogue.value = true }
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
                if (showBackDialogue.value) {
                    GeneralConfirmDialog(
                        reminder = "確定要返回主頁嗎？\n返回主頁將會清除所有資料！",
                        confirm = "確定",
                        onDismiss = { /*TODO*/ },
                        onConfirmClick = { onBackButtonClick() },
                        onCancelClick = { showBackDialogue.value = false }
                    )
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

