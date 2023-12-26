package com.example.ntufapp.ui

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.model.Tree

@Composable
fun PlotTreeView(
    curTreeNum: MutableState<String>,
    totalTreesState: MutableList<String>,
    newPlotData: PlotData
) {
    val curTree = remember {
        mutableStateOf(newPlotData.searchTree(curTreeNum.value.toInt()))
    }

    Column {
        Box(
            modifier = Modifier.padding(10.dp)
        ) {
            Row{
                SearchableDropdownMenu(
                    totalTreesState,
                    label = "請選擇樣樹",
                    defaultString = "1",
                    onChoose = {
                        curTreeNum.value = it
                        curTree.value = newPlotData.searchTree(curTreeNum.value.toInt())
                    },
                    onAdd = {
                        newPlotData.PlotTrees.add(Tree(SampleNum = it.toInt()))
                        curTreeNum.value = it
                    }
                )

                TreeSpeciesWidget(curTree = curTree.value!!)
            }
        }

        ChipsTreeCondition(
            curTree = curTree.value!!,
        )
    }
}

@Composable
fun TreeSpeciesWidget(
    curTree: Tree,
) {
    val showDialog = remember { mutableStateOf(false) }
    val curTreeSpecies = remember {
        mutableStateOf(curTree.Species)
    }
    curTreeSpecies.value = curTree.Species

    Row(
        modifier = Modifier
            .padding(5.dp)
    ){
        TextField(
            value = curTreeSpecies.value,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = "樹種",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            modifier = Modifier
                .width(150.dp)
                .padding(vertical = 10.dp),
            textStyle = TextStyle(fontSize = 18.sp),
        )
        Button(
            modifier = Modifier
                .width(90.dp)
                .padding(top = 15.dp, start = 3.dp),
            onClick = {
                showDialog.value = true
            }
        ) {
            Text("修改")
        }

        if (showDialog.value) {
            AdjustSpeciesDialogue(
                onDismiss = {showDialog.value = false},
                onCancelClick = { showDialog.value = false},
                onNextButtonClick = {
                    showDialog.value = false
                    curTreeSpecies.value = it
                    curTree.Species = it
                }
            )
        }
    }
}