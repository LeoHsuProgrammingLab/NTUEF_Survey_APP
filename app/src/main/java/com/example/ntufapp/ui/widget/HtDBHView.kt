package com.example.ntufapp.ui.widget

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.model.Tree
import com.example.ntufapp.ui.theme.CustomizedDivider
import com.example.ntufapp.ui.theme.DropdownDivider
import com.example.ntufapp.ui.theme.LowerShapes
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.circleThreeDigitsModifier
import com.example.ntufapp.ui.theme.dropDownItemModifier
import com.example.ntufapp.ui.theme.dropDownMenuModifier
import com.example.ntufapp.ui.theme.inputTextModifier
import com.example.ntufapp.ui.theme.lightBorder
import com.example.ntufapp.ui.theme.md_theme_light_primary
import com.example.ntufapp.ui.theme.md_theme_light_primaryContainer
import com.example.ntufapp.utils.showMessage
import kotlinx.coroutines.launch


@Composable
fun HtDBHView(
    totalTreesNumList: MutableList<String>,
    dbhTreeSet: MutableState<MutableSet<String>>,
    measHtTreeSet: MutableState<MutableSet<String>>,
    forkHtTreeSet: MutableState<MutableSet<String>>,
    visHtTreeSet: MutableState<MutableSet<String>>,
    newPlotData: PlotData
) {
    for (i in totalTreesNumList.size downTo 1) {
        if (dbhTreeSet.value.contains(i.toString())) {
            break
        }
        dbhTreeSet.value.add(i.toString())
        visHtTreeSet.value.add(i.toString())
        forkHtTreeSet.value.add(i.toString())
        measHtTreeSet.value.add(i.toString())
    }

    // Adjust the height
    val windowInfo = rememberWindowInfo()
    val height =  when (windowInfo.screenHeightInfo) {
        WindowInfo.WindowType.Compact -> 300.dp
        WindowInfo.WindowType.Medium -> 380.dp
        WindowInfo.WindowType.Expanded -> 420.dp
    }

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    Log.i("HtDBHView", "newPlotData.getPlotTreesSize(): ${newPlotData.PlotTrees.size}")

    val curItemId = remember { mutableStateOf("1") }

    Box(
        modifier = Modifier
            .padding(5.dp)
            .width(670.dp)
            .height(height)
            .background(md_theme_light_primaryContainer, Shapes.medium)
            .border(lightBorder, Shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Column {
            val targetId = remember { mutableIntStateOf(1) }

            // Search Block
            Row(
                modifier = Modifier
                    .height(70.dp)
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
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
                    modifier = Modifier,
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
                    modifier = Modifier,
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
                    modifier = Modifier,
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
                    modifier = Modifier,
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
                        ListItem(
                            modifier = circleThreeDigitsModifier,
                            headlineContent = {
                                // https://stackoverflow.com/questions/63719072/jetpack-compose-centering-text
                                Text(
                                    text = String.format("%03d", tree.SampleNum),
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.wrapContentHeight(Alignment.CenterVertically)
                                )
                            }
                        )

                        val dbh = remember{ mutableStateOf(tree.DBH.toString()) }
                        LazyColumnInputTextField(
                            textContent = dbh,
                            unaddressedTreeSet = dbhTreeSet,
                            tree = tree,
                            type = "DBH",
                        )

                        val measHeight = remember{ mutableStateOf(tree.MeasHeight.toString()) }
                        LazyColumnInputTextField(
                            textContent = measHeight,
                            unaddressedTreeSet = measHtTreeSet,
                            tree = tree,
                            type = "測量樹高",
                        )

                        val visHeight = remember{ mutableStateOf(tree.VisHeight.toString()) }
                        LazyColumnInputTextField(
                            textContent = visHeight,
                            unaddressedTreeSet = visHtTreeSet,
                            tree = tree,
                            type = "目視樹高",
                        )

                        val forkHeight = remember{ mutableStateOf(tree.ForkHeight.toString()) }
                        LazyColumnInputTextField(
                            textContent = forkHeight,
                            unaddressedTreeSet = forkHtTreeSet,
                            tree = tree,
                            type = "分岔樹高",
                        )
                    }
                    DropdownDivider()
                }
            }
        }
    }

}

@Composable
fun LazyColumnInputTextField(
    textContent: MutableState<String>,
    unaddressedTreeSet: MutableState<MutableSet<String>>,
    tree: Tree,
    type: String,
) {
    val context = LocalContext.current
    if (textContent.value == "0.0") {
        textContent.value = ""
    }
    Log.d("LazyColumnInputTextField", "tree.SampleNum: ${tree.SampleNum}, ${textContent.value}")

    fun updateTree(newValue: Double?) {
        if (newValue != null) {
            if (newValue > 0) {
                unaddressedTreeSet.value.remove(tree.SampleNum.toString())
                when(type) {
                    "DBH" -> tree.DBH = newValue
                    "目視樹高" -> tree.VisHeight = newValue
                    "測量樹高" -> tree.MeasHeight = newValue
                    "分岔樹高" -> tree.ForkHeight = newValue
                }
            } else {
                unaddressedTreeSet.value.add(tree.SampleNum.toString())
            }
        } else {
            unaddressedTreeSet.value.add(tree.SampleNum.toString())
            showMessage(context,"請輸入正確的數字")
        }
    }

    TextField(
        value = textContent.value,
        onValueChange = {
            if (it.isNotEmpty()) {
                textContent.value = it
                updateTree(it.toDoubleOrNull())
            } else {
                Log.d("LazyColumnInputTextField", "it is empty")
            }
            textContent.value = it
            updateTree(it.toDoubleOrNull())
        },
        label = {
            Text(
                type,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        placeholder = { Text("請輸入數字") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(fontSize = 18.sp),
        modifier = inputTextModifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ExposedUnaddressedTreeList(
    treeSet: MutableState<MutableSet<String>>,
    label: String,
    widthType: String,
    modifier: Modifier,
    onChoose: (String) -> Unit
) {
    Log.d("ExposedUnaddressedTreeList", "Re-render")
    val dropdownExpanded = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = dropdownExpanded.value,
        onExpandedChange = {
            dropdownExpanded.value = !dropdownExpanded.value
        },
        modifier = modifier.padding(horizontal = 10.dp)
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
            treeSet.value.sortedWith(
                Comparator { str1, str2 -> str1.toInt() - str2.toInt() }
            ).forEach { option ->
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
