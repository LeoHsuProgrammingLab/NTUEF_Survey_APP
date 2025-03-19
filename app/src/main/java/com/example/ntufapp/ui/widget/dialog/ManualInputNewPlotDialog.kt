package com.example.ntufapp.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.R
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.basicModifier
import com.example.ntufapp.utils.showMessage

@Composable
fun ManualInputNewPlotDialog(
    onDismiss: () -> Unit,
    onSendClick: (PlotData) -> Unit,
) {
    val context = LocalContext.current

    val plotData = remember { mutableStateOf(PlotData()) }
    val manageUnit = remember { mutableStateOf("") }
    val subUnit = remember { mutableStateOf("") }
    val plotName = remember { mutableStateOf("") }
    val plotNum = remember { mutableStateOf("") }
    val plotType = remember { mutableStateOf("") }
    val plotArea = remember { mutableStateOf("") }
    val altitude= remember { mutableStateOf("") }
    val slope = remember { mutableStateOf("") }
    val aspect = remember { mutableStateOf("") }
    val TWD97_X = remember { mutableStateOf("") }
    val TWD97_Y = remember { mutableStateOf("") }

    // Function to check if plot data is valid
    fun isValidPlotData(): Boolean {
        return manageUnit.value.isNotBlank() &&
                subUnit.value.isNotBlank() &&
                plotName.value.isNotBlank() &&
                plotNum.value.isNotBlank() &&
                plotType.value.isNotBlank() &&
                plotArea.value.toDoubleOrNull() != null &&
                altitude.value.toDoubleOrNull() != null &&
                slope.value.toDoubleOrNull() != null &&
                aspect.value.isNotBlank() &&
                TWD97_X.value.isNotBlank() &&
                TWD97_Y.value.isNotBlank()
    }

    fun updatePlotData() {
        plotData.value.ManageUnit = manageUnit.value
        plotData.value.SubUnit = subUnit.value
        plotData.value.AreaKind = plotName.value
        plotData.value.AreaNum = plotNum.value
        plotData.value.PlotType = plotType.value
        plotData.value.PlotArea = plotArea.value.toDouble()
        plotData.value.Altitude = altitude.value.toDouble()
        plotData.value.Slope = slope.value.toDouble()
        plotData.value.Aspect = aspect.value
        plotData.value.TWD97_X = TWD97_X.value
        plotData.value.TWD97_Y = TWD97_Y.value
    }

    Dialog(
        onDismissRequest = {}
    ) {
        Surface(shape = Shapes.small) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogHeader(header = "請輸入樣區資料")
                LazyColumn(
                    modifier = basicModifier.height(500.dp)
                ) {
                    items(1) {
                        TextField(
                            value = manageUnit.value,
                            onValueChange = { text: String -> manageUnit.value = text },
                            label = { Text("營林區") }
                        )
                        TextField(
                            value = subUnit.value,
                            onValueChange = { text: String -> subUnit.value = text },
                            label = { Text("林班地") }
                        )
                        TextField(
                            value = plotName.value,
                            onValueChange = { text: String -> plotName.value = text },
                            label = { Text("樣區名稱") }
                        )
                        TextField(
                            value = plotNum.value,
                            onValueChange = { text: String -> plotNum.value = text },
                            label = { Text("樣區編號") }
                        )
                        TextField(
                            value = plotType.value,
                            onValueChange = { text: String -> plotType.value = text },
                            label = { Text("樣區型態") }
                        )
                        TextField(
                            value = plotArea.value,
                            onValueChange = { text: String -> plotArea.value = text },
                            label = { Text("樣區面積(m2)") },
                            placeholder = { Text("請輸入數字") }
                        )
                        TextField(
                            value = altitude.value,
                            onValueChange = { text: String -> altitude.value = text },
                            label = { Text("樣區海拔(m)") },
                            placeholder = { Text("請輸入數字") }
                        )
                        TextField(
                            value = slope.value,
                            onValueChange = { text: String -> slope.value = text },
                            label = { Text("樣區坡度") },
                            placeholder = { Text("請輸入數字") }
                        )
                        TextField(
                            value = aspect.value,
                            onValueChange = { text: String -> aspect.value = text },
                            label = { Text("樣區坡向") }
                        )
                        TextField(
                            value = TWD97_X.value,
                            onValueChange = { text: String -> TWD97_X.value = text },
                            label = { Text("TWD97_X") }
                        )
                        TextField(
                            value = TWD97_Y.value,
                            onValueChange = { text: String -> TWD97_Y.value = text },
                            label = { Text("TWD97_Y") }
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = basicModifier,
                        onClick = onDismiss
                    ) {
                        Text(stringResource(id = (R.string.cancel)))
                    }
                    Button(
                        modifier = basicModifier,
                        onClick = {
                            if (isValidPlotData()) {
                                updatePlotData()
                                onSendClick(plotData.value)
                            } else {
                                showMessage(context, "請輸入正確且完整的樣區資料")
                            }
                        }
                    ) {
                        Text(stringResource(id = (R.string.next)))
                    }
                }
            }
        }
    }
}



