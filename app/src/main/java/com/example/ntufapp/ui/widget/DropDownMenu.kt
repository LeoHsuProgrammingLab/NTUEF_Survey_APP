import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateListOf
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
import com.example.ntufapp.api.dataType.plotsCatalogueResponse.Location
import com.example.ntufapp.ui.theme.DropdownDivider
import com.example.ntufapp.ui.theme.dropDownItemModifier
import com.example.ntufapp.ui.theme.dropDownMenuModifier

// This file is to store all the dropdown menu without searching function, simply select the item from the list

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PlotSelectionDropDownMenu(
    label: String,
    allPlotsInfo: Map<String, List<Pair<String, String>>>,
    onChoose: (String) -> Unit,
    widthType: String = "large",
) {
    val nameDropdownExpanded = remember { mutableStateOf(false) }
    val locationDropdownExpanded = remember { mutableStateOf(false) }
    val nameList = remember { mutableStateOf(allPlotsInfo.keys.toList()) }
    val name = remember { mutableStateOf(allPlotsInfo.keys.first()) }
    val locationList = remember {
        mutableStateOf(
            listOf("請選擇樣區") + (allPlotsInfo[name.value]?.map { pair -> pair.first } ?: emptyList())
        )
    }
    val location = remember { mutableStateOf("請選擇樣區") }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // ExposedDropdownMenuBox for the plot name
        ExposedDropdownMenuBox(
            expanded = nameDropdownExpanded.value,
            onExpandedChange = {
                nameDropdownExpanded.value = !nameDropdownExpanded.value
            },
            modifier = Modifier
                .height(80.dp)
                .padding(horizontal = 10.dp)
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            OutlinedTextField(
                value = name.value,
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
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = nameDropdownExpanded.value)
                },
                modifier = dropDownMenuModifier
                    .width(
                        when (widthType) {
                            "medium" -> 100.dp
                            "large" -> 600.dp
                            else -> 90.dp
                        }
                    )
                    .height(50.dp)
                    .menuAnchor()
                    .onFocusChanged { keyboardController?.hide() },
                textStyle = TextStyle(fontSize = 16.sp),
            )

            // DropdownMenu
            ExposedDropdownMenu(
                expanded = nameDropdownExpanded.value,
                onDismissRequest = { nameDropdownExpanded.value = false },
                modifier = dropDownMenuModifier
            ) {
                nameList.value.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(option)
                        },
                        onClick = {
                            nameDropdownExpanded.value = false
                            locationList.value = listOf("請選擇樣區") + (allPlotsInfo[option]?.map { pair -> pair.first }?.sorted() ?: emptyList())
                            name.value = option
                            location.value = locationList.value.firstOrNull() ?: "尚無"
                        },
                        modifier = dropDownItemModifier
                            .width(
                                when (widthType) {
                                    "medium" -> 100.dp
                                    "large" -> 600.dp
                                    else -> 90.dp
                                }
                            )
                    )
                    DropdownDivider()
                }
            }
        }

        // ExposedDropdownMenuBox for the plot name
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
                modifier = dropDownMenuModifier
                    .width(
                        when (widthType) {
                            "medium" -> 100.dp
                            "large" -> 600.dp
                            else -> 90.dp
                        }
                    )
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
                                allPlotsInfo[name.value]?.find { pair -> pair.first == option }?.second ?: ""
                            )
                        },
                        modifier = dropDownItemModifier
                            .width(
                                when (widthType) {
                                    "medium" -> 100.dp
                                    "large" -> 600.dp
                                    else -> 90.dp
                                }
                            )
                    )
                    DropdownDivider()
                }
            }
        }
    }
}