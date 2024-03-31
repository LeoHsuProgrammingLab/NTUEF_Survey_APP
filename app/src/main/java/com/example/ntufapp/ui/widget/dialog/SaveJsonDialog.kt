package com.example.ntufapp.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.R
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.basicModifier

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
