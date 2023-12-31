package com.example.ntufapp.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.data.ntufappInfo.Companion.tag
import com.example.ntufapp.layout.showMessage
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.model.Tree
import com.example.ntufapp.ui.theme.CustomizedDivider
import com.example.ntufapp.ui.theme.DropdownDivider
import com.example.ntufapp.ui.theme.LowerShapes
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.UpperShapes
import com.example.ntufapp.ui.theme.dropDownItemModifier
import com.example.ntufapp.ui.theme.dropDownMenuModifier
import com.example.ntufapp.ui.theme.inputTextModifier
import com.example.ntufapp.ui.theme.lightBorder
import com.example.ntufapp.ui.theme.md_theme_light_inverseOnSurface
import com.example.ntufapp.ui.theme.md_theme_light_primary
import com.example.ntufapp.ui.theme.md_theme_light_primaryContainer
import kotlinx.coroutines.launch


@Composable
fun HtDBHView(
    numPlotTrees: MutableState<Int>,
    dbhTreeSet: MutableState<MutableSet<String>>,
    measHtTreeSet: MutableState<MutableSet<String>>,
    forkHtTreeSet: MutableState<MutableSet<String>>,
    visHtTreeSet: MutableState<MutableSet<String>>,
    newPlotData: PlotData,
    onNextButtonClick: () -> Unit
) {
    // Adjust the height
    val windowInfo = rememberWindowInfo()
    val height =  when (windowInfo.screenHeightInfo) {
        WindowInfo.WindowType.Compact -> 300.dp
        WindowInfo.WindowType.Medium -> 350.dp
        WindowInfo.WindowType.Expanded -> 420.dp
        else -> 400.dp
    }

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

//    val numPlotTrees = remember { mutableStateOf(totalTreesState.size) }
//    val dbhTreeSet = remember { mutableStateOf(totalTreesState.toMutableSet()) }
//    val measHtTreeSet = remember { mutableStateOf(totalTreesState.toMutableSet())}
//    val forkHtTreeSet = remember { mutableStateOf(totalTreesState.toMutableSet()) }
//    val visHtTreeSet = remember { mutableStateOf(totalTreesState.toMutableSet()) }
//
//    if (newPlotData.PlotTrees.size > numPlotTrees.value) {
//        for (i in newPlotData.PlotTrees.size downTo numPlotTrees.value + 1) {
//            dbhTreeSet.value.add(i.toString())
//            visHtTreeSet.value.add(i.toString())
//            forkHtTreeSet.value.add(i.toString())
//            measHtTreeSet.value.add(i.toString())
//        }
//        numPlotTrees.value = newPlotData.PlotTrees.size
//    }

    val curItemId = remember { mutableStateOf("1") }

    Column(
        modifier = Modifier
            .width(700.dp)
            .height(350.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 5.dp)
                .background(md_theme_light_primaryContainer, Shapes.medium)
                .border(lightBorder, Shapes.medium)
        ) {
            Column {

                val targetId = remember { mutableStateOf(1) }

                // Search Block
                Row(
                    modifier = Modifier
                        .height(70.dp)
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    OutlinedTextField(
                        value = curItemId.value,
                        onValueChange = {
                            curItemId.value = it
                            targetId.value = it.toIntOrNull()?: 1
                            coroutineScope.launch {
                                lazyListState.scrollToItem(targetId.value - 1)
                            }
                        },
                        label = {
                            Text(
                                text = "查看樣樹",
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                        },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .width(100.dp)
                    )

                    ExposedUnaddressedTreeList(
                        treeSet = dbhTreeSet,
                        label = "剩餘DBH",
                        widthType = "medium",
                        onChoose = {
                            targetId.value = it.toInt()
                            coroutineScope.launch {
                                lazyListState.scrollToItem(targetId.value - 1)
                            }
                        }
                    )

                    ExposedUnaddressedTreeList(
                        treeSet = measHtTreeSet,
                        label = "剩餘樹高",
                        widthType = "medium",
                        onChoose = {
                            targetId.value = it.toInt()
                            coroutineScope.launch {
                                lazyListState.scrollToItem(targetId.value - 1)
                            }
                        }
                    )

                    ExposedUnaddressedTreeList(
                        treeSet = visHtTreeSet,
                        label = "剩餘目視樹高",
                        widthType = "large",
                        onChoose = {
                            targetId.value = it.toInt()
                            coroutineScope.launch {
                                lazyListState.scrollToItem(targetId.value - 1)
                            }
                        }
                    )

                    ExposedUnaddressedTreeList(
                        treeSet = forkHtTreeSet,
                        label = "剩餘分岔樹高",
                        widthType = "large",
                        onChoose = {
                            targetId.value = it.toInt()
                            coroutineScope.launch {
                                lazyListState.scrollToItem(targetId.value - 1)
                            }
                        }
                    )
                }

                CustomizedDivider()

                // Progress Block
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = md_theme_light_primary,
                            shape = LowerShapes.medium
                        )
                ) {
                    itemsIndexed(newPlotData.PlotTrees) { idx, tree ->
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val treeDBH = remember { mutableStateOf(tree.DBH.toString()) }
                            val treeMeasHt = remember { mutableStateOf(tree.MeasHeight.toString()) }
                            val treeVisHt = remember { mutableStateOf(tree.VisHeight.toString()) }
                            val treeForkHt = remember { mutableStateOf(tree.ForkHeight.toString()) }

                            ListItem(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .background(md_theme_light_inverseOnSurface),
                                headlineContent = {
                                    Text(
                                        text = String.format("%03d", tree.SampleNum),
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier
                                            .wrapContentWidth(Alignment.CenterHorizontally)
                                            .wrapContentHeight(Alignment.CenterVertically)
                                    )
                                }
                            )

                            LazyColumnInputTextField(
                                textContent = treeDBH,
                                unaddressedTreeSet = dbhTreeSet,
                                tree = tree,
                                label = "DBH",
                                modifier = inputTextModifier
                            )

                            LazyColumnInputTextField(
                                textContent = treeMeasHt,
                                unaddressedTreeSet = measHtTreeSet,
                                tree = tree,
                                label = "測量樹高",
                                modifier = inputTextModifier
                            )

                            LazyColumnInputTextField(
                                textContent = treeVisHt,
                                unaddressedTreeSet = visHtTreeSet,
                                tree = tree,
                                label = "目視樹高",
                                modifier = inputTextModifier
                            )

                            LazyColumnInputTextField(
                                textContent = treeForkHt,
                                unaddressedTreeSet = forkHtTreeSet,
                                tree = tree,
                                label = "分岔樹高",
                                modifier = inputTextModifier
                            )
                        }
                        DropdownDivider()
                    }
                }
            }
        }

