package com.example.ntufapp.ui.widget

import android.util.Log
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.R
import com.example.ntufapp.data.DataSource
import com.example.ntufapp.model.Tree
import com.example.ntufapp.ui.theme.IntervalDivider
import com.example.ntufapp.utils.showMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeConditionChips(//https://semicolonspace.com/jetpack-compose-filterchip/
    currentTree: Tree,
    reRenderCurrentTree: (SnapshotStateList<String>) -> Unit,
    modifier: Modifier
) {
    // when to recompose:
    // 1. currentTreeNum changes
    // 2. selectedItems changes
    // 3. treeCondition changes
    // *4. currentTreeNum.State changes won't trigger recomposition
    val context = LocalContext.current

    val singleItemList = DataSource.TreeSingleConditionList // only one item can be selected
    val multiItemList = DataSource.TreeMultiConditionList // multiple items can be selected
    val squirrelItemList = DataSource.SquirrelConditionList // multiple items can be selected

    val treeCondition = remember { mutableStateOf("") }
    treeCondition.value = currentTree.State.joinToString()

    val selectedItems = remember { mutableStateListOf<String>() }
    reRenderCurrentTree(selectedItems)

    Column(
        modifier = modifier
    ) {
        // the below log.d is a fun topic related to recomposition and infinite loop
        // Log.d("TreeConditionChips", "curTree: ${selectedItems.joinToString() }")
        IntervalDivider()
        LazyRow( modifier = modifier){
            itemsIndexed(singleItemList) { id, item ->
                FilterChip(
                    modifier = Modifier.padding(horizontal = 6.dp), // gap between items
                    selected = selectedItems.contains(item),
                    onClick = {
                        if (selectedItems.contains(item)) {
                            Log.d("TreeConditionChips", "curTree: ${currentTree.State.joinToString() }")
                            selectedItems.remove(item)
                        } else {
                            Log.d("TreeConditionChips", "else")
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
                            selectedItems.removeAll(squirrelItemList)
                            selectedItems.removeAll(singleItemList)
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
            itemsIndexed(multiItemList) { id, item ->
                FilterChip(
                    modifier = Modifier.padding(horizontal = 6.dp), // gap between items
                    selected = selectedItems.contains(item),
                    onClick = {
                        if (selectedItems.contains(item)) {
                            selectedItems.remove(item)
                        } else {
                            selectedItems.removeAll(singleItemList)
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
                onValueChange = {},
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
                    .padding(start = 5.dp, top = 10.dp, bottom = 10.dp, end = 5.dp)
            )

            Button(
                modifier = Modifier
                    .width(80.dp)
                    .padding(top = 10.dp, bottom = 10.dp),
                onClick = {
                    if (selectedItems.isEmpty()) {
                        showMessage(context, "請選擇生長狀態")
                    } else {
                        currentTree.State = selectedItems.toMutableList()
                        treeCondition.value = selectedItems.joinToString()
                        Log.d("TreeConditionChips", "string: ${System.identityHashCode(selectedItems.joinToString()) }")
                        Log.d("TreeConditionChips", "string: ${System.identityHashCode(selectedItems) }")
                        showMessage(context, "您已新增樣樹${currentTree.SampleNum}之生長狀態\n${currentTree.State.joinToString()}")
                    }
                }
            ) {
                Text(stringResource(id = R.string.input))
            }

//            TreeSpeciesWidget(
//                currentTree = currentTree
//            ) {}
        }
    }

}