package com.example.ntufapp.ui.widget.dialog

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.R
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.basicModifier
import com.example.ntufapp.utils.showMessage

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
