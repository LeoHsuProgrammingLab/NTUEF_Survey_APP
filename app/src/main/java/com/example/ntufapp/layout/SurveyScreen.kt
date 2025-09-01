package com.example.ntufapp.layout

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.widget.SurveyView

@Composable
fun ReSurveyScreen(
    newPlotData: PlotData,
    onNextButtonClick: () -> Unit,
) {
    BackHandler(enabled = true) {}

    // Save the final return data
    val newPlotDataState = remember { mutableStateOf(newPlotData) }
    newPlotDataState.value.setToday()
    // Store all the tree numbers in ReSurvey Layout
    val totalTreesNumList = remember { mutableStateListOf(*List(newPlotData.PlotTrees.size){ (it+1).toString() }.toTypedArray()) }
    SurveyView(totalTreesNumList = totalTreesNumList, newPlotData = newPlotDataState.value, onNextButtonClick = onNextButtonClick)
}

@Composable
fun NewSurveyScreen(
    newPlotData: PlotData,
    onNextButtonClick: () -> Unit
) {
    BackHandler(enabled = true) {}

    // Save the final return data
    val newPlotDataState = remember { mutableStateOf(newPlotData) }
    newPlotDataState.value.setToday()

    // Store all the tree numbers in ReSurvey Layout
    val totalTreesNumList = remember { mutableStateListOf(*List(newPlotData.PlotTrees.size){ (it+1).toString() }.toTypedArray()) }
    SurveyView(totalTreesNumList = totalTreesNumList, newPlotData = newPlotDataState.value, onNextButtonClick = onNextButtonClick)
}

@Preview(device = Devices.PIXEL_C)
@Composable
fun PreviewMyApp() {

}




