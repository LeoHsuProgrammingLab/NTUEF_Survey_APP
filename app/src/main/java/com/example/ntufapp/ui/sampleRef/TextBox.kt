package com.example.ntufapp.ui.sampleRef

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.layout.showMessage

@Composable
fun TextBox (
    label: String,
    defaultValue: String = "",
    readOnly: Boolean = false,
    keyboardTypeInput: KeyboardType = KeyboardType.Number,
    boxAlign: Alignment = Alignment.TopStart,
){
    val textFieldValue = remember {
        mutableStateOf(defaultValue)
    }

    Row(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Box(
            contentAlignment = boxAlign,
            modifier = Modifier.size(width = 100.dp, height = 100.dp)
        ) {
            Text(label, modifier = Modifier.padding(end = 0.dp), textAlign = TextAlign.Justify, fontSize = 20.sp)
        }

        OutlinedTextField(
            value = textFieldValue.value,
            onValueChange = {
                textFieldValue.value = it
            },
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardTypeInput, imeAction = ImeAction.Done),
            modifier = Modifier
                .padding(start = 5.dp, end = 25.dp)
                .size(width = 120.dp, height = 50.dp)
        )
    }
}