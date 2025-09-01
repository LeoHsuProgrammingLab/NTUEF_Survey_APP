package com.example.ntufapp.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.ui.theme.DropdownDivider
import com.example.ntufapp.ui.theme.dropDownItemModifier
import com.example.ntufapp.ui.theme.dropDownMenuModifier

// This file is to store all the dropdown menu without searching function, simply select the item from the list

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PlotSelectionDropDownMenu(
    label: String,
    allPlotsInfo: Map<String, Map<String, List<Map<String, List<Pair<String, String>>>>>>, // dept_name to (area_name to Pair<location_name, location_mid>)
    onChoose: (String, String) -> Unit,
    widthType: String = "large",
) {
    val deptNameDropdownExpanded = remember { mutableStateOf(false) }
    val compartDropdownExpanded = remember { mutableStateOf(false) }
    val areaNameDropdownExpanded = remember { mutableStateOf(false) }
    val locationDropdownExpanded = remember { mutableStateOf(false) }

    // deptNameList will be expanded in the future, currently only one dept
    val deptNameList = allPlotsInfo.keys.toList()
    val deptName = remember { mutableStateOf(allPlotsInfo.keys.first()) }

    // 林班地 compartment
    val compartList = remember {
        mutableStateOf(
        allPlotsInfo[deptName.value]
            ?.keys
            ?.toList()
            ?.sortedBy { it.toInt() }
            ?: emptyList()
        )
    }
    val compart = remember { mutableStateOf("請選擇林班地") }


    // areaNameList contains all the area_name in the selected dept
    val areaNameList = remember {
        mutableStateOf(
        allPlotsInfo[deptName.value]
            ?.get(compart.value)
            ?.flatMap { it.keys }
            ?: emptyList()
        )
    }
    val areaName = remember { mutableStateOf("請選擇試驗地") }

    // locationList contains all the location_name in the selected area
    val locationList = remember { // List of location_name
        mutableStateOf(
        allPlotsInfo[deptName.value]
            ?.get(compart.value)
            ?.find { it.containsKey(areaName.value) }
            ?.get(areaName.value)
            ?.map {it.first}
            ?: emptyList()
        )
    }
    val location = remember { mutableStateOf("請選擇樣區") }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val outlinedModifier = dropDownMenuModifier
            .width(
                when (widthType) {
                    "medium" -> 100.dp
                    "large" -> 600.dp
                    else -> 90.dp
                }
            )
        val boxModifier = Modifier
            .padding(horizontal = 10.dp)
            .height(80.dp)

        @Composable
        fun DropdownBox (
            expanded: Boolean,
            onExpandChange: (Boolean) -> Unit,
            currentValue: String,
            itemList: List<String>,
            onItemsChange: (String) -> Unit,
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            val selectedIndex = remember { mutableIntStateOf(itemList.indexOf(currentValue)) }
            val listState = rememberLazyListState()

            LaunchedEffect(expanded) {
                if (expanded && selectedIndex.intValue != -1) {
                    listState.animateScrollToItem(selectedIndex.intValue)
                }
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { onExpandChange(!expanded) },
                modifier = boxModifier
            ) {
                OutlinedTextField(
                    value = currentValue,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(text = label, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold))
                    },
                    trailingIcon = {
                        IconButton (onClick = { onExpandChange(!expanded) }) {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    },
                    modifier = outlinedModifier
                        .height(50.dp)
                        .menuAnchor()
                        .onFocusChanged { keyboardController?.hide() },
                    textStyle = TextStyle(fontSize = 16.sp),
                )

                // DropdownMenu
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {},
                    modifier = dropDownMenuModifier.height(300.dp)
                ) {
                    // SubcomposeLayout
                    // Should define the height and width (size) of the LazyColumn
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(500.dp)
                            .height(300.dp)
                    ) {
                        itemsIndexed(itemList) { index, option ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = option)
                                },
                                onClick = {
                                    onExpandChange(false)
                                    onItemsChange(option)
                                    selectedIndex.intValue = index  // Update the selected index
                                },
                                modifier = dropDownItemModifier
                            )
                            DropdownDivider()
                        }
                    }
                }
            }
        }

        DropdownBox(
            expanded = deptNameDropdownExpanded.value,
            onExpandChange = { deptNameDropdownExpanded.value = it },
            currentValue = deptName.value,
            itemList = deptNameList,
            onItemsChange = {
                deptName.value = it
                compartList.value = listOf("請選擇林班地") + (allPlotsInfo[it]?.keys?.sortedBy{ s -> s.toInt() } ?: emptyList())
                compart.value = compartList.value.first()
            }
        )

        DropdownBox(
            expanded = compartDropdownExpanded.value,
            onExpandChange = {compartDropdownExpanded.value = it},
            currentValue = compart.value,
            itemList = compartList.value,
            onItemsChange = {
                compart.value = it
                areaNameList.value = listOf("請選擇試驗地") + (allPlotsInfo[deptName.value]?.get(it)?.flatMap { list -> list.keys }?.sorted() ?: emptyList())
                areaName.value = areaNameList.value.first()
            }
        )

        DropdownBox(
            expanded = areaNameDropdownExpanded.value,
            onExpandChange = { areaNameDropdownExpanded.value = it },
            currentValue = areaName.value,
            itemList = areaNameList.value,
            onItemsChange = { it ->
                areaName.value = it
                locationList.value = listOf("請選擇樣區") + (allPlotsInfo[deptName.value]?.get(compart.value)?.find { listKeys -> listKeys.containsKey(it) }?.get(it)?.map { it.first } ?: emptyList())
                location.value = locationList.value.firstOrNull() ?: "尚無"
            }
        )

        DropdownBox(
            expanded = locationDropdownExpanded.value,
            onExpandChange = { locationDropdownExpanded.value = it },
            currentValue = location.value,
            itemList = locationList.value,
            onItemsChange = {
                location.value = it
                onChoose(
                    allPlotsInfo[deptName.value]?.get(compart.value)?.find {listKeys -> listKeys.containsKey(areaName.value) }?.get(areaName.value)?.find { pair -> pair.first == it }?.second ?: "",
                    deptName.value
                )
            }
        )
    }
}

