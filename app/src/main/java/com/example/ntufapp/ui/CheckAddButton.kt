package com.example.ntufapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CheckAddButton(
    dbhSet: MutableState<MutableSet<String>>,
    htSet: MutableState<MutableSet<String>>,
    visHtSet: MutableState<MutableSet<String>>,
    measHtSet: MutableState<MutableSet<String>>,
    onNextButtonClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        val showDialogue = remember {
            mutableStateOf(false)
        }

        OutlinedButton(
            onClick = {
                if (dbhSet.value.size == 0 || htSet.value.size == 0 || visHtSet.value.size == 0 || measHtSet.value.size == 0) {
                    onNextButtonClick()
                } else {
                    showDialogue.value = true
                }
            },
            modifier = Modifier
                .padding(end = 16.dp)
                .height(70.dp)
        ) {
            Text("完成此次調查", fontSize = 20.sp)
        }

        if (showDialogue.value) {
            surveyConfirmDialogue(
                dbhSetSize = dbhSet.value.size,
                htSetSize = htSet.value.size,
                visHtSetSize = visHtSet.value.size,
                measHtSetSize = measHtSet.value.size,
                onDismiss = { showDialogue.value = false },
                onCancelClick = { showDialogue.value = false },
                onNextButtonClick = onNextButtonClick
            )
        }
    }
}