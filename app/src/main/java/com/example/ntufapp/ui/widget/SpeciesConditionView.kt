package com.example.ntufapp.ui.widget

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.R
import com.example.ntufapp.data.DataSource
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.model.Tree
import com.example.ntufapp.ui.theme.IntervalDivider
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.lightBorder
import com.example.ntufapp.utils.showMessage
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeciesConditionView(
    currentTreeNum: MutableState<String>,
    speciesTreeSet: MutableState<MutableSet<String>>,
    conditionTreeSet: MutableState<MutableSet<String>>,
    totalTreesNumList: MutableList<String>,
    newPlotData: PlotData,
    surveyType: String = "NewSurvey"
) {
    val context = LocalContext.current
    val modifier = Modifier.width(530.dp)
    // Adjust the height
    val windowInfo = rememberWindowInfo()
    val height =  when (windowInfo.screenHeightInfo) {
        WindowInfo.WindowType.Compact -> 300.dp
        WindowInfo.WindowType.Medium -> 380.dp
        WindowInfo.WindowType.Expanded -> 420.dp
    }

    val currentTree = remember { mutableStateOf(Tree()) }
    currentTree.value = newPlotData.PlotTrees.find { it.SampleNum == currentTreeNum.value.toInt() }!!

    Box(
        modifier = Modifier
            .padding(5.dp)
            .height(height)
            .border(lightBorder, Shapes.medium),
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val unaddressedModifier = Modifier.height(60.dp)
                SearchableAddMenu(
                    totalTreesNumList,
                    label = "請選擇樣樹",
                    defaultString = "1",
                    keyboardType = KeyboardType.Number,
                    onChoose = {
                        currentTreeNum.value = it
                    },
                    onAdd = {
                        newPlotData.PlotTrees.add(Tree(SampleNum = it.toInt()))
                        currentTreeNum.value = it
                    },
                    modifier = Modifier
                        .height(75.dp)
                        .padding(5.dp)
                        .width(150.dp)
                )

                ExposedUnaddressedTreeList(
                    treeSet = conditionTreeSet,
                    label = "剩餘生長狀況",
                    widthType = "large",
                    modifier = unaddressedModifier,
                    onChoose = {
                        currentTreeNum.value = it
                    }
                )

                ExposedUnaddressedTreeList(
                    treeSet = speciesTreeSet,
                    label = "樹種",
                    widthType = "large",
                    modifier = unaddressedModifier,
                    onChoose = {
                        currentTreeNum.value = it
                    }
                )

            }
//            TreeConditionChips(
//                currentTreeNum = currentTreeNum.value,
//                reRenderCurrentTree = { it.clear() },
//                modifier = modifier
//            )

            val singleItemList = DataSource.TreeSingleConditionList // only one item can be selected
            val multiItemList = DataSource.TreeMultiConditionList // multiple items can be selected
            val squirrelItemList = DataSource.SquirrelConditionList // multiple items can be selected
            val selectedItems = remember { mutableStateListOf<String>() }
            selectedItems.clear()
            selectedItems.addAll(currentTree.value.State)

            Column(
                modifier = modifier
            ) {
                // the below log.d is a fun topic related to recomposition and infinite loop
                // Log.d("TreeConditionChips", "curTree: ${selectedItems.joinToString() }")
                IntervalDivider()
                Row(
                    modifier = modifier
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "當前樣樹: ${currentTreeNum.value}號",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    )
                }

                LazyRow( modifier = modifier){
                    itemsIndexed(singleItemList) { id, item ->
                        FilterChip(
                            modifier = Modifier.padding(horizontal = 5.dp), // gap between items
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

                val treeCondition = remember { mutableStateOf("") }
                treeCondition.value = currentTree.value.State.joinToString()
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
                            .padding(start = 5.dp, top = 10.dp, bottom = 5.dp, end = 5.dp)
                    )

                    Button(
                        modifier = Modifier
                            .width(80.dp)
                            .padding(top = 10.dp, bottom = 10.dp),
                        onClick = {
                            if (selectedItems.isEmpty()) {
                                showMessage(context, "請選擇生長狀態")
                            } else {
                                currentTree.value.State = selectedItems.toMutableList()
                                treeCondition.value = selectedItems.joinToString()
//                                Log.d("TreeConditionChips", "string: ${System.identityHashCode(selectedItems.joinToString()) }")
//                                Log.d("TreeConditionChips", "string: ${System.identityHashCode(selectedItems) }")
                                showMessage(context, "您已新增樣樹${currentTree.value.SampleNum}之生長狀態\n${currentTree.value.State.joinToString()}")
                            }
                        }
                    ) {
                        Text(stringResource(id = R.string.input))
                    }

                    TreeSpeciesWidget(currentTree = currentTree.value)
                }
            }
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
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp, end = 5.dp),
            textStyle = TextStyle(fontSize = 18.sp),
        )
        Button(
            modifier = Modifier.width(80.dp),
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