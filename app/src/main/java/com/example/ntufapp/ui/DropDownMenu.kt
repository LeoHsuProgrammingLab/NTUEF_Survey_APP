package com.example.ntufapp.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.ntufappInfo.Companion.tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun treeStateMenu(label: String, treeState: MutableState<String>): MutableState<String> {
    val dropdownExpanded = remember {
        mutableStateOf(false)
    }
    val dropdownItems = listOf("正常", "風倒", "風折", "枯立", "中空")

    val selectedOption = remember() {
        mutableStateOf(dropdownItems[0])
    }

    val squirrelInfExpanded = remember {
        mutableStateOf(false)
    }
    val squirrelInfItems = listOf("早期", "中期", "晚期")

    Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        Box(
            modifier = Modifier.size(width = 100.dp, height = 100.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Text(label, modifier = Modifier.padding(end = 0.dp), textAlign = TextAlign.Start, fontSize = 20.sp)
        }

        ExposedDropdownMenuBox(
            expanded = dropdownExpanded.value,
            onExpandedChange = {
                dropdownExpanded.value = !dropdownExpanded.value
                squirrelInfExpanded.value = false
            }
        ) {
            OutlinedTextField(
                value = selectedOption.value,
                onValueChange = {
                    treeState.value = it
                },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = dropdownExpanded.value
                    )
                },

//                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .padding(start = 5.dp, end = 16.dp)
                    .size(width = 150.dp, height = 50.dp)
                    .menuAnchor()//https://stackoverflow.com/questions/67111020/exposed-drop-down-menu-for-jetpack-compose
            )

            ExposedDropdownMenu(
                expanded = dropdownExpanded.value,
                onDismissRequest = { dropdownExpanded.value = false }
            ) {
                dropdownItems.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(item)
                        },
                        onClick = {
                            selectedOption.value = item
                            dropdownExpanded.value = false
                        }
                    )
                }
                DropdownMenuItem(
                    text = {
                        Text("鼠害")
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

    return selectedOption
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun treeSpeciesMenu(label: String): MutableState<String> {
    val dropdownExpanded = remember {
        mutableStateOf(false)
    }
    val dropdownItems = listOf("正常", "風倒", "風折", "枯立", "中空")
//    selectedOption.value =
    val selectedOption = remember {
        mutableStateOf(dropdownItems[0])
    }

    val squirrelInfExpanded = remember {
        mutableStateOf(false)
    }
    val squirrelInfItems = listOf("早期", "中期", "晚期")

    fun getTreeState(): String {
        return selectedOption.value
    }

    Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        Box(
            modifier = Modifier.size(width = 100.dp, height = 100.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Text(label, modifier = Modifier.padding(end = 0.dp), textAlign = TextAlign.Start, fontSize = 20.sp)
        }

        ExposedDropdownMenuBox(
            expanded = dropdownExpanded.value,
            onExpandedChange = {
                dropdownExpanded.value = !dropdownExpanded.value
                squirrelInfExpanded.value = false
            }
        ) {
            OutlinedTextField(
                value = selectedOption.value,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = dropdownExpanded.value
                    )
                },

//                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .padding(start = 5.dp, end = 16.dp)
                    .size(width = 150.dp, height = 50.dp)
                    .menuAnchor()//https://stackoverflow.com/questions/67111020/exposed-drop-down-menu-for-jetpack-compose
            )

            ExposedDropdownMenu(
                expanded = dropdownExpanded.value,
                onDismissRequest = { dropdownExpanded.value = false }
            ) {
                dropdownItems.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(item)
                        },
                        onClick = {
                            selectedOption.value = item
                            dropdownExpanded.value = false
                        }
                    )
                }
                DropdownMenuItem(
                    text = {
                        Text("鼠害")
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
    return selectedOption
}