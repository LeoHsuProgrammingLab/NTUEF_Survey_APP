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
    filePicker: ManagedActivityResultLauncher<String, Uri?>,
    header: String,
    mainButtonText: String,
    nextButtonText: String,
    cancelButtonText: String = stringResource(id = (R.string.cancel)),
    onDismiss: () -> Unit,
    onSendClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss.invoke() }
    ) {
        Surface(shape = Shapes.small) {
            Column(
                modifier = basicModifier,
            ) {
                DialogHeader(header = header)
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
                        Text(mainButtonText)
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
                        Text(cancelButtonText)
                    }

                    Button(
                        modifier = basicModifier,
                        onClick = onSendClick
                    ) {
                        Text(nextButtonText)
                    }
                }
            }
        }
    }
}
