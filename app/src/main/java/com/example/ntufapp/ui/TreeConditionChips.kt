package com.example.ntufapp.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.data.DataSource
import com.example.ntufapp.model.Tree
import com.example.ntufapp.ui.theme.md_theme_light_primary
import com.example.ntufapp.utils.showMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipsTreeCondition(//https://semicolonspace.com/jetpack-compose-filterchip/
    curTree: Tree,
    modifier: Modifier
) {
    val curContext = LocalContext.current

    val singleItemList = DataSource.TreeSingleConditionList
    val multiItemList = DataSource.TreeMultiConditionList
    val squirrelItemList = DataSource.SquirrelConditionList

//    val contextForToast = LocalContext.current.applicationContext

    val selectedItems = remember {
        mutableStateListOf<String>()/*.apply {
            addAll(newPlotData.value.searchTree(curTreeNum.value.toInt())!!.State)
        } // initially, first item is selected*/
    }

    val treeCondition = remember { mutableStateOf("") }

    treeCondition.value = curTree.State.joinToString()

    Box(
        modifier = modifier
//            .height()
            .border(width = 1.dp, md_theme_light_primary)
            .padding(10.dp)
    ) {
        Column(
            modifier = modifier
        ) {
            LazyRow( modifier = modifier){
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
                        },
                        label = {
                            Text(
                                text = item,
                                style = TextStyle(
                                    fontSize = 18.sp
                                )
                            )
                        }
                    )
                }
            }

            LazyRow( modifier = modifier) {
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
                            Text(
                                text = item,
                                style = TextStyle(
                                    fontSize = 18.sp
                                )
                            )
                        }
                    )
                }
            }

            LazyRow( modifier = modifier) {
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
                        },
                        label = {
                            Text(
                                text = item,
                                style = TextStyle(
                                    fontSize = 18.sp
                                )
                            )
                        }
                    )
                }
            }

            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = treeCondition.value,
                    onValueChange = {
                    },
                    readOnly = true,
                    label = {
                        Text(
                            text = "生長狀況",
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .width(200.dp)
                        .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                )

                    Button(
                        modifier = Modifier
                            .width(90.dp)
                            .padding(top = 10.dp, bottom = 10.dp),
                        onClick = {
                            if (selectedItems.isEmpty()) {
                                showMessage(curContext, "請選擇生長狀態")
                            } else {
                                curTree.State = selectedItems
                                treeCondition.value = curTree.State.joinToString()
                                showMessage(curContext, "您已新增 樣樹${curTree.SampleNum} 之生長狀態\n${curTree.State.joinToString()}")
                            }
                        }
                    ) {
                        Text("新增")
                    }
            }
        }
    }
}