package com.example.ntufapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.R
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.Shapes

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchableDropdownMenu(
    optionsInput: MutableList<String>,
    defaultString: String = "",
    label: String = "搜尋",
    dialogType: String = "Tree",
    readOnly: Boolean = false,
    onChoose: (String) -> Unit,
    onAdd: (String) -> Unit
) {
    val options = remember {
        mutableStateOf(optionsInput)
    }
    val searchText = remember { mutableStateOf(defaultString) }
    val filteredOptions = remember { mutableStateOf(options.value) }
    val showDialogue = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val dropdownExpanded = remember { mutableStateOf(false) }
    val addExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(vertical = 10.dp, horizontal = 5.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = dropdownExpanded.value,
            onExpandedChange = {
                dropdownExpanded.value = !dropdownExpanded.value
                addExpanded.value = false
            }
        ) {
            OutlinedTextField(
                value = searchText.value,
                readOnly = readOnly,
                label = {
                    Text(
                        text = label,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                onValueChange = { text ->
                    filterOptions(options.value, text).let {
                        filteredOptions.value = it as MutableList<String>
                    }
                    searchText.value = text
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = dropdownExpanded.value
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                    }
                ),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .width(150.dp)
                    .height(75.dp)
                    .menuAnchor() // super important
            )
            // DropdownMenu
            ExposedDropdownMenu(
                expanded = dropdownExpanded.value,
                onDismissRequest = { dropdownExpanded.value = false },
                modifier = Modifier.height(200.dp)
            ) {

                filteredOptions.value.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(option)
                        },
                        onClick = {
                            searchText.value = option
                            dropdownExpanded.value = false
                            onChoose(option)
                        },
                        modifier = Modifier
                            .width(120.dp)
                            .height(40.dp)
                    )


                    Divider(
                        color = Color.Black,
                        thickness = 1.dp,
                        modifier = Modifier.padding(1.dp)
                    )
                }

                DropdownMenuItem(
                    text = {
                        Text(stringResource(R.string.add))
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add, // Replace with your desired icon
                            contentDescription = "Add Icon",
                        )
                    },
                    onClick = {
                        showDialogue.value = true
                    }
                )

                if(showDialogue.value) {
                    AddDialogue(
                        type = dialogType,
                        onDismiss = {
                            showDialogue.value = false
                        },
                        onCancelClick = {
                            showDialogue.value = false
                        },
                        onNextButtonClick = {
                            showDialogue.value = false
                            options.value.add(it)
                            onAdd(it)
                        },
                        curSize = options.value.size
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchableDropdownMenu2(
    optionsInput: MutableList<String>,
    defaultString: String = "",
    label: String = "搜尋",
    readOnly: Boolean = false,
    onChoose: (String) -> Unit,
) {
    val options = remember {
        mutableStateOf(optionsInput)
    }
    val searchText = remember { mutableStateOf(defaultString) }
    val filteredOptions = remember { mutableStateOf(options.value) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val dropdownExpanded = remember { mutableStateOf(false) }
    val addExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(200.dp)
            .padding(vertical = 10.dp, horizontal = 5.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = dropdownExpanded.value,
            onExpandedChange = {
                dropdownExpanded.value = !dropdownExpanded.value
                addExpanded.value = false
            }
        ) {
            OutlinedTextField(
                value = searchText.value,
                readOnly = readOnly,
                label = {Text(label)},
                onValueChange = { text ->
                    filterOptions(options.value, text).let {
                        filteredOptions.value = it as MutableList<String>
                    }
                    searchText.value = text
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = dropdownExpanded.value
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                    }
                ),
                singleLine = true,
                textStyle = TextStyle(fontSize = 13.sp),
                modifier = Modifier
                    .width(180.dp)
                    .height(80.dp)
                    .menuAnchor()
            )
            // DropdownMenu
            ExposedDropdownMenu(
                expanded = dropdownExpanded.value,
                onDismissRequest = { dropdownExpanded.value = false },
                modifier = Modifier.height(200.dp)
            ) {
                filteredOptions.value.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(option)
                        },
                        onClick = {
                            searchText.value = option
                            dropdownExpanded.value = false
                            onChoose(option)
                        },
                        modifier = Modifier.width(150.dp).height(40.dp)
                    )

                    Divider(
                        color = Color.Black,
                        thickness = 1.dp,
                        modifier = Modifier.padding(1.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddDialogue(
    type: String = "tree",
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    onNextButtonClick: (String) -> Unit,
    curSize: Int
){
    when(type) {
        "Tree" ->
            Dialog(
                onDismissRequest = {
                    onDismiss.invoke()
                }
            ) {
                Surface(shape = Shapes.small) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(text = "您預計新增第 ${curSize + 1} 棵樹")
                        Spacer(modifier = Modifier.padding(10.dp))

                        Row{
                            Button(
                                modifier = Modifier.padding(10.dp),
                                onClick = onCancelClick,
                            ) {
                                Text("取消")
                            }

                            Button(
                                modifier = Modifier.padding(10.dp),
                                onClick = {
                                    onNextButtonClick((curSize + 1).toString())
                                }
                            ) {
                                Text("下一步")
                            }
                        }
                    }
                }
            }
        "Name" ->
            Dialog(
                onDismissRequest = {
                    onDismiss.invoke()
                }
            ) {
                Surface(shape = Shapes.small) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        // TODO: DropDownMenu

                        Spacer(modifier = Modifier.padding(10.dp))

                        Row{
                            Button(
                                modifier = Modifier.padding(10.dp),
                                onClick = onCancelClick,
                            ) {
                                Text("取消")
                            }

                            Button(
                                modifier = Modifier.padding(10.dp),
                                onClick = {
                                    onNextButtonClick((curSize + 1).toString())
                                }
                            ) {
                                Text("下一步")
                            }
                        }
                    }
                }
            }
    }
}

fun filterOptions(options: List<String>, query: String): List<String> {
    return options.filter { it.contains(query, ignoreCase = true) }.ifEmpty {
        listOf("查無此資料")
    }
}