//        CheckAddButton(
//            dbhSet = dbhTreeSet,
//            htSet = measHtTreeSet,
//            visHtSet = visHtTreeSet,
//            measHtSet = forkHtTreeSet,
//            onNextButtonClick = onNextButtonClick
//        )
    }
}

@Composable
fun LazyColumnInputTextField(
    textContent: MutableState<String>,
    unaddressedTreeSet: MutableState<MutableSet<String>>,
    tree: Tree,
    label: String,
    modifier: Modifier
) {
    val context = LocalContext.current
    TextField(
        value = textContent.value,
        onValueChange = {
            textContent.value = it
            val newTarget = it.toDoubleOrNull()
            if (newTarget != null) {
                if (newTarget > 0) {
                    unaddressedTreeSet.value.remove(tree.SampleNum.toString())
                    when(label) {
                        "DBH" -> tree.DBH = newTarget
                        "目視樹高" -> tree.VisHeight = newTarget
                        "測量樹高" -> tree.MeasHeight = newTarget
                        "分岔樹高" -> tree.ForkHeight = newTarget
                    }
                } else {
                    unaddressedTreeSet.value.add(tree.SampleNum.toString())
                }
            } else {
                showMessage(context,"請輸入正確的數字")
            }
        },
        label = {
            Text(
                label,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        modifier = modifier,
        textStyle = TextStyle(fontSize = 18.sp),
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ExposedUnaddressedTreeList(
    treeSet: MutableState<MutableSet<String>>,
    label: String,
    widthType: String,
    onChoose: (String) -> Unit
) {
    val dropdownExpanded = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = dropdownExpanded.value,
        onExpandedChange = {
            dropdownExpanded.value = !dropdownExpanded.value
        },
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        OutlinedTextField(
            value = treeSet.value.size.toString(),
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = label,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded.value)
            },
            modifier = dropDownMenuModifier
                .width(
                    when (widthType) {
                        "medium" -> 100.dp
                        "large" -> 130.dp
                        else -> 90.dp
                    }
                )
                .menuAnchor(),
            textStyle = TextStyle(fontSize = 16.sp),
        )

        // DropdownMenu
        ExposedDropdownMenu(
            expanded = dropdownExpanded.value,
            onDismissRequest = { dropdownExpanded.value = false },
            modifier = dropDownMenuModifier
        ) {
            treeSet.value.sortedWith(Comparator {
                str1, str2 -> str1.toInt() - str2.toInt()
            }).forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(option)
                    },
                    onClick = {
                        dropdownExpanded.value = false
                        onChoose(option)
                    },
                    modifier = dropDownItemModifier
                        .width(
                            when (widthType) {
                                "medium" -> 100.dp
                                "large" -> 130.dp
                                else -> 90.dp
                            }
                        )
                )
                DropdownDivider()
            }
        }
    }
}
