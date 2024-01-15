package com.example.ntufapp.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        val dbhTreeSet = remember { mutableStateOf(totalTreesNumList.toMutableSet()) }
        val measHtTreeSet = remember { mutableStateOf(totalTreesNumList.toMutableSet()) }
        val forkHtTreeSet = remember { mutableStateOf(totalTreesNumList.toMutableSet()) }
        val visHtTreeSet = remember { mutableStateOf(totalTreesNumList.toMutableSet()) }
        val speciesTreeSet = remember { mutableStateOf(totalTreesNumList.toMutableSet()) }
        val conditionTreeSet = remember { mutableStateOf(totalTreesNumList.toMutableSet()) }

        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SpeciesConditionView(
                totalTreesNumList = totalTreesNumList,
                currentTreeNum = currentTreeNum,
                speciesTreeSet = speciesTreeSet,
                conditionTreeSet = conditionTreeSet,
                newPlotData = newPlotData
            )
            HtDBHView(
                totalTreesNumList = totalTreesNumList,
                dbhTreeSet = dbhTreeSet,
                measHtTreeSet = measHtTreeSet,
                forkHtTreeSet = forkHtTreeSet,
                visHtTreeSet = visHtTreeSet,
                newPlotData = newPlotData
            )
        }

        CheckAddButton(
            dbhSet = dbhTreeSet,
            measHtSet = measHtTreeSet,
            visHtSet = visHtTreeSet,
            forkHtSet = forkHtTreeSet
        ) {
            onNextButtonClick()
        }
    }
}