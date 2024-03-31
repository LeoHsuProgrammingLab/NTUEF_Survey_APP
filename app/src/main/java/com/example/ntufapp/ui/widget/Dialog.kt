package com.example.ntufapp.ui.widget

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.R
import com.example.ntufapp.data.DataSource
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.basicModifier
import com.example.ntufapp.utils.showMessage

@Composable
fun ChoosePlotToDownloadDialog(
    allPlotsInfo: Map<String, Map<String, List<Pair<String, String>>>>,
    onDownload: (String) -> Unit,
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit
) {
    val midLocation = remember { mutableStateOf("") }
    val context = LocalContext.current
    Dialog(
        onDismissRequest = { onDismiss.invoke() },
    ) {
        Surface(shape = Shapes.large) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogHeader(header = "請選擇樣區並下載")
                PlotSelectionDropDownMenu(
                    label = "",
                    allPlotsInfo = allPlotsInfo,
                    onChoose = { midLocation.value = it })

                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        modifier = basicModifier,
                        onClick = {
                            onCancelClick()
                        }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                    Button(
                        modifier = basicModifier,
                        onClick = {
//                        Log.d("LoadJsonScreen", midLocation.value);
                            if (midLocation.value != "") {
                                onDownload(midLocation.value)
                            } else {
                                showMessage(context, "請選擇樣區!")
                            }
                        }
                    ) {
                        Text(stringResource(id = R.string.download))
                    }
                }
            }
        }
    }
}

@Composable
fun UploadFileDialog( // Plot Options Screen
    selectedFileUri: Uri?,
    filePicker: ManagedActivityResultLauncher<String, Uri?>,
    buttonText: String,
    onDismiss: () -> Unit,
    onSendClick: (Uri?) -> Unit,
    onCancelClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss.invoke() }
    ) {
        Surface(shape = Shapes.small) {
            Column(
                modifier = basicModifier,
            ) {
                DialogHeader(header = "請匯入樣區資料")
                Spacer(modifier = basicModifier)
                Row(
                    modifier = Modifier.width(200.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = basicModifier,
                        onClick = {
                            filePicker.launch("application/json")
                        }
                    ) {
                        Text(buttonText)
                    }
                }

                Spacer(modifier = basicModifier)
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val context = LocalContext.current
                    Button(
                        modifier = basicModifier,
                        onClick = onCancelClick,
                    ) {
                        Text(stringResource(id = (R.string.cancel)))
                    }

                    Button(
                        modifier = basicModifier,
                        onClick = {
                            if (selectedFileUri != null) {
                                onSendClick(selectedFileUri)
                            } else {
                                showMessage(context, "請選擇JSON檔案")
                            }
                        }
                    ) {
                        Text("匯入")
                    }
                }
            }
        }
    }
}

@Composable
fun AddDialog(
    type: String = "tree",
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    onNextButtonClick: (String) -> Unit,
    curSize: Int
){
    Dialog(
        onDismissRequest = { onDismiss.invoke() }
    ) {
        Surface(shape = Shapes.small) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val surveyorName = remember { mutableStateOf("") }
                if (type == "tree") {
                    DialogHeader(header = "您預計新增第 ${curSize + 1} 棵樣樹")
                } else {
                    DialogHeader(header = "您預計新增第 ${curSize + 1} 位調查人員")
                    OutlinedTextField(
                        value = surveyorName.value,
                        onValueChange = {
                            surveyorName.value = it
                        },
                        placeholder = { Text("請輸入調查人員姓名") },
                        modifier = basicModifier.width(200.dp)
                    )
                }
                Spacer(modifier = basicModifier)
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = basicModifier,
                        onClick = onCancelClick,
                    ) {
                        Text(stringResource(id = (R.string.cancel)))
                    }

                    Button(
                        modifier = basicModifier,
                        onClick = {
                            if(type == "tree") {
                                onNextButtonClick((curSize + 1).toString())
                            } else {
                                onNextButtonClick(surveyorName.value)
                            }
                        }
                    ) {
                        Text(stringResource(id = (R.string.next)))
                    }
                }
            }
        }
    }
}

@Composable
// https://medium.com/@rooparshkalia/single-choice-dialog-with-jetpack-compose-d021650d31ca
fun ConfirmDialog( // Plot Options Screen
    metaData: PlotData,
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    onNextButtonClick: (PlotData) -> Unit
){
    Dialog(
        onDismissRequest = { onDismiss.invoke() }
    ) {
        Surface(shape = Shapes.small) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogHeader(
                    header = "您已匯入${metaData.ManageUnit}${metaData.SubUnit}的資料\n樣區名稱：${metaData.PlotName}\n樣區編號：${metaData.PlotNum}\n該樣區有${metaData.PlotTrees.size}棵樣樹"
                )
                Spacer(basicModifier)
                Row(
                  horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // cancel button
                    Button(
                        modifier = basicModifier,
                        onClick = onCancelClick,
                    ) {
                        Text(stringResource(id = (R.string.cancel)))
                    }

                    // next button
                    Button(
                        modifier = basicModifier,
                        onClick = { onNextButtonClick(metaData) }
                    ) {
                        Text(stringResource(id = (R.string.next)))
                    }
                }
            }
        }
    }
}

