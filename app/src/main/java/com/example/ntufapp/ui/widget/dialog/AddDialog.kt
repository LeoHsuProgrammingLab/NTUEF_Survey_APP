package com.example.ntufapp.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.R
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.basicModifier

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