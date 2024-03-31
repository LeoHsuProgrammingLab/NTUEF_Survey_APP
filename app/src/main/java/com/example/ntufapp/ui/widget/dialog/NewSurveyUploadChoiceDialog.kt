package com.example.ntufapp.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.R
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.basicModifier

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
