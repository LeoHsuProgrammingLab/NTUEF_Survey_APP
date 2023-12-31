package com.example.ntufapp.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ntufapp.data.ntufappInfo.Companion.defaultTreeNum
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.CheckAddButton
import com.example.ntufapp.ui.HtDBHView
import com.example.ntufapp.ui.MetaDateView
import com.example.ntufapp.ui.PlotTreeView

//ref: https://www.youtube.com/watch?v=8XJfLaAOxD0&ab_channel=AndroidDevelopers
// live-edit for compose

@Composable
fun ReSurveyScreen(
    onNextButtonClick: () -> Unit,
    oldPlotData: PlotData,
    newPlotData: PlotData
) {
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

    val totalTreesState = remember { mutableStateOf(treeNumList) }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        MetaDateView(newPlotData = newPlotDataState.value)

        val curTreeNum = remember { mutableStateOf("1") }
        val numPlotTrees = remember { mutableStateOf(totalTreesState.value.size) }
        val dbhTreeSet = remember { mutableStateOf(totalTreesState.value.toMutableSet()) }
        val measHtTreeSet = remember { mutableStateOf(totalTreesState.value.toMutableSet())}
        val forkHtTreeSet = remember { mutableStateOf(totalTreesState.value.toMutableSet()) }
        val visHtTreeSet = remember { mutableStateOf(totalTreesState.value.toMutableSet()) }

        if (newPlotDataState.value.PlotTrees.size > numPlotTrees.value) {
            for (i in newPlotDataState.value.PlotTrees.size downTo numPlotTrees.value + 1) {
                dbhTreeSet.value.add(i.toString())
                visHtTreeSet.value.add(i.toString())
                forkHtTreeSet.value.add(i.toString())
                measHtTreeSet.value.add(i.toString())
            }
            numPlotTrees.value = newPlotDataState.value.PlotTrees.size
        }

        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PlotTreeView(curTreeNum = curTreeNum, totalTreesState = totalTreesState.value, newPlotData = newPlotDataState.value)
            HtDBHView(
                numPlotTrees = numPlotTrees,
                dbhTreeSet = dbhTreeSet,
                measHtTreeSet = measHtTreeSet,
                forkHtTreeSet = forkHtTreeSet,
                visHtTreeSet = visHtTreeSet,
                newPlotDataState.value, onNextButtonClick
            )
        }
        CheckAddButton(dbhSet = dbhTreeSet, measHtSet = measHtTreeSet, visHtSet = visHtTreeSet, forkHtSet = forkHtTreeSet) {
            onNextButtonClick()
        }
    }
}

@Composable
fun NewSurveyScreen(
    onNextButtonClick: () -> Unit,
    newPlotData: PlotData
) {
    // Save the final return data
    val newPlotDataState = remember { mutableStateOf(newPlotData) }

    newPlotDataState.value.setToday()

    // Save the tree numbers in ReSurvey Layout
    val treeNumList = mutableListOf<String>()
    for (i in 0 until defaultTreeNum) {
        treeNumList.add((i + 1).toString())
    }

    val totalTreesState = remember {
        mutableStateOf(treeNumList)
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        MetaDateView(newPlotData = newPlotDataState.value)
        val curTreeNum = remember { mutableStateOf("1") }
        val numPlotTrees = remember { mutableStateOf(totalTreesState.value.size) }
        val dbhTreeSet = remember { mutableStateOf(totalTreesState.value.toMutableSet()) }
        val measHtTreeSet = remember { mutableStateOf(totalTreesState.value.toMutableSet())}
        val forkHtTreeSet = remember { mutableStateOf(totalTreesState.value.toMutableSet()) }
        val visHtTreeSet = remember { mutableStateOf(totalTreesState.value.toMutableSet()) }

        if (newPlotDataState.value.PlotTrees.size > numPlotTrees.value) {
            for (i in newPlotDataState.value.PlotTrees.size downTo numPlotTrees.value + 1) {
                dbhTreeSet.value.add(i.toString())
                visHtTreeSet.value.add(i.toString())
                forkHtTreeSet.value.add(i.toString())
                measHtTreeSet.value.add(i.toString())
            }
            numPlotTrees.value = newPlotDataState.value.PlotTrees.size
        }

        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PlotTreeView(curTreeNum = curTreeNum, totalTreesState = totalTreesState.value, newPlotData = newPlotDataState.value)
            HtDBHView(
                numPlotTrees = numPlotTrees,
                dbhTreeSet = dbhTreeSet,
                measHtTreeSet = measHtTreeSet,
                forkHtTreeSet = forkHtTreeSet,
                visHtTreeSet = visHtTreeSet,
                newPlotDataState.value, onNextButtonClick
            )
        }
        CheckAddButton(dbhSet = dbhTreeSet, measHtSet = measHtTreeSet, visHtSet = visHtTreeSet, forkHtSet = forkHtTreeSet) {
            onNextButtonClick()
        }
    }
}

@Preview(device = Devices.PIXEL_C)
@Composable
fun PreviewMyApp() {

}




