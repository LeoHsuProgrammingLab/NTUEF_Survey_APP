package com.example.ntufapp.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.basicModifier

@Composable
fun SurveyConfirmDialog( // Survey Screen
    dbhSetSize: Int,
    measHtSetSize: Int,
    visHtSetSize: Int,
    forkHtSetSize: Int,
    speciesSetSize: Int = 0,
    conditionSetSize: Int = 0,
    surveyType: String = "ReSurvey",
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    onNextButtonClick: () -> Unit
){
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(shape = Shapes.small) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogHeader(header = "您還有以下未調查樣樹，確認完成嗎？")

                Column {
                    Text(text = "DBH還有${dbhSetSize}棵樹未調查")
                    Text("樹高還有${measHtSetSize}棵樹未調查")
                    Text("目視樹高還有${visHtSetSize}棵樹未調查")
                    Text("分岔樹高還有${forkHtSetSize}棵樹未調查")

                    if (surveyType == "NewSurvey") {
                        Text("樹種還有${speciesSetSize}棵樹未調查")
                        Text("生長狀況還有${conditionSetSize}棵樹未調查")
                    }
                }

                Spacer(modifier = basicModifier)

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = basicModifier,
                        onClick = onCancelClick,
                    ) {
                        Text("繼續調查")
                    }

                    Button(
                        modifier = basicModifier,
                        onClick = onNextButtonClick
                    ) {
                        Text("完成調查")
                    }
                }
            }
        }
    }
}