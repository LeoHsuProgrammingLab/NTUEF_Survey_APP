package com.example.ntufapp.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.model.Tree
import com.example.ntufapp.ui.CheckAddButton
import com.example.ntufapp.ui.MetaDateView
import com.example.ntufapp.ui.PlotTreeView
import com.example.ntufapp.ui.InputProgressView
import com.example.ntufapp.ui.InputProgressView2

//ref: https://www.youtube.com/watch?v=8XJfLaAOxD0&ab_channel=AndroidDevelopers
// live-edit for compose

@Composable
fun ReSurveyScreen(
    onNextButtonClick: (Int) -> Unit,
    oldPlotData: PlotData,
    newPlotData: PlotData
) {
    val contextNow = LocalContext.current
    // Save the final return data
    val newPlotDataState = remember {
        mutableStateOf(newPlotData)
    }
    
    newPlotDataState.value.setToday()

    // Save the tree numbers in ReSurvey Layout
    val treeNumList = mutableListOf<String>()
    for (i in 0 until newPlotData.PlotTrees.size) {
        treeNumList.add((i + 1).toString())
    }

    val totalTreesState = remember {
        mutableStateOf(treeNumList)
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        MetaDateView(newPlotData = newPlotDataState.value)
        Box(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(450.dp)
        ) {

            val curTreeNum = remember {
                mutableStateOf("1")
            }

            Row {

                PlotTreeView(curTreeNum = curTreeNum, totalTreesState = totalTreesState.value, newPlotData = newPlotDataState.value)

//                InputProgressView(
//                    curTreeNum.value,
//                    newPlotDataState.value
//                )
                InputProgressView2(
                    totalTreesState.value,
                    newPlotDataState.value
                )
            }

        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ){
            CheckAddButton()
        }
    }
}

@Composable
fun checkDialogue(treeData: Tree, onDismiss: () -> Unit) {

}

@Preview(device = Devices.PIXEL_C)
@Composable
fun PreviewMyApp() {

}