@Composable
fun NewSurveyUploadChoiceDialog(
    onDismiss: () -> Unit,
    onUploadTypeClick: (String) -> Unit,
){
    Dialog(
        onDismissRequest = {}
    ) {
        Surface(shape = Shapes.small) {
            Column(
                modifier = basicModifier,
            ) {
                DialogHeader(header = "請選擇輸入新樣區之方式")
                Spacer(modifier = basicModifier)
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = basicModifier,
                        onClick = { onDismiss.invoke() }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                    Button(
                        modifier = basicModifier,
                        onClick = { onUploadTypeClick("Manual") }
                    ) {
                        Text("手動輸入")
                    }
                    Button(
                        modifier = basicModifier,
                        onClick = { onUploadTypeClick("JSON") }
                    ) {
                        Text("匯入JSON檔")
                    }
                }
            }
        }
    }
}

@Composable
fun SaveJsonDialog(
    onDismiss: () -> Unit, 
    onSaveClick: (String) -> Unit,
    onCancelClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss.invoke() }
    ) {
        val text = remember { mutableStateOf("") }
        Surface(shape = Shapes.small) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = text.value,
                    onValueChange = {
                        text.value = it
                    },
                    label = { Text("請輸入檔名") },
                    leadingIcon = { Icon(imageVector = Icons.Filled.FileDownload, contentDescription = null) }
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onCancelClick
                    ) {
                        Text(stringResource(id = (R.string.cancel)))
                    }
                    OutlinedButton(
                        onClick = {
                            if(text.value != "") {
                                onSaveClick(text.value)
                            }
                        }
                    ) {
                        Text("命名")
                    }
                }
            }
        }
    }
}

@Composable
fun GeneralConfirmDialog(
    reminder: String,
    confirmText: String,
    cancelText: String = stringResource(id = (R.string.cancel)),
    leadingIcon: () -> Unit = {},
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss.invoke() }
    ) {
        val text = remember { mutableStateOf("") }
        Surface(
            shape = Shapes.small
        ) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogHeader(header = reminder)
                Spacer(modifier = basicModifier)
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        modifier = basicModifier,
                        onClick = onCancelClick
                    ) {
                        Text(cancelText)
                    }
                    OutlinedButton(
                        modifier = basicModifier,
                        onClick = onConfirmClick
                    ) {
                        Text(confirmText)
                    }
                }
            }
        }
    }
}

