package com.example.ntufapp.layout

import android.content.Context
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
import androidx.compose.runtime.MutableState
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
import com.example.ntufapp.api.getTodayDate
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.widget.dialog.UploadFileDialog
import com.example.ntufapp.ui.theme.LayoutDivider
import com.example.ntufapp.ui.widget.dialog.GeneralConfirmDialog
import com.example.ntufapp.utils.getFileName
import com.example.ntufapp.utils.parseJsonToPlotData
import com.example.ntufapp.utils.showMessage

@Composable
fun PlotOptionsScreen(
    onNextButtonClick: (PlotData, String, String) -> Unit
) {
    BackHandler(enabled = true) {}
    val context = LocalContext.current
    val selectedFileUri = remember { mutableStateOf<Uri?>(null) }
    val buttonText = remember { mutableStateOf("請選擇JSON檔案") }
    val plotData = remember { mutableStateOf(PlotData()) }
    val surveyType = remember { mutableStateOf("") }
    val outputFilename = remember { mutableStateOf("") }

    val showOldPlotUpload = remember { mutableStateOf(false) }
    val showOldUploadData = remember { mutableStateOf(false) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri.value = uri
        buttonText.value = getFileName(context, uri).takeIf { it.isNotEmpty() } ?: "請選擇JSON檔案"
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val image = painterResource(id = R.drawable.forest_mountain_svgrepo_com)
        Image(painter = image,
            contentDescription = "forest start screen",
            modifier = Modifier.size(200.dp, 200.dp)
        )
        LayoutDivider()
        Row {
            // Old Plot
            StartSurveyButton(showOldPlotUpload = showOldPlotUpload, buttonText = "開始調查")
            // Upload Old Plot by JSON
            if(showOldPlotUpload.value) {
                UploadFileDialog(
                    header = "請匯入樣區資料",
                    mainButtonText = buttonText.value,
                    nextButtonText = "匯入",
                    onDismiss = {},
                    onSendClick = {
                       handleFileInput(
                            context,
                            selectedFileUri,
                            plotData,
                            buttonText,
                            outputFilename,
                            showOldUploadData,
                            showOldPlotUpload
                        )
                    },
                    onCancelClick = { showOldPlotUpload.value = false },
                    filePicker = filePickerLauncher,
                )
            }

            if(showOldUploadData.value) {
                val confirmHeader = "您已匯入 ${plotData.value.ManageUnit}${plotData.value.SubUnit} 的資料\n樣區種類：${plotData.value.AreaKind}\n調查區編號：${plotData.value.AreaNum}\n該樣區有${plotData.value.PlotTrees.size}棵樣樹"

                GeneralConfirmDialog(
                    reminder = confirmHeader,
                    confirmText = stringResource(id = R.string.next),
                    onDismiss = {},
                    onCancelClick = { showOldUploadData.value = false },
                    onConfirmClick = {
                        showOldPlotUpload.value = false
                        surveyType.value = "ReSurvey"
                        onNextButtonClick(plotData.value, surveyType.value, outputFilename.value)
                    }
                )
            }
        }
    }
}

@Composable
fun StartSurveyButton(
    showOldPlotUpload: MutableState<Boolean>,
    buttonText: String
) {
    Button(
        modifier = Modifier.padding(end = 20.dp),
        onClick = { showOldPlotUpload.value = true }
    ) {
        Text(buttonText, fontSize = 20.sp)
    }
}


fun handleFileInput(
    context: Context,
    selectedFileUri: MutableState<Uri?>,
    plotData: MutableState<PlotData>,
    buttonText: MutableState<String>,
    outputFilename: MutableState<String>,
    showOldUploadData: MutableState<Boolean>,
    showOldPlotUpload: MutableState<Boolean>
) {
    if (selectedFileUri.value != null) {
        try {
            plotData.value = parseJsonToPlotData(selectedFileUri.value!!, context)!!
            showOldUploadData.value = true
            val fileNameParts = getFileName(context, selectedFileUri.value).split("_")
            outputFilename.value = fileNameParts.dropLast(2).joinToString("_") + "_" + getTodayDate() + ".json"
        } catch (e: Exception) {
            showMessage(context, "檔案解析時發生錯誤！\n資料不齊全！")
        }
    } else {
        showMessage(context, "請選擇JSON檔案")
    }
    buttonText.value = "請選擇JSON檔案"
    showOldPlotUpload.value = false
}
