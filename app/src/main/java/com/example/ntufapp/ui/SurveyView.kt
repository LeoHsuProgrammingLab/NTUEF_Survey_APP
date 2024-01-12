package com.example.ntufapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.example.ntufapp.model.PlotData

@Composable
fun SurveyView(
    totalTreesNumList: MutableList<String>,
    newPlotData: PlotData,
    onNextButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MetaDateView(newPlotData = newPlotData)
        val currentTreeNum = remember { mutableStateOf("1") }
        val numPlotTrees = remember { mutableIntStateOf(totalTreesNumList.size) }
        val dbhTreeSet = remember { mutableStateOf(totalTreesNumList.toMutableSet()) }
        val measHtTreeSet = remember { mutableStateOf(totalTreesNumList.toMutableSet()) }
        val forkHtTreeSet = remember { mutableStateOf(totalTreesNumList.toMutableSet()) }
        val visHtTreeSet = remember { mutableStateOf(totalTreesNumList.toMutableSet()) }

        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SpeciesConditionView(
                totalTreesNumList = totalTreesNumList,
                currentTreeNum = currentTreeNum,
                newPlotData = newPlotData
            )
            HtDBHView(
                totalTreesNumList = totalTreesNumList,
                numPlotTrees = numPlotTrees,
                dbhTreeSet = dbhTreeSet,
                measHtTreeSet = measHtTreeSet,
                forkHtTreeSet = forkHtTreeSet,
                visHtTreeSet = visHtTreeSet,
                newPlotData
            )
        }
        CheckAddButton(dbhSet = dbhTreeSet, measHtSet = measHtTreeSet, visHtSet = visHtTreeSet, forkHtSet = forkHtTreeSet) {
            onNextButtonClick()
        }
    }
}