@Composable
fun ManualInputNewPlotDialog(
    onDismiss: () -> Unit,
    onSendClick: (PlotData) -> Unit,
) {
    val context = LocalContext.current
     val plotData = remember {
        mutableStateOf(PlotData())
    }

    Dialog(
        onDismissRequest = {}
    ) {
        val manageUnit = remember { mutableStateOf("") }
        val subUnit = remember { mutableStateOf("") }
        val plotName = remember { mutableStateOf("") }
        val plotNum = remember { mutableStateOf("") }
        val plotType = remember { mutableStateOf("") }
        val plotArea = remember { mutableStateOf("") }
        val altitude= remember { mutableStateOf("") }
        val slope = remember { mutableStateOf("") }
        val aspect = remember { mutableStateOf("") }
        val TWD97_X = remember { mutableStateOf("") }
        val TWD97_Y = remember { mutableStateOf("") }

        Surface(shape = Shapes.small) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogHeader(header = "請輸入樣區資料")
                LazyColumn(
                    modifier = basicModifier.height(500.dp)
                ) {
                    items(1) {
                        TextField(
                            value = manageUnit.value,
                            onValueChange = { text: String -> manageUnit.value = text },
                            label = { Text("營林區") }
                        )
                        TextField(
                            value = subUnit.value,
                            onValueChange = { text: String -> subUnit.value = text },
                            label = { Text("林班地") }
                        )
                        TextField(
                            value = plotName.value,
                            onValueChange = { text: String -> plotName.value = text },
                            label = { Text("樣區名稱") }
                        )
                        TextField(
                            value = plotNum.value,
                            onValueChange = { text: String -> plotNum.value = text },
                            label = { Text("樣區編號") }
                        )
                        TextField(
                            value = plotType.value,
                            onValueChange = { text: String -> plotType.value = text },
                            label = { Text("樣區型態") }
                        )
                        TextField(
                            value = plotArea.value,
                            onValueChange = { text: String -> plotArea.value = text },
                            label = { Text("樣區面積(m2)") },
                            placeholder = { Text("請輸入數字") }
                        )
                        TextField(
                            value = altitude.value,
                            onValueChange = { text: String -> altitude.value = text },
                            label = { Text("樣區海拔(m)") },
                            placeholder = { Text("請輸入數字") }
                        )
                        TextField(
                            value = slope.value,
                            onValueChange = { text: String -> slope.value = text },
                            label = { Text("樣區坡度") },
                            placeholder = { Text("請輸入數字") }
                        )
                        TextField(
                            value = aspect.value,
                            onValueChange = { text: String -> aspect.value = text },
                            label = { Text("樣區坡向") }
                        )
                        TextField(
                            value = TWD97_X.value,
                            onValueChange = { text: String -> TWD97_X.value = text },
                            label = { Text("TWD97_X") }
                        )
                        TextField(
                            value = TWD97_Y.value,
                            onValueChange = { text: String -> TWD97_Y.value = text },
                            label = { Text("TWD97_Y") }
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = basicModifier,
                        onClick = { onDismiss.invoke() }
                    ) {
                        Text(stringResource(id = (R.string.cancel)))
                    }
                    Button(
                        modifier = basicModifier,
                        onClick = {
                            if (
                                manageUnit.value != "" &&
                                subUnit.value != "" &&
                                plotName.value != "" &&
                                plotNum.value != "" &&
                                plotType.value != "" &&
                                (plotArea.value.toDoubleOrNull() ?: Double.NaN).isNaN().not() && // cool syntax
                                (altitude.value.toDoubleOrNull() ?: Double.NaN).isNaN().not() &&
                                (slope.value.toDoubleOrNull() != null) &&
                                aspect.value != "" &&
                                TWD97_X.value != "" &&
                                TWD97_Y.value != ""
                            ) {
                                plotData.value.ManageUnit = manageUnit.value
                                plotData.value.SubUnit = subUnit.value
                                plotData.value.PlotName = plotName.value
                                plotData.value.PlotNum = plotNum.value
                                plotData.value.PlotType = plotType.value
                                plotData.value.PlotArea = plotArea.value.toDouble()
                                plotData.value.Altitude = altitude.value.toDouble()
                                plotData.value.Slope = slope.value.toDouble()
                                plotData.value.Aspect = aspect.value
                                plotData.value.TWD97_X = TWD97_X.value
                                plotData.value.TWD97_Y = TWD97_Y.value
                                onSendClick(plotData.value)
                            } else {
                                showMessage(context, "請輸入正確且完整的樣區資料")
                            }
                        }
                    ) {
                        Text(stringResource(id = (R.string.next)))
                    }
                }
            }
        }
    }
}


@Composable
fun AdjustSpeciesDialog( // Survey Screen
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    onNextButtonClick: (String) -> Unit
){
    val context = LocalContext.current
    val treeSpecies = remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = { onDismiss.invoke() }
    ) {
        Surface(shape = Shapes.small) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchableChooseMenu(
                    totalItemsList = DataSource.SpeciesList as MutableList<String>,
                    onChoose = {
                        treeSpecies.value = it
                    }
                )
                Spacer(modifier = basicModifier)
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Button(
                        modifier = basicModifier,
                        onClick = onCancelClick,
                    ) {
                        Text(stringResource(id = (R.string.cancel)))
                    }

                    Button(
                        modifier = basicModifier,
                        onClick = {
                            if(treeSpecies.value != "") {
                                onNextButtonClick(treeSpecies.value)
                            } else {
                                showMessage(context, "請選擇樹種!")
                            }
                        }
                    ) {
                        Text("確認樹種")
                    }
                }
            }
        }
    }
}

@Composable
fun SurveyConfirmDialog( // Survey Screen
    dbhSetSize: Int,
    measHtSetSize: Int,
    visHtSetSize: Int,
    forkHtSetSize: Int,
    speciesSetSize: Int = 0,
    conditionSetSize: Int = 0,
    surveyType: String = "ReSurvey",
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    onNextButtonClick: () -> Unit
){
    Dialog(
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        Surface(shape = Shapes.small) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogHeader(header = "您還有以下未調查樣樹，確認完成嗎？")
                Column {
                    Text(text = "DBH還有${dbhSetSize}棵樹未調查")
                    Text("樹高還有${measHtSetSize}棵樹未調查")
                    Text("目視樹高還有${visHtSetSize}棵樹未調查")
                    Text("分岔樹高還有${forkHtSetSize}棵樹未調查")
                    if (surveyType == "NewSurvey") {
                        Text("樹種還有${speciesSetSize}棵樹未調查")
                        Text("生長狀況還有${conditionSetSize}棵樹未調查")
                    }
                }
                Spacer(modifier = basicModifier)
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = basicModifier,
                        onClick = onCancelClick,
                    ) {
                        Text("繼續調查")
                    }

                    Button(
                        modifier = basicModifier,
                        onClick = {
                            onNextButtonClick()
                        }
                    ) {
                        Text("完成調查")
                    }
                }
            }
        }
    }
}

@Composable
fun DialogHeader(header: String) {
    Text(
        text = header,
        modifier = basicModifier,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    )
}