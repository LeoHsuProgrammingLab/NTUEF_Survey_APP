package com.example.ntufapp.layout

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.R
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.Shapes
import com.google.gson.Gson
import java.io.BufferedReader

@Composable
fun PlotOptionsScreen(
    onNextButtonClick: (PlotData) -> Unit
) {
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
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        Row{
            val showDialog = remember { mutableStateOf(false) }
            val selectedFileUri = remember { mutableStateOf<Uri?>(null) }

            val filePickerLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()) { uri: Uri? ->
                selectedFileUri.value = uri
            }

            val showUploadData = remember {
                mutableStateOf(false)
            }

            val oldPlotData = remember {
                mutableStateOf(PlotData())
            }

            val context = LocalContext.current

            Button(modifier = Modifier.padding(end = 20.dp),
                onClick = {
                    showDialog.value = true
                }
            ) {
                Text("複查樣區", fontSize = 20.sp)
            }

            if(showDialog.value) {
                UploadFileDialog(
                    selectedFileUri = selectedFileUri.value,
                    onDismiss = { showDialog.value = false },
                    onSendClick = { uri ->
                        if(uri != null) {
                            selectedFileUri.value = uri
                            oldPlotData.value  = parseJsonToMetaData(uri, context)!!
                            if(oldPlotData.value.ManageUnit == "") {
                                Toast.makeText(context, "你上傳了錯誤的檔案(.json)！", Toast.LENGTH_SHORT).show()
                            } else {
                                showUploadData.value = true
                            }
                        }
                        showDialog.value = false
                    },
                    onCancelClick = { showDialog.value = false },
                    filePicker = filePickerLauncher
                )
            }

            if(showUploadData.value) {
                ConfirmDialogue(
                    metaData = oldPlotData.value,
                    onDismiss = { showUploadData.value = false },
                    onCancelClick = {showUploadData.value = false},
                    onNextButtonClick = {onNextButtonClick(oldPlotData.value)}
                )
            }

            Button(
                onClick = { /*TODO Add a new plot*/ }
            ) {
                Text("新增樣區", fontSize = 20.sp)
            }
        }
    }
}

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
    onNextButtonClick: () -> Unit
){
    Dialog(
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
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
                        onClick = {onNextButtonClick()}
                    ) {
                        Text("下一步")
                    }
                }
            }
        }
    }
}

fun showMessage(context: Context, s: String) {
    Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
}

fun parseJsonToMetaData(uri: Uri, context: Context): PlotData? {
    val inputStream = context.contentResolver.openInputStream(uri)
    val jsonString = inputStream?.bufferedReader()?.use(BufferedReader::readText)
    return try {
        Gson().fromJson(jsonString, PlotData::class.java)
    } catch (e: Exception) {
        // Handle parsing errors here
        null
    } finally {
        // Close the InputStream after use
        inputStream?.close()
    }
}
