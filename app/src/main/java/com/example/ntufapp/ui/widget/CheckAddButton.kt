package com.example.ntufapp.ui.widget

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
import com.example.ntufapp.ui.widget.dialog.SurveyConfirmDialog

@Composable
fun CheckAddButton(
    dbhSet: MutableState<MutableSet<String>>,
    measHtSet: MutableState<MutableSet<String>>,
    visHtSet: MutableState<MutableSet<String>>,
    forkHtSet: MutableState<MutableSet<String>>,
    speciesSet: MutableState<MutableSet<String>>,
    conditionSet: MutableState<MutableSet<String>>,
    surveyType: String = "ReSurvey",
    onNextButtonClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        val showDialog = remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = {
                if (dbhSet.value.size == 0 || measHtSet.value.size == 0 || visHtSet.value.size == 0 || forkHtSet.value.size == 0) {
                    onNextButtonClick()
                } else {
                    showDialog.value = true
                }
            },
            modifier = Modifier
                .padding(top = 10.dp, end = 30.dp)
                .height(50.dp)
        ) {
            Text("完成此次調查", fontSize = 18.sp)
        }

        if (showDialog.value) {
            if (surveyType == "NewSurvey") {
                SurveyConfirmDialog(
                    dbhSetSize = dbhSet.value.size,
                    measHtSetSize = measHtSet.value.size,
                    visHtSetSize = visHtSet.value.size,
                    forkHtSetSize = forkHtSet.value.size,
                    speciesSetSize = speciesSet.value.size,
                    conditionSetSize = conditionSet.value.size,
                    surveyType = surveyType,
                    onDismiss = {},
                    onCancelClick = { showDialog.value = false },
                    onNextButtonClick = onNextButtonClick
                )
            } else {
                SurveyConfirmDialog(
                    dbhSetSize = dbhSet.value.size,
                    measHtSetSize = measHtSet.value.size,
                    visHtSetSize = visHtSet.value.size,
                    forkHtSetSize = forkHtSet.value.size,
                    onDismiss = {},
                    onCancelClick = { showDialog.value = false },
                    onNextButtonClick = onNextButtonClick
                )
            }
        }
    }
}