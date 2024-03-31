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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.R
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.basicModifier

@Composable
// https://medium.com/@rooparshkalia/single-choice-dialog-with-jetpack-compose-d021650d31ca
fun ConfirmDialog( // Plot Options Screen
    metaData: PlotData,
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    onNextButtonClick: (PlotData) -> Unit
){
    Dialog(
        onDismissRequest = { onDismiss.invoke() }
    ) {
        Surface(shape = Shapes.small) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogHeader(
                    header = "您已匯入${metaData.ManageUnit}${metaData.SubUnit}的資料\n樣區名稱：${metaData.PlotName}\n樣區編號：${metaData.PlotNum}\n該樣區有${metaData.PlotTrees.size}棵樣樹"
                )
                Spacer(basicModifier)
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // cancel button
                    Button(
                        modifier = basicModifier,
                        onClick = onCancelClick,
                    ) {
                        Text(stringResource(id = (R.string.cancel)))
                    }

                    // next button
                    Button(
                        modifier = basicModifier,
                        onClick = { onNextButtonClick(metaData) }
                    ) {
                        Text(stringResource(id = (R.string.next)))
                    }
                }
            }
        }
    }
}
