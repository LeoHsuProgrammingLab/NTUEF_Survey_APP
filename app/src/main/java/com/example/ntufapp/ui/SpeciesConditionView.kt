package com.example.ntufapp.ui

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.R
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.model.Tree
import com.example.ntufapp.ui.theme.IntervalDivider
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.lightBorder

@Composable
fun SpeciesConditionView(
    currentTreeNum: MutableState<String>,
    totalTreesNumList: MutableList<String>,
    newPlotData: PlotData
) {
    val currentTree = remember { mutableStateOf(newPlotData.searchTree(currentTreeNum.value.toInt())) }
    val modifier = Modifier.width(450.dp)
    // Adjust the height
    val windowInfo = rememberWindowInfo()
    val height =  when (windowInfo.screenHeightInfo) {
        WindowInfo.WindowType.Compact -> 300.dp
        WindowInfo.WindowType.Medium -> 350.dp
        WindowInfo.WindowType.Expanded -> 420.dp
        else -> 400.dp
    }

    Box(
        modifier = Modifier
            .padding(10.dp)
            .height(height)
            .border(lightBorder, Shapes.medium),
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchableAddMenu(
                    totalTreesNumList,
                    label = "請選擇樣樹",
                    defaultString = "1",
                    keyboardType = KeyboardType.Number,
                    onChoose = {
                        currentTreeNum.value = it
                        currentTree.value = newPlotData.searchTree(currentTreeNum.value.toInt())
                    },
                    onAdd = {
                        newPlotData.PlotTrees.add(Tree(SampleNum = it.toInt()))
                        currentTreeNum.value = it
                        currentTree.value = newPlotData.searchTree(currentTreeNum.value.toInt())
                        Log.i(
                            "PlotTreeView",
                            "newPlotData.getPlotTreesSize(): ${newPlotData.PlotTrees.size}"
                        )
                    },
                    modifier = Modifier
                        .padding(5.dp)
                        .width(180.dp)
                        .height(75.dp)
                )
                TreeSpeciesWidget(currentTree = currentTree.value!!)
            }
            TreeConditionChips(
                currentTreeNum = currentTree.value!!,
                reRenderCurrentTree = { it.clear() },
                modifier = modifier
            )

        }
    }
}

@Composable
fun TreeSpeciesWidget(
    currentTree: Tree,
) {
    val showDialog = remember { mutableStateOf(false) }
    val currentTreeSpecies = remember { mutableStateOf(currentTree.Species) }
    currentTreeSpecies.value = currentTree.Species

    Row(
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = currentTreeSpecies.value,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = "樹種",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            modifier = Modifier
                .width(150.dp)
                .padding(vertical = 10.dp, horizontal = 10.dp),
            textStyle = TextStyle(fontSize = 18.sp),
        )
        Button(
            modifier = Modifier.width(90.dp),
            onClick = { showDialog.value = true }
        ) {
            Text(stringResource(id = R.string.input))
        }

        if (showDialog.value) {
            AdjustSpeciesDialog(
                onDismiss = {},
                onCancelClick = { showDialog.value = false },
                onNextButtonClick = {
                    showDialog.value = false
                    currentTreeSpecies.value = it
                    currentTree.Species = it
                }
            )
        }
    }
}