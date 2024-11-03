package com.example.ntufapp.ui.widget

import android.util.Log
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
import androidx.compose.ui.unit.dp
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.basicModifier

@Composable
fun SurveyView(
    totalTreesNumList: MutableList<String>,
    newPlotData: PlotData,
    onNextButtonClick: () -> Unit
) {
    Column(
        modifier = basicModifier.fillMaxWidth(),
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

        updateUnaddressedSet(
            dbhSet = dbhTreeSet,
            measHtSet = measHtTreeSet,
            visHtSet = visHtTreeSet,
            forkHtSet = forkHtTreeSet,
            speciesSet = speciesTreeSet,
            conditionSet = conditionTreeSet,
            newPlotData = newPlotData
        )

        Row(
            modifier = basicModifier.fillMaxWidth(),
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
            forkHtSet = forkHtTreeSet,
            speciesSet = speciesTreeSet,
            conditionSet = conditionTreeSet,
            surveyType = "NewSurvey",
        ) {
            onNextButtonClick()
        }
    }
}

fun updateUnaddressedSet(
    dbhSet: MutableState<MutableSet<String>>,
    measHtSet: MutableState<MutableSet<String>>,
    visHtSet: MutableState<MutableSet<String>>,
    forkHtSet: MutableState<MutableSet<String>>,
    speciesSet: MutableState<MutableSet<String>>,
    conditionSet: MutableState<MutableSet<String>>,
    newPlotData: PlotData
) {
    for (tree in newPlotData.PlotTrees) {
        if (tree.DBH != 0.0) {
            dbhSet.value.remove(tree.SampleNum.toString())
        }
        if (tree.MeasHeight != 0.0) {
            measHtSet.value.remove(tree.SampleNum.toString())
        }
        if (tree.VisHeight != 0.0) {
            visHtSet.value.remove(tree.SampleNum.toString())
        }
        if (tree.ForkHeight != 0.0) {
            forkHtSet.value.remove(tree.SampleNum.toString())
        }
        if (tree.Species != "") {
            Log.d("species", "${tree.Species}")
            speciesSet.value.remove(tree.SampleNum.toString())
        }
        if (tree.State.isNotEmpty()) {
            Log.d("treeState", "${tree.State.size} ${tree.State}")
            conditionSet.value.remove(tree.SampleNum.toString())
        }
    }
}