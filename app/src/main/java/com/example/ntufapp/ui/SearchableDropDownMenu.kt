package com.example.ntufapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchableDropdownMenu(
    options: List<String>,
    defaultString: String
) {
    val searchText = remember { mutableStateOf(defaultString) }
    val filteredOptions = remember { mutableStateOf(options) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val dropdownExpanded = remember { mutableStateOf(false) }
    val addExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(150.dp)
            .height(170.dp)
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
                onValueChange = { text ->
                    searchText.value = text
                    filterOptions(options, text).let {
                        filteredOptions.value = it
                    }
                },
                label = { Text("搜尋") },
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
                    .width(150.dp)
                    .height(70.dp)
            )
            // DropdownMenu
            ExposedDropdownMenu(
                expanded = dropdownExpanded.value,
                onDismissRequest = { dropdownExpanded.value = false }
            ) {

                filteredOptions.value.forEach { option ->

                    DropdownMenuItem(
                        text = {
                            Text(option)
                        },
                        onClick = {
                            searchText.value = option
                            dropdownExpanded.value = false
                        },
                        modifier = Modifier.width(120.dp)
                            .height(40.dp)
                    )


                    Divider(
                        color = Color.Black,
                        thickness = 1.dp,
                        modifier = Modifier.padding(1.dp)
                    )
                }
            }


//            Divider(
//                color = Color.Black,
//                thickness = 1.dp,
//                modifier = Modifier.padding(5.dp)
//            )
//            Box(
//                modifier = Modifier
//                    //                .background(Color.White)
//                    .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(8.dp))
//            ) {
//                LazyColumn(
//                    modifier = Modifier
//                        .padding(5.dp)
//                        .width(150.dp)
//                        .height(50.dp)
//                ) {
//                    itemsIndexed(filteredOptions.value) { idx, option ->
//                        ListItem(
//                            modifier = Modifier
//                                .width(150.dp)
//                                .height(40.dp)
//                                .clickable { searchText.value = option }
//                                .background(Color.Green),
//                            headlineContent = { Text(option) }
//                        )
//                        Divider(
//                            color = Color.Black,
//                            thickness = 1.dp,
//                            modifier = Modifier.padding(1.dp)
//                        )
//                    }
//                }
//            }
        }
    }
}