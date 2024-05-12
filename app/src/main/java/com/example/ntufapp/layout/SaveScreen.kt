package com.example.ntufapp.layout

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
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
import com.example.ntufapp.data.ntufappInfo.Companion.outputDir
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.widget.dialog.GeneralConfirmDialog
import com.example.ntufapp.ui.theme.LayoutDivider
import com.example.ntufapp.utils.checkIfFileExists
import com.example.ntufapp.utils.generateUniqueFilename
import com.example.ntufapp.utils.getFilenameWithFormat
import com.example.ntufapp.utils.saveFile

@RequiresApi(Build.VERSION_CODES.R)
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
                val showOverwriteDialog = remember { mutableStateOf(false) }
                val showBackDialog = remember { mutableStateOf(false) }
                val validFilename = getFilenameWithFormat(newPlotData, outputFilename)
                val currentFilename = remember { mutableStateOf(validFilename) }
                OutlinedButton(
                    modifier = modifier,
                    onClick = { showBackDialog.value = true }
                ) {
                    Text(text = "返回主頁", fontSize = 20.sp)
                }
                OutlinedButton(
                    modifier = modifier,
                    onClick = {
//                        val lastIndexToKeep = newPlotData.PlotTrees.findLast {
//                            it.State.isNotEmpty() &&
//                            it.Species.isNotEmpty() &&
//                            it.DBH != 0.0 &&
//                            it.MeasHeight != 0.0 &&
//                            it.VisHeight != 0.0 &&
//                            it.ForkHeight != 0.0
//                        }
//                        if (lastIndexToKeep != null) {
//                            newPlotData.PlotTrees = newPlotData.PlotTrees.subList(0, lastIndexToKeep.SampleNum)
//                        }
                        showOverwriteDialog.value = checkIfFileExists(context, validFilename)

                        if (!showOverwriteDialog.value) {
                            currentFilename.value = validFilename
                            saveFile(context, newPlotData, outputDir = outputDir, validFilename = currentFilename.value)
                        }
                    }
                ) {
                    Text(text = "儲存樣區資料", fontSize = 20.sp)
                }

                if (showOverwriteDialog.value) {
                    GeneralConfirmDialog(
                        reminder = "${currentFilename.value} 已存在，確定要覆蓋嗎？",
                        confirmText = "覆蓋",
                        cancelText = "另存新檔",
                        onDismiss = {},
                        onConfirmClick = {
                            showOverwriteDialog.value = false
                            saveFile(context, newPlotData, outputDir = outputDir, validFilename = currentFilename.value)
                        },
                        onCancelClick = {
                            currentFilename.value = generateUniqueFilename(context, validFilename.substringBeforeLast("."))
                            saveFile(context, newPlotData, outputDir = outputDir, validFilename = currentFilename.value)
                            showOverwriteDialog.value = false
                        }
                    )
                }

                if (showBackDialog.value) {
                    GeneralConfirmDialog(
                        reminder = "確定要返回主頁嗎？\n返回主頁將會重新開始調查！",
                        confirmText = "確定",
                        onDismiss = {},
                        onConfirmClick = { onBackButtonClick() },
                        onCancelClick = { showBackDialog.value = false }
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

