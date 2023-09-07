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
import androidx.compose.runtime.Composable
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
import com.example.ntufapp.layout.showMessage
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.Shapes

@Composable
fun UploadFileDialog(
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
                        onClick = {onSendClick(selectedFileUri)}
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
fun ConfirmDialogue(
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

//                for (i in 0 until metaData.PlotTrees.size) {
//                    showMessage(context, "Read DBH = ${metaData.PlotTrees[i].DBH}")
//                }

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
fun FixSpeciesDialogue(
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
fun surveyConfirmDialogue(
    dbhSetSize: Int,
    htSetSize: Int,
    visHtSetSize: Int,
    measHtSetSize: Int,
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
                Text("樹高還有${htSetSize}棵樹未調查")
                Text("目視樹高還有${visHtSetSize}棵樹未調查")
                Text("分岔樹高還有${measHtSetSize}棵樹未調查")
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