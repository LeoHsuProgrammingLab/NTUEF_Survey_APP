package com.example.ntufapp.ui

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ntufapp.data.DataSource
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.md_theme_light_primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipsTreeCondition(
    newPlotData: PlotData,
    curTreeNum: MutableState<String>,
    onAdd: (MutableList<String>) -> Unit
) {
    val singleItemList = DataSource.TreeSingleConditionList
    val multiItemList = DataSource.TreeMultiConditionList
    val squirrelItemList = DataSource.SquirrelConditionList

//    val contextForToast = LocalContext.current.applicationContext

    val selectedItems = remember {
        mutableStateListOf<String>()/*.apply {
            addAll(newPlotData.value.searchTree(curTreeNum.value.toInt())!!.State)
        } // initially, first item is selected*/
    }

    val mod = Modifier.width(360.dp)
    Box(
        modifier = mod
//            .height()
            .border(width = 1.dp, md_theme_light_primary)
            .padding(10.dp)
    ) {
        Column(
            modifier = mod
        ) {
            LazyRow( modifier = mod){
                itemsIndexed(singleItemList) { id, item ->
                    FilterChip(
                        modifier = Modifier.padding(horizontal = 6.dp), // gap between items
                        selected = selectedItems.contains(item),
                        onClick = {
                            if (selectedItems.contains(item)) {
                                selectedItems.remove(item)
                            } else {
                                selectedItems.clear()
                                selectedItems.add(item)
                            }
//                            Toast.makeText(contextForToast, selectedItems.joinToString(), Toast.LENGTH_SHORT)
//                                .show()
                        },
                        label = {
                            Text(text = item)
                        }
                    )
                }
            }

            LazyRow( modifier = mod) {
                itemsIndexed(squirrelItemList) {id, item ->
                    FilterChip(
                        modifier = Modifier.padding(horizontal = 6.dp), // gap between items
                        selected = selectedItems.contains(item),
                        onClick = {
                            if (selectedItems.contains(item)) {
                                selectedItems.remove(item)
                            } else {
                                for(i in squirrelItemList.size - 1 downTo  0) {
                                    if(selectedItems.contains(squirrelItemList[i])) {
                                        selectedItems.remove(squirrelItemList[i])
                                    }
                                }
                                for(i in singleItemList.size - 1 downTo  0) {
                                    if(selectedItems.contains(singleItemList[i])) {
                                        selectedItems.remove(singleItemList[i])
                                    }
                                }
                                selectedItems.add(item)
                            }
//                            Toast.makeText(contextForToast, selectedItems.joinToString(), Toast.LENGTH_SHORT)
//                                .show()
                        },
                        label = {
                            Text(text = item)
                        }
                    )
                }
            }

            LazyRow( modifier = mod) {
                itemsIndexed(multiItemList) { id, item ->
                    FilterChip(
                        modifier = Modifier.padding(horizontal = 6.dp), // gap between items
                        selected = selectedItems.contains(item),
                        onClick = {
                            if (selectedItems.contains(item)) {
                                selectedItems.remove(item)
                            } else {
                                for(i in singleItemList.size - 1 downTo  0) {
                                    if(selectedItems.contains(singleItemList[i])) {
                                        selectedItems.remove(singleItemList[i])
                                    }
                                }
                                selectedItems.add(item)
                            }
//                            Toast.makeText(contextForToast, selectedItems.joinToString(), Toast.LENGTH_SHORT)
//                                .show()
                        },
                        label = {
                            Text(text = item)
                        }
                    )
                }
            }

            Row() {
                TextField(
                    value = newPlotData.searchTree(curTreeNum.value.toInt())!!.State.joinToString(),
                    onValueChange = {
                    },
                    readOnly = true,
                    label = { Text("生長狀況") },
                    modifier = Modifier
                        .width(200.dp)
                        .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                )

                Button(
                    modifier = Modifier.width(90.dp),
                    onClick = {
                        onAdd(selectedItems)
                    }
                ) {
                    Text("新增")
                }
            }
        }
    }


}
