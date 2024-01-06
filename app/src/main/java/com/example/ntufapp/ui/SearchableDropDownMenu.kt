package com.example.ntufapp.ui

import android.util.Log
import androidx.compose.foundation.focusable
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.example.ntufapp.data.ntufappInfo.Companion.dTag
import com.example.ntufapp.ui.theme.DropdownDivider
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.dropDownItemModifier
import com.example.ntufapp.ui.theme.dropDownMenuModifier
import com.example.ntufapp.utils.showMessage

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchableDropdownMenu(
    totalTreesNumList: MutableList<String>,
    defaultString: String = "",
    label: String = "搜尋",
    dialogType: String = "Tree",
    readOnly: Boolean = false,
    keyboardType: KeyboardType,
    onChoose: (String) -> Unit,
    onAdd: (String) -> Unit
) {
    val searchText = remember { mutableStateOf(defaultString) }
    val validSearchText = remember { mutableStateOf(defaultString) }
    val filteredOptions = remember { mutableStateListOf<String>() }
    val showDialog = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val dropdownExpanded = remember { mutableStateOf(false) }
    val addExpanded = remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        expanded = dropdownExpanded.value,
        onExpandedChange = {
            dropdownExpanded.value = !dropdownExpanded.value
            if (searchText.value !in totalTreesNumList) {
                searchText.value = validSearchText.value
            } else {
                validSearchText.value = searchText.value
            }
            filteredOptions.clear()
            filteredOptions.addAll(totalTreesNumList)
            onChoose(searchText.value)
            addExpanded.value = false
            Log.i(dTag, "dropdownExpanded.value: ${dropdownExpanded.value}")
        }
    ) {
        val curContext = LocalContext.current
        OutlinedTextField(
            value = searchText.value,
            onValueChange = { text ->
                filterOptions(totalTreesNumList, text).let {
                    filteredOptions.clear()
                    filteredOptions.addAll(it)
                }
                searchText.value = text
                if (searchText.value in totalTreesNumList) {
                    onChoose(searchText.value)
                    validSearchText.value = searchText.value
                } else if (searchText.value.isEmpty()) {
                    onChoose("1")
                } else {
                    onChoose(validSearchText.value)
                    searchText.value = validSearchText.value
                    showMessage(curContext, "您搜尋的樹不存在!")
                }
                Log.i(dTag, "dropdown searchText.value: ${searchText.value}")
            },
            label = {
                Text(
                    text = label,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
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
                keyboardType = keyboardType
            ),
            readOnly = readOnly,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 18.sp
            ),
            modifier = Modifier
                .width(150.dp)
                .height(75.dp)
                .menuAnchor() // super important
                .focusable(true)
        )
        // DropdownMenu
        ExposedDropdownMenu(
            expanded = dropdownExpanded.value,
            onDismissRequest = {
                dropdownExpanded.value = false
            },
            modifier = dropDownMenuModifier
        ) {
            if (filteredOptions.isEmpty()) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.no_result)) },
                    onClick = {},
                    modifier = dropDownItemModifier
                )
                DropdownDivider()
            } else {
                filteredOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            validSearchText.value = option
                            searchText.value = option
                            dropdownExpanded.value = false
                            onChoose(option)
                        },
                        modifier = dropDownItemModifier
                    )
                    DropdownDivider()
                }
            }

            DropdownMenuItem(
                text = { Text(stringResource(R.string.add)) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Add, // Replace with your desired icon
                        contentDescription = "Add Icon",
                    )
                },
                onClick = { showDialog.value = true }
            )

            if(showDialog.value) {
                AddDialog(
                    type = dialogType,
                    onDismiss = { /*showDialog.value = false*/ },
                    onCancelClick = { showDialog.value = false },
                    onNextButtonClick = {
                        showDialog.value = false
                        totalTreesNumList.add(it)
                        onAdd(it)
                    },
                    curSize = totalTreesNumList.size
                )
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
                        modifier = Modifier
                            .width(150.dp)
                            .height(40.dp)
                    )

                    DropdownDivider()
                }
            }
        }
    }
}

@Composable
fun AddDialog(
    type: String = "tree",
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    onNextButtonClick: (String) -> Unit,
    curSize: Int
){
    val modifier = Modifier.padding(10.dp)
    when(type) {
        "Tree" ->
            Dialog(
                onDismissRequest = {
                    onDismiss.invoke()
                }
            ) {
                Surface(shape = Shapes.small) {
                    Column(modifier = modifier) {
                        Text(text = "您預計新增第 ${curSize + 1} 棵樹")
                        Spacer(modifier = modifier)

                        Row{
                            Button(
                                modifier = modifier,
                                onClick = onCancelClick,
                            ) {
                                Text(stringResource(id = (R.string.cancel)))
                            }

                            Button(
                                modifier = modifier,
                                onClick = { onNextButtonClick((curSize + 1).toString()) }
                            ) {
                                Text(stringResource(id = (R.string.next)))
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
                    Column(modifier = modifier) {
                        // TODO: DropDownMenu

                        Spacer(modifier = modifier)

                        Row{
                            Button(
                                modifier = modifier,
                                onClick = onCancelClick,
                            ) {
                                Text(stringResource(id = (R.string.cancel)))
                            }

                            Button(
                                modifier = modifier,
                                onClick = {
                                    onNextButtonClick((curSize + 1).toString())
                                }
                            ) {
                                Text(stringResource(id = (R.string.next)))
                            }
                        }
                    }
                }
            }
    }
}

fun filterOptions(options: List<String>, query: String): List<String> {
    return options.filter { it.contains(query, ignoreCase = true) }
}