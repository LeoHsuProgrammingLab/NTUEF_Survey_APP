package com.example.ntufapp.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.model.Tree

@Composable
fun SpeciesConditionView(
    curTreeNum: MutableState<String>,
    totalTreesNumList: MutableList<String>,
    newPlotData: PlotData
) {
    val curTree = remember { mutableStateOf(newPlotData.searchTree(curTreeNum.value.toInt())) }
    val modifier = Modifier.width(450.dp)
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            SearchableDropdownMenu(
                totalTreesNumList,
                label = "請選擇樣樹",
                defaultString = "1",
                keyboardType = KeyboardType.Number,
                onChoose = {
                    curTreeNum.value = it
                    curTree.value = newPlotData.searchTree(curTreeNum.value.toInt())
                },
                onAdd = {
                    newPlotData.PlotTrees.add(Tree(SampleNum = it.toInt()))
                    curTreeNum.value = it
                    curTree.value = newPlotData.searchTree(curTreeNum.value.toInt())
                    Log.i("PlotTreeView", "newPlotData.getPlotTreesSize(): ${newPlotData.PlotTrees.size}")
                }
            )

            TreeSpeciesWidget(curTree = curTree.value!!)
        }

        ChipsTreeCondition(curTree = curTree.value!!, modifier = modifier)
    }
}

@Composable
fun TreeSpeciesWidget(
    curTree: Tree,
) {
    val showDialog = remember { mutableStateOf(false) }
    val curTreeSpecies = remember { mutableStateOf(curTree.Species) }
    curTreeSpecies.value = curTree.Species

    Row(
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        TextField(
            value = curTreeSpecies.value,
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
            Text("修改")
        }

        if (showDialog.value) {
            AdjustSpeciesDialog(
                onDismiss = {showDialog.value = false },
                onCancelClick = { showDialog.value = false },
                onNextButtonClick = {
                    showDialog.value = false
                    curTreeSpecies.value = it
                    curTree.Species = it
                }
            )
        }
    }
}