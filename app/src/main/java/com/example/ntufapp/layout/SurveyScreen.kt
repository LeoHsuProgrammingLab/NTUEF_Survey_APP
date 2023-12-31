package com.example.ntufapp.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ntufapp.data.ntufappInfo.Companion.defaultTreeNum
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.CheckAddButton
import com.example.ntufapp.ui.HtDBHView
import com.example.ntufapp.ui.MetaDateView
import com.example.ntufapp.ui.SpeciesConditionView
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
    val totalTreesNumList = remember { mutableStateListOf<String>(*treeNumList.toTypedArray()) }

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
    newPlotDataState.value.initPlotTrees()

    // Save the tree numbers in ReSurvey Layout
    val treeNumList = mutableListOf<String>()
    for (i in 0 until defaultTreeNum) {
        treeNumList.add((i + 1).toString())
    }
    // Save every tree's number in ReSurvey Layout
    val totalTreesNumList = remember { mutableStateListOf(*treeNumList.toTypedArray()) }

    SurveyView(totalTreesNumList = totalTreesNumList, newPlotData = newPlotDataState.value, onNextButtonClick = onNextButtonClick)

}

@Preview(device = Devices.PIXEL_C)
@Composable
fun PreviewMyApp() {

}




