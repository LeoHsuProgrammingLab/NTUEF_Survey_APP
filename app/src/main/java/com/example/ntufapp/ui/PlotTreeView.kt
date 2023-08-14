package com.example.ntufapp.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.model.Tree
import com.example.ntufapp.ui.theme.InputProgressView

@Composable
fun PlotTreeView(curTreeNum: MutableState<String>, totalTreesState: MutableList<String>, newPlotData: PlotData) {
    val curTree = remember {
        mutableStateOf(newPlotData.searchTree(curTreeNum.value.toInt()))
    }

    Column() {
        Row(
            horizontalArrangement = Arrangement.Center,
        ) {
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
            Box(
                modifier = Modifier.padding(10.dp)
            ) {
                Row(){
                    TextField(
                        value = curTree.value!!.Species,
                        onValueChange = {
                        },
                        readOnly = true,
                        label = { Text("樹種") },
                        modifier = Modifier
                            .width(150.dp)
                            .padding(vertical = 10.dp)
                    )
                    Button(
                        modifier = Modifier
                            .width(90.dp)
                            .padding(top = 15.dp, start = 3.dp),
                        onClick = {/*TODO*/ }
                    ) {
                        Text("修改")
                    }
                }
            }
        }

        ChipsTreeCondition(
            curTree = curTree.value!!,
        )
    }
}