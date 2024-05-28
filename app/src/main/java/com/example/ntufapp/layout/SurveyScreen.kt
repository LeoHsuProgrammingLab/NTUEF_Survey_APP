package com.example.ntufapp.layout

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.ntufapp.data.ntufappInfo.Companion.defaultTreeNum
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.widget.SurveyView
import com.example.ntufapp.utils.showMessage

//ref: https://www.youtube.com/watch?v=8XJfLaAOxD0&ab_channel=AndroidDevelopers
// live-edit for compose

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
    val context = LocalContext.current
//    DisableBackButtonHandler(from = "NewSurveyScreen")
    BackHandler(enabled = true) {}

    // Save the final return data
    val newPlotDataState = remember { mutableStateOf(newPlotData) }
    newPlotDataState.value.setToday()
    // Init the tree number if there is no tree data in the plot
    if (newPlotDataState.value.PlotTrees.isEmpty()) {
        showMessage(context,"There are no tree data in the plot, use default tree number $defaultTreeNum.")
        newPlotDataState.value.initPlotTrees(defaultTreeNum)
    }
    // Store all the tree numbers in ReSurvey Layout
    val totalTreesNumList = remember { mutableStateListOf(*List(newPlotData.PlotTrees.size){ (it+1).toString() }.toTypedArray()) }
    SurveyView(totalTreesNumList = totalTreesNumList, newPlotData = newPlotDataState.value, onNextButtonClick = onNextButtonClick)
}

@Preview(device = Devices.PIXEL_C)
@Composable
fun PreviewMyApp() {

}




