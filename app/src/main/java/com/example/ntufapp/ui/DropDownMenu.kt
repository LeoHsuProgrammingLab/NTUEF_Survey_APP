package com.example.ntufapp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.R
import com.example.ntufapp.ui.theme.md_theme_light_primary

@Composable
fun SurveyorAddDeleteMenu() {
    val optionText = remember { mutableStateOf("") }
    val options = remember { mutableStateOf(mutableListOf<String>()) }

    options.value = mutableListOf("John", "Leo", "Alex")

    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = optionText.value,
                    onValueChange = { optionText.value = it },
                    textStyle = TextStyle.Default.copy(color = md_theme_light_primary),
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        options.value.add(optionText.value)
                        optionText.value = "新增"
                        optionText.value = ""
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("新增選項")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            options.value.forEachIndexed { index, option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BasicTextField(
                        value = TextFieldValue(option),
                        onValueChange = {},
                        textStyle = TextStyle.Default.copy(color = md_theme_light_primary),
                        enabled = false,
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = {
                            options.value.removeAt(index)
                            optionText.value = "新成員"
                            optionText.value = ""
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("刪除")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun HeightSurveyorMenu() {

}
@Composable
fun SpeciesMenu() {

}

fun filterOptions(options: List<String>, query: String): List<String> {
    return options.filter { it.contains(query, ignoreCase = true) }.ifEmpty {
        listOf("查無此資料")
    }
}
@Composable
fun DropDowItems(
    items: List<String>,
    onOptionSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(5.dp)
            .width(150.dp)
            .height(50.dp)
    ) {
//        items.forEach { item ->
//            DropdownMenuItem(
//                onClick = {
//                    onOptionSelected(item)
//                },
//                text = {Text(item)}
//            )
//        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchDropdownMenu(
    items: List<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    leadingIcon: ImageVector? = null
) {
    var expanded = remember { mutableStateOf(false) }
    var searchText = remember { mutableStateOf("") }
    val filteredItems = remember{
        mutableStateOf(items)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = searchText.value,
        onValueChange = { searchText.value = it },
        label = { label?.let { Text(it) } },
        trailingIcon = {
            IconButton(onClick = { expanded.value = !expanded.value }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon")
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),

        keyboardActions = KeyboardActions(
            onDone = {
                expanded.value = false
                keyboardController?.hide()
            }
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )

    if (expanded.value) {
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                itemsIndexed(filteredItems.value) { id, item ->
                    DropdownMenuItem(
                        text = {Text(text = item)},
                        onClick = {
                            onItemSelected(item)
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}