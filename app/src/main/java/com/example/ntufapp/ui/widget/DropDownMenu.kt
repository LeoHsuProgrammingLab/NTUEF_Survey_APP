package com.example.ntufapp.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    allPlotsInfo: Map<String, Map<String, List<Pair<String, String>>>>, // dept_name to (area_name to Pair<location_name, location_mid>)
    onChoose: (String) -> Unit,
    widthType: String = "large",
) {
    val deptNameDropdownExpanded = remember { mutableStateOf(false) }
    val areaNameDropdownExpanded = remember { mutableStateOf(false) }
    val locationDropdownExpanded = remember { mutableStateOf(false) }

    // deptNameList will be expanded in the future
    val deptNameList = allPlotsInfo.keys.toList()
    val deptName = remember { mutableStateOf(allPlotsInfo.keys.first()) }
    val areaNameList = remember {
        mutableStateOf( allPlotsInfo.values.map { it.keys }.flatten() ) // it.keys is a list, so flatten it
    }
    val areaName = remember { mutableStateOf("請選擇試驗地") }

    // TODO: 林班地
    // You have to choose the
    val locationList = remember { // List of location_name
        mutableStateOf(listOf("請選擇樣區") + (allPlotsInfo[deptName.value]?.get(areaName.value)?.map { it.first } ?: emptyList()))
    }
    val location = remember { mutableStateOf("請選擇樣區") }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val modifier = dropDownMenuModifier
            .width(
                when (widthType) {
                    "medium" -> 100.dp
                    "large" -> 600.dp
                    else -> 90.dp
                }
            )

        // 1. ExposedDropdownMenuBox for dept_name
        ExposedDropdownMenuBox(
            expanded = deptNameDropdownExpanded.value,
            onExpandedChange = {
                deptNameDropdownExpanded.value = !deptNameDropdownExpanded.value
            },
            modifier = Modifier
                .height(80.dp)
                .padding(horizontal = 10.dp)
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            OutlinedTextField(
                value = deptName.value,
                onValueChange = {},
                //            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None),
                readOnly = true,
                label = {
                    Text(text = label, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold))
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = deptNameDropdownExpanded.value)
                },
                modifier = modifier
                    .height(50.dp)
                    .menuAnchor()
                    .onFocusChanged { keyboardController?.hide() },
                textStyle = TextStyle(fontSize = 16.sp),
            )

            // DropdownMenu
            ExposedDropdownMenu(
                expanded = deptNameDropdownExpanded.value,
                onDismissRequest = { deptNameDropdownExpanded.value = false },
                modifier = dropDownMenuModifier
            ) {
                deptNameList.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(option)
                        },
                        onClick = {
                            deptNameDropdownExpanded.value = false
                            areaNameList.value = listOf("請選擇樣區") + (allPlotsInfo[option]?.keys?.sorted() ?: emptyList())
                            deptName.value = option
                            areaName.value = areaNameList.value.first()
                        },
                        modifier = modifier
                    )
                    DropdownDivider()
                }
            }
        }

        // 2. ExposedDropdownMenuBox for area_name
        ExposedDropdownMenuBox(
            expanded = deptNameDropdownExpanded.value,
            onExpandedChange = {
                areaNameDropdownExpanded.value = !areaNameDropdownExpanded.value
            },
            modifier = Modifier
                .height(80.dp)
                .padding(horizontal = 10.dp)
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            OutlinedTextField(
                value = deptName.value,
                onValueChange = {},
                //            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None),
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
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = deptNameDropdownExpanded.value)
                },
                modifier = modifier
                    .height(50.dp)
                    .menuAnchor()
                    .onFocusChanged { keyboardController?.hide() },
                textStyle = TextStyle(fontSize = 16.sp),
            )

            // DropdownMenu
            ExposedDropdownMenu(
                expanded = deptNameDropdownExpanded.value,
                onDismissRequest = { deptNameDropdownExpanded.value = false },
                modifier = dropDownMenuModifier
            ) {
                deptNameList.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(option)
                        },
                        onClick = {
                            areaNameDropdownExpanded.value = false
                            locationList.value = listOf("請選擇樣區") + (allPlotsInfo[deptName.value]?.get(option)
                                ?.map { pair -> pair.first }?.sorted() ?: emptyList())
                            areaName.value = option
                            location.value = locationList.value.firstOrNull() ?: "尚無"
                        },
                        modifier = modifier
                    )
                    DropdownDivider()
                }
            }
        }

        // 3. ExposedDropdownMenuBox for location_name
        ExposedDropdownMenuBox(
            expanded = locationDropdownExpanded.value,
            onExpandedChange = {
                locationDropdownExpanded.value = !locationDropdownExpanded.value
            },
            modifier = Modifier
                .height(80.dp)
                .padding(horizontal = 10.dp)
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            OutlinedTextField(
                value = location.value,
                onValueChange = {},
                //            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None),
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
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationDropdownExpanded.value)
                },
                modifier = modifier
                    .height(50.dp)
                    .menuAnchor()
                    .onFocusChanged { keyboardController?.hide() },
                textStyle = TextStyle(fontSize = 16.sp),
            )

            // DropdownMenu
            ExposedDropdownMenu(
                expanded = locationDropdownExpanded.value,
                onDismissRequest = { locationDropdownExpanded.value = false },
                modifier = dropDownMenuModifier
            ) {
                locationList.value.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(option)
                        },
                        onClick = {
                            locationDropdownExpanded.value = false
                            location.value = option
                            onChoose(
                                allPlotsInfo[deptName.value]?.get(areaName.value)?.find { pair -> pair.first == option }?.second ?: ""
                            )
                        },
                        modifier = modifier
                    )
                    DropdownDivider()
                }
            }
        }
    }
}