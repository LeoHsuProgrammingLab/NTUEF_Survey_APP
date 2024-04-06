package com.example.ntufapp.layout

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.R
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.widget.dialog.ManualInputNewPlotDialog
import com.example.ntufapp.ui.widget.dialog.NewSurveyUploadChoiceDialog
import com.example.ntufapp.ui.widget.dialog.UploadFileDialog
import com.example.ntufapp.ui.theme.LayoutDivider
import com.example.ntufapp.ui.widget.dialog.GeneralConfirmDialog
import com.example.ntufapp.utils.getFileName
import com.example.ntufapp.utils.parseJsonToMetaData
import com.example.ntufapp.utils.showMessage

@Composable
fun PlotOptionsScreen(
    onNextButtonClick: (PlotData, String, String) -> Unit
) {
//    DisableBackButtonHandler(from = "PlotOptionsScreen")
    BackHandler(enabled = true) {}
    val tag = "PlotOptions"

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//                Spacer(Modifier.padding(40.dp))
        val image = painterResource(id = R.drawable.forest_mountain_svgrepo_com)
        Image(painter = image,
            contentDescription = "forest start screen",
            modifier = Modifier.size(200.dp, 200.dp)
//                        .border(2.dp, Color.Black, CircleShape)
        )
        LayoutDivider()
        Row {
            val context = LocalContext.current
            val selectedFileUri = remember { mutableStateOf<Uri?>(null) }
            val buttonText = remember { mutableStateOf("請選擇JSON檔案") }
            val filePickerLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()) { uri: Uri? ->
                selectedFileUri.value = uri
                buttonText.value = getFileName(context, uri).takeIf { it.isNotEmpty() } ?: "請選擇JSON檔案"
            }
            val plotData = remember { mutableStateOf(PlotData()) }
            val surveyType = remember { mutableStateOf("") }
            val outputFilename = remember { mutableStateOf("") }

            val showOldPlotUpload = remember { mutableStateOf(false) }
            val showOldUploadData = remember { mutableStateOf(false) }
            val showNewPlotUploadChoices = remember { mutableStateOf(false) }
            val showNewPlotUpload = remember { mutableStateOf(false) }
            val showNewUploadData = remember { mutableStateOf(false) }
            val showNewPlotManualInput = remember { mutableStateOf(false) }
            val dismissDialog: (Boolean) -> Unit = { isNewPlot: Boolean ->
                if (isNewPlot) {
                    showNewPlotUpload.value = false
                } else {
                    showOldPlotUpload.value = false
                }
                buttonText.value = "請選擇JSON檔案"
            }

            // Old Plot
            Button(
                modifier = Modifier.padding(end = 20.dp),
                onClick = { showOldPlotUpload.value = true }
            ) {
                Text("複查樣區", fontSize = 20.sp)
            }
            // Upload Old Plot by JSON
            if(showOldPlotUpload.value) {
                UploadFileDialog(
                    header = "請匯入樣區資料",
                    mainButtonText = buttonText.value,
                    nextButtonText = "匯入",
                    onDismiss = {},
                    onSendClick = {
                        if(selectedFileUri.value != null) {
                            try {
                                plotData.value  = parseJsonToMetaData(selectedFileUri.value!!, context)!!
                                if(plotData.value.ManageUnit == "" || plotData.value.PlotTrees.size == 0) {
                                    showMessage(context, "你匯入了錯誤的檔案(.json)！")
                                } else {
                                    showOldUploadData.value = true
                                    outputFilename.value = getFileName(context, selectedFileUri.value)
                                }
                            } catch (e: Exception) {
                                showMessage(context, "檔案解析時發生錯誤！")
                            }
                        } else {
                            showMessage(context, "請選擇JSON檔案")
                        }
                        dismissDialog(false)
                    },
                    onCancelClick = { dismissDialog(false) },
                    filePicker = filePickerLauncher,
                )
            }

            val confirmHeader = "您已匯入${plotData.value.ManageUnit}${plotData.value.SubUnit}的資料\n樣區名稱：${plotData.value.PlotName}\n樣區編號：${plotData.value.PlotNum}\n該樣區有${plotData.value.PlotTrees.size}棵樣樹"

            if(showOldUploadData.value) {
                GeneralConfirmDialog(
                    reminder = confirmHeader,
                    confirmText = stringResource(id = R.string.next),
                    onDismiss = {},
                    onCancelClick = { showOldUploadData.value = false },
                    onConfirmClick = {
                        surveyType.value = "ReSurvey"
                        onNextButtonClick(plotData.value, surveyType.value, outputFilename.value)
                    }
                )
            }

            // New Plot
            Button(
                onClick = { showNewPlotUploadChoices.value = true }
            ) {
                Text("新增樣區", fontSize = 20.sp)
            }

            if (showNewPlotUploadChoices.value) {
                NewSurveyUploadChoiceDialog(
                    onDismiss = { showNewPlotUploadChoices.value = false },
                    onUploadTypeClick = {uploadType ->
                        if (uploadType == "JSON") {
                            showNewPlotUpload.value = true
                        } else {
                            showNewPlotManualInput.value = true
                        }
                        showNewPlotUploadChoices.value = false
                        surveyType.value = "NewSurvey"
                    },
                )
            }
            // Upload New Plot by JSON
            if (showNewPlotUpload.value) {
                UploadFileDialog(
                    header = "請匯入樣區資料",
                    mainButtonText = buttonText.value,
                    nextButtonText = "匯入",
                    onDismiss = {},
                    onSendClick = {
                        if(selectedFileUri.value != null) {
                            try {
                                plotData.value  = parseJsonToMetaData(selectedFileUri.value!!, context)!!
                                if(plotData.value.ManageUnit == "") {
                                    showMessage(context, "你匯入了錯誤的檔案(.json)！")
                                } else {
                                    showNewUploadData.value = true
                                    outputFilename.value = getFileName(context, selectedFileUri.value!!)
                                }
                            } catch (e: Exception) {
                                showMessage(context, "檔案解析時發生錯誤！")
                            }
                        } else {
                            showMessage(context, "請選擇JSON檔案")
                        }
                        dismissDialog(true)
                    },
                    onCancelClick = { dismissDialog(true) },
                    filePicker = filePickerLauncher
                )
            }


            if (showNewUploadData.value) {
                GeneralConfirmDialog(
                    reminder = confirmHeader,
                    confirmText = stringResource(id = R.string.next),
                    onDismiss = {},
                    onCancelClick = { showNewUploadData.value = false },
                    onConfirmClick = { onNextButtonClick(plotData.value, surveyType.value, outputFilename.value) }
                )
            }

            // Manual Input New Plot
            if (showNewPlotManualInput.value) {
                ManualInputNewPlotDialog(
                    onDismiss = { showNewPlotManualInput.value = false },
                    onSendClick = { onNextButtonClick(it, surveyType.value, "") }
                )
            }
        }
    }
}
