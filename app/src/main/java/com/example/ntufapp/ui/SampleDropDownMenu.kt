package com.example.ntufapp.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ntufapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeStateMenu(label: String, dropdownItems: List<String>) {
    val dropdownExpanded = remember {
        mutableStateOf(false)
    }

    val dropdownItemsState = remember {
        mutableStateOf(dropdownItems)
    }

    val selectedOption = remember {
        mutableStateOf(dropdownItems[0])
    }

    val searchText = remember {
        mutableStateOf("")
    }

    val squirrelInfExpanded = remember {
        mutableStateOf(false)
    }
    val squirrelInfItems = listOf("早期", "中期", "晚期")

    Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
//        Box(
//            modifier = Modifier.size(width = 100.dp, height = 100.dp),
//            contentAlignment = Alignment.TopStart
//        ) {
//            Text(label, modifier = Modifier.padding(end = 0.dp), textAlign = TextAlign.Start, fontSize = 20.sp)
//        }

        ExposedDropdownMenuBox(
            expanded = dropdownExpanded.value,
            onExpandedChange = {
                dropdownExpanded.value = !dropdownExpanded.value
                squirrelInfExpanded.value = false
            }
        ) {
            OutlinedTextField(
                value = searchText.value,
                onValueChange = {item ->
                    filterOptions(dropdownItems, item).let {
                        dropdownItemsState.value = it
                    }
                    searchText.value = item
                },
                label = {Text(label)},
//                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = dropdownExpanded.value
                    )
                },

//                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .padding(start = 5.dp, end = 16.dp)
                    .size(width = 500.dp, height = 50.dp)
                    .menuAnchor()//https://stackoverflow.com/questions/67111020/exposed-drop-down-menu-for-jetpack-compose
            )

            ExposedDropdownMenu(
                expanded = dropdownExpanded.value,
                onDismissRequest = { dropdownExpanded.value = false },
                modifier = Modifier.height(100.dp)
            ) {
                dropdownItemsState.value.forEach { item ->
                    Row(modifier = Modifier.padding(5.dp)) {
                        DropdownMenuItem(
                            text = {
                                Text(item)
                            },
                            onClick = {
                                selectedOption.value = item
                                searchText.value = selectedOption.value
                                dropdownExpanded.value = false
                            },
                            modifier = Modifier.width(125.dp)
                        )
                        Button(
                            onClick = {

                            },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text("刪除")
                        }
                    }
                }
                DropdownMenuItem(
                    text = {
                        Text(stringResource(R.string.add))
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = squirrelInfExpanded.value
                        )
                    },
                    onClick = {
                        selectedOption.value = "鼠害"
                        squirrelInfExpanded.value = true
                        dropdownExpanded.value = false
                    }
                )
            }

            ExposedDropdownMenu(
                expanded = squirrelInfExpanded.value,
                onDismissRequest = {squirrelInfExpanded.value = false}
            ) {
                squirrelInfItems.forEach {item ->
                    DropdownMenuItem(
                        text = {
                            Text(item)
                        },
                        onClick = {
                            selectedOption.value = "鼠害: $item"
                            squirrelInfExpanded.value = false
                        }
                    )
                }
            }
        }
    }
}