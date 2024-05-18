package com.example.ntufapp.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.R
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.basicModifier
import com.example.ntufapp.ui.widget.PlotSelectionDropDownMenu
import com.example.ntufapp.utils.showMessage

@Composable
fun ChoosePlotToDownloadDialog(
    allPlotsInfo: Map<String, Map<String, List<Pair<String, String>>>>,
    onDownload: (String, String) -> Unit,
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit
) {
    val locationMid = remember { mutableStateOf("") }
    val deptName = remember { mutableStateOf("") }
    val context = LocalContext.current
    Dialog(
        onDismissRequest = { onDismiss.invoke() },
    ) {
        Surface(shape = Shapes.large) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogHeader(header = "請選擇樣區並下載")
                PlotSelectionDropDownMenu(
                    label = "",
                    allPlotsInfo = allPlotsInfo,
                    onChoose = { location_mid, dept_name ->
                        locationMid.value = location_mid
                        deptName.value = dept_name
                    }
                )

                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        modifier = basicModifier,
                        onClick = {
                            onCancelClick()
                        }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                    
                    Button(
                        modifier = basicModifier,
                        onClick = {
                            if (locationMid.value != "") {
                                onDownload(locationMid.value, deptName.value)
                            } else {
                                showMessage(context, "請選擇樣區!")
                            }
                        }
                    ) {
                        Text(stringResource(id = R.string.download))
                    }
                }
            }
        }
    }
}
