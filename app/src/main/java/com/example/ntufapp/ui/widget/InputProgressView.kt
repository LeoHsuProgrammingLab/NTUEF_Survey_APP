package com.example.ntufapp.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.lightBorder
import com.example.ntufapp.ui.theme.md_theme_light_inverseOnSurface
import com.example.ntufapp.ui.theme.md_theme_light_primaryContainer


@Composable
fun InputProgressView(
    currentTreeNum: String,
    newPlotData: PlotData
) {
    val dbhState = remember {
        mutableDoubleStateOf(0.0)
    }

    val htState = remember {
        mutableDoubleStateOf(0.0)
    }

    val visHtState = remember {
        mutableDoubleStateOf(0.0)
    }

    val forkHtState = remember {
        mutableDoubleStateOf(0.0)
    }
        Row {
            Column(
                modifier = Modifier.width(320.dp)
            ) {
                InputRow(label = "DBH",
                    onClick = {
                        dbhState.doubleValue = it
                        newPlotData.searchTree(currentTreeNum.toInt())!!.DBH = it
                    }
                )
                InputRow(label = "目視樹高",
                    onClick = {
                        visHtState.doubleValue = it
                        newPlotData.searchTree(currentTreeNum.toInt())!!.VisHeight = it
                    }
                )
                InputRow(label = "量測樹高",
                    onClick = {
                        htState.doubleValue = it
                        newPlotData.searchTree(currentTreeNum.toInt())!!.MeasHeight = it
                    }
                )
                InputRow(label = "分岔樹高",
                    onClick = {
                        forkHtState.doubleValue = it
                        newPlotData.searchTree(currentTreeNum.toInt())!!.ForkHeight = it
                    }
                )
            }

        Progress(
            dbh = dbhState.doubleValue,
            ht = htState.doubleValue,
            visHt = visHtState.doubleValue,
            forkHt = forkHtState.doubleValue,
            newPlotData = newPlotData
        )

    }
}

@Composable
fun Progress(
    dbh: Double,
    ht: Double,
    visHt: Double,
    forkHt: Double,
    newPlotData: PlotData
) {
    Box(
        modifier = Modifier
            .padding(20.dp)
            .background(md_theme_light_primaryContainer, Shapes.medium)
            .border(lightBorder, Shapes.medium)
            .fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .background(md_theme_light_primaryContainer)
        ) {
            itemsIndexed(newPlotData.PlotTrees) { idx, tree ->
                Row(
                    modifier = Modifier.padding(5.dp)
                ) {
                    ListItem(
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .clip(CircleShape)
                            .background(md_theme_light_inverseOnSurface),
                        headlineContent = {
                            Text(
                                tree.SampleNum.toString()
                            )
                        }
                    )
                    Row(
                        modifier = Modifier.padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val mod = Modifier
                            .padding(5.dp)
                            .size(width = 90.dp, height = 50.dp)

                        TextField(
                            value = "${tree.DBH}",
                            onValueChange = {},
                            label = { Text("DBH") },
                            modifier = mod,
                            readOnly = true
                        )
                        TextField(
                            value = "${tree.VisHeight}",
                            onValueChange = {},
                            label = { Text("目視樹高") },
                            modifier = mod,
                            readOnly = true
                        )
                        TextField(
                            value = "${tree.MeasHeight}",
                            onValueChange = {},
                            label = { Text("量測樹高") },
                            modifier = mod,
                            readOnly = true
                        )
                        TextField(
                            value = "${tree.ForkHeight}",
                            onValueChange = {},
                            label = { Text("分岔樹高") },
                            modifier = mod,
                            readOnly = true
                        )
                    }
                }

                Divider(
                    color = Color.Black,
                    thickness = 1.dp,
                    modifier = Modifier.padding(1.dp)
                )
            }
        }
    }
}

@Composable
fun InputRow(
    label: String,
    onClick: (Double)->Unit
) {

    val inputState = remember {
        mutableStateOf("")
    }
    Box(
        modifier = Modifier.padding(10.dp)
    ) {
        Row {
            TextField(
                value = inputState.value,
                onValueChange = {
                    inputState.value = it
                },
                readOnly = false,
                label = { Text(label) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 10.dp)
            )
            Button(
                modifier = Modifier
                    .width(90.dp)
                    .padding(top = 15.dp, start = 5.dp),
                onClick = {
                    onClick(inputState.value.toDouble())
                }
            ) {
                Text("新增")
            }
        }
    }
}
