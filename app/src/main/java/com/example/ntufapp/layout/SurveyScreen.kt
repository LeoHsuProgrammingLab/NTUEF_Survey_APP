package com.example.ntufapp.layout

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.ntufapp.data.ntufappInfo.Companion.dTag
import com.example.ntufapp.data.ntufappInfo.Companion.defaultTreeNum
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.SurveyView

//ref: https://www.youtube.com/watch?v=8XJfLaAOxD0&ab_channel=AndroidDevelopers
// live-edit for compose

@Composable
fun ReSurveyScreen(
    oldPlotData: PlotData,
    newPlotData: PlotData,
    onNextButtonClick: () -> Unit,
) {
    // Save the final return data
    val newPlotDataState = remember { mutableStateOf(newPlotData) }
    newPlotDataState.value.setToday()
    val treeNumList = mutableListOf<String>()
    for (i in 0 until newPlotData.PlotTrees.size) {
        treeNumList.add((i + 1).toString())
    }
    // Save every tree's number in ReSurvey Layout
    val totalTreesNumList = remember { mutableStateListOf(*treeNumList.toTypedArray()) }

    SurveyView(totalTreesNumList = totalTreesNumList, newPlotData = newPlotDataState.value, onNextButtonClick = onNextButtonClick)
}

@Composable
fun NewSurveyScreen(
    newPlotData: PlotData,
    onNextButtonClick: () -> Unit
) {
    // Save the final return data
    val newPlotDataState = remember { mutableStateOf(newPlotData) }

    newPlotDataState.value.setToday()

    // Save every tree's number in ReSurvey Layout
    val treeNumList = mutableListOf<String>()
    if (newPlotDataState.value.PlotTrees.size == 0) {
        newPlotDataState.value.initPlotTrees()
        for (i in 0 until defaultTreeNum) {
            treeNumList.add((i + 1).toString())
        }
    }

    val totalTreesNumList = remember { mutableStateListOf(*treeNumList.toTypedArray()) }
    Log.d(dTag, "NewSurveyScreen: totalTreesNumList: ${totalTreesNumList.size}")

    SurveyView(totalTreesNumList = totalTreesNumList, newPlotData = newPlotDataState.value, onNextButtonClick = onNextButtonClick)
}

@Preview(device = Devices.PIXEL_C)
@Composable
fun PreviewMyApp() {

}




