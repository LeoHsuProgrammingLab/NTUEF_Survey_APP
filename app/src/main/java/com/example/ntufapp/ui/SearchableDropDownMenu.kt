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
// Can add new choices to the list
// Searchable with texts and use the dropdown menu to show the filtered options
fun SearchableAddMenu(
    totalItemsList: MutableList<String>,
    defaultString: String = "",
    label: String = "搜尋",
    dialogType: String = "Tree",
    readOnly: Boolean = false,
    keyboardType: KeyboardType,
    onChoose: (String) -> Unit,
    onAdd: (String) -> Unit,
    modifier: Modifier
) {
    val searchText = remember { mutableStateOf(defaultString) }
    val validSearchText = remember { mutableStateOf(defaultString) }
    val filteredOptions = remember { mutableStateListOf<String>() }
    val showDialog = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val dropdownExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(200.dp)
            .padding(vertical = 5.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = dropdownExpanded.value,
            onExpandedChange = {
                dropdownExpanded.value = !dropdownExpanded.value
                if (searchText.value !in totalItemsList) {
                    searchText.value = validSearchText.value
                } else {
                    validSearchText.value = searchText.value
                }
                filteredOptions.reset(totalItemsList)
                onChoose(searchText.value)
            }
        ) {
            val context = LocalContext.current
            OutlinedTextField(
                value = searchText.value,
                onValueChange = { text ->
                    filterOptions(totalItemsList, text).let {
                        filteredOptions.reset(it)
                    }
                    searchText.value = text
                    if (searchText.value in totalItemsList) {
                        onChoose(searchText.value)
                        validSearchText.value = searchText.value
                    } else if (searchText.value.isEmpty()) {
                        onChoose("1")
                        validSearchText.value = "1"
                    } else {
                        onChoose(validSearchText.value)
                        searchText.value = validSearchText.value
                        showMessage(context, "您搜尋的樹不存在!")
                    }
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
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "searchable")
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
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                modifier = modifier
                    .menuAnchor() // super important
                    .focusable(true), // for keyboard to work in the dropdown menu
            )
            // DropdownMenu
            ExposedDropdownMenu(
                expanded = dropdownExpanded.value,
                onDismissRequest = {},
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

                if (showDialog.value) {
                    AddDialog(
                        type = dialogType,
                        onDismiss = { /*showDialog.value = false*/ },
                        onCancelClick = { showDialog.value = false },
                        onNextButtonClick = {
                            showDialog.value = false
                            totalItemsList.add(it)
                            onAdd(it)
                        },
                        curSize = totalItemsList.size
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchableChooseMenu(
    totalItemsList: MutableList<String>,
    defaultString: String = "",
    label: String = "搜尋",
    readOnly: Boolean = false,
    onChoose: (String) -> Unit,
) {
    val speciesOptions = remember { mutableStateListOf<String>() }
    val searchText = remember { mutableStateOf(defaultString) }
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
                speciesOptions.reset(filterOptions(totalItemsList, searchText.value))
            }
        ) {
            OutlinedTextField(
                value = searchText.value,
                readOnly = readOnly,
                label = { Text(label) },
                onValueChange = { text ->
                    searchText.value = text
                    speciesOptions.reset(filterOptions(totalItemsList, text))
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
                singleLine = true,
                textStyle = TextStyle(fontSize = 13.sp),
                modifier = Modifier
                    .width(180.dp)
                    .height(80.dp)
                    .menuAnchor()
                    .focusable(true),
            )
            // DropdownMenu
            ExposedDropdownMenu(
                expanded = dropdownExpanded.value,
                onDismissRequest = { dropdownExpanded.value = false },
                modifier = Modifier.height(200.dp)
            ) {
                speciesOptions.forEach { option ->
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

fun filterOptions(options: List<String>, query: String): List<String> {
    if (query.isEmpty()) {
        return options
    }
    return options.filter { it.contains(query, ignoreCase = true) }
}

fun MutableList<String>.reset(options: List<String>) {
    clear()
    addAll(options)
}