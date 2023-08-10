package com.example.ntufapp.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@Composable
fun InputBlock(
    onAddDBH: (Double)->Unit,
    onAddVisHeight: (Double)->Unit,
    onAddHeight: (Double)->Unit,
    onAddForkHeight: (Double)->Unit
) {
    Column(
        modifier = Modifier.width(320.dp)
    ) {
        InputRow(label = "DBH", onAddDBH)
        InputRow(label = "目視樹高", onAddVisHeight)
        InputRow(label = "量測樹高", onAddHeight)
        InputRow(label = "分岔樹高", onAddForkHeight)
    }
}

@Composable
fun InputRow(
    label: String,
    onClick: (Double)->Unit
) {

    val inputState = remember {
        mutableStateOf("")
    }
    Box(
        modifier = Modifier.padding(10.dp)
    ) {
        Row(){
            TextField(
                value = inputState.value,
                onValueChange = {
                    inputState.value = it
                },
                readOnly = false,
                label = { Text(label) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 10.dp)
            )
            Button(
                modifier = Modifier
                    .width(90.dp)
                    .padding(top = 15.dp, start = 5.dp),
                onClick = {
                    onClick(inputState.value.toDouble())
                }
            ) {
                Text("新增")
            }
        }
    }
}
