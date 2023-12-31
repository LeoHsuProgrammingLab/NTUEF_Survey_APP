package com.example.ntufapp.ui

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.data.DataSource
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.Shapes

@Composable
fun UploadFileDialog( // Plot Options Screen
    selectedFileUri: Uri?,
    onDismiss: () -> Unit,
    filePicker: ManagedActivityResultLauncher<String, Uri?>,
    onSendClick: (Uri?) -> Unit,
    onCancelClick: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        Surface(shape = Shapes.small) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "請上傳樣區資料")

                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        filePicker.launch("application/json")
                    }
                ) {
                    Text("請選擇JSON檔案")
                }

                Spacer(modifier = Modifier.padding(10.dp))
                Row{
                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick = onCancelClick,
                    ) {
                        Text("取消")
                    }

                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick = { onSendClick(selectedFileUri) }
                    ) {
                        Text("上傳")
                    }
                }
            }
        }
    }
}

@Composable
// https://medium.com/@rooparshkalia/single-choice-dialog-with-jetpack-compose-d021650d31ca
fun ConfirmDialogue( // Plot Options Screen
    metaData: PlotData,
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    onNextButtonClick: (PlotData) -> Unit
){
    Dialog(
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        val context = LocalContext.current

        Surface(shape = Shapes.small) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "您已上傳${metaData.ManageUnit}${metaData.SubUnit}的資料")
                Text("樣區名稱：${metaData.PlotName}")
                Text("樣區編號：${metaData.PlotNum}")
                Text("該樣區有${metaData.PlotTrees.size}棵樣樹")

                Spacer(modifier = Modifier.padding(10.dp))
                Row{
                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick = onCancelClick,
                    ) {
                        Text("取消")
                    }
                    
                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick = { onNextButtonClick(metaData) }
                    ) {
                        Text("下一步")
                    }
                }
            }
        }
    }
}

@Composable
fun NewSurveyUploadChoiceDialogue(
    onDismiss: () -> Unit,
    onUploadTypeClick: (String) -> Unit,
){
    Dialog(
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        Surface(shape = Shapes.small) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "請選擇輸入新樣區資料之方式")

                Spacer(modifier = Modifier.padding(10.dp))
                Row{
                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick = { onUploadTypeClick("Manual") }
                    ) {
                        Text("手動輸入")
                    }

                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick = { onUploadTypeClick("JSON") }
                    ) {
                        Text("上傳JSON檔")
                    }
                }
            }
        }
    }
}

@Composable
fun ManualInputNewPlotDialogue(
    onDismiss: () -> Unit,
    onSendClick: (PlotData) -> Unit,
) {
     val plotData = remember {
        mutableStateOf(PlotData())
    }

    Dialog(
        onDismissRequest = {
            onDismiss.invoke()
        }
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
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "請輸入樣區資料")
                TextField(value = manageUnit.value, onValueChange = { text: String -> manageUnit.value = text }, label = { Text("營林區")})
                TextField(value = subUnit.value, onValueChange = { text: String -> subUnit.value = text }, label = { Text("林班地")})
                TextField(value = plotName.value, onValueChange = { text: String -> plotName.value = text }, label = { Text("樣區名稱")})
                TextField(value = plotNum.value, onValueChange = { text: String -> plotNum.value = text }, label = { Text("樣區編號")})
                TextField(value = plotType.value, onValueChange = { text: String -> plotType.value = text }, label = { Text("樣區型態")})
                TextField(value = plotArea.value, onValueChange = { text: String -> plotArea.value = text }, label = { Text("樣區面積")})
                TextField(value = altitude.value, onValueChange = { text: String -> altitude.value = text }, label = { Text("樣區海拔")})
                TextField(value = slope.value, onValueChange = { text: String -> slope.value = text }, label = { Text("樣區坡度")})
                TextField(value = aspect.value, onValueChange = { text: String -> aspect.value = text }, label = { Text("樣區坡向")})
                TextField(value = TWD97_X.value, onValueChange = { text: String -> TWD97_X.value = text }, label = { Text("TWD97_X")})
                TextField(value = TWD97_Y.value, onValueChange = { text: String -> TWD97_Y.value = text }, label = { Text("TWD97_Y")})

                Row{
                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick ={
                            plotData.value.ManageUnit = manageUnit.value
                            plotData.value.SubUnit = subUnit.value
                            plotData.value.PlotName = plotName.value
                            plotData.value.PlotNum = plotNum.value.toInt()
                            plotData.value.PlotType = plotType.value
                            plotData.value.PlotArea = plotArea.value.toDouble()
                            plotData.value.Altitude = altitude.value.toDouble()
                            plotData.value.Slope = slope.value.toDouble()
                            plotData.value.Aspect = aspect.value
                            plotData.value.TWD97_X = TWD97_X.value
                            plotData.value.TWD97_Y = TWD97_Y.value
                            onSendClick(plotData.value)
                        }
                    ) {
                        Text("下一步")
                    }
                }
            }
        }
    }
}


@Composable
fun AdjustSpeciesDialogue( // Survey Screen
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    onNextButtonClick: (String) -> Unit
){
    val treeSpecies = remember {
        mutableStateOf("")
    }

    Dialog(
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        Surface(shape = Shapes.small) {
            Column(modifier = Modifier.padding(10.dp)) {
                SearchableDropdownMenu2(
                    optionsInput = DataSource.SpeciesList as MutableList<String>,
                    onChoose = {
                        treeSpecies.value = it
                    }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Row{
                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick = onCancelClick,
                    ) {
                        Text("取消")
                    }

                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick = {
                            if(treeSpecies.value != "") {
                                onNextButtonClick(treeSpecies.value)
                            }
                        }
                    ) {
                        Text("修改")
                    }
                }
            }
        }
    }
}

@Composable
fun SurveyConfirmDialogue( // Survey Screen
    dbhSetSize: Int,
    measHtSetSize: Int,
    visHtSetSize: Int,
    forkHtSetSize: Int,
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
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "DBH還有${dbhSetSize}棵樹未調查")
                Text("樹高還有${measHtSetSize}棵樹未調查")
                Text("目視樹高還有${visHtSetSize}棵樹未調查")
                Text("分岔樹高還有${forkHtSetSize}棵樹未調查")
                Text("請確認是否要完成此次調查?", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))

                Spacer(modifier = Modifier.padding(10.dp))

                Row{
                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick = onCancelClick,
                    ) {
                        Text("繼續調查")
                    }

                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick = {
                            //TODO: Save the data
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