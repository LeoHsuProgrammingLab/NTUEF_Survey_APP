package com.example.ntufapp.layout

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.model.Tree
import com.example.ntufapp.model.compare2Plots
import com.example.ntufapp.ui.WindowInfo
import com.example.ntufapp.ui.rememberWindowInfo
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.md_theme_light_inverseOnSurface
import com.example.ntufapp.ui.theme.md_theme_light_primary
import java.lang.Math.abs

@Composable
fun ResultDisplayScreen(
    oldPlotData: PlotData,
    newPlotData: PlotData,
    onBackButtonClick: () -> Unit,
    onNextButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val buttonModifier = Modifier
            .padding(end = 16.dp)
            .height(50.dp)
        Row {
            TabbedValidationResult(
                oldPlotData = oldPlotData,
                newPlotData = newPlotData,
            )
            ScatterPlot(oldPlotData = oldPlotData, newPlotData = newPlotData)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = { onBackButtonClick() },
                modifier = buttonModifier
            ) {
                Text("繼續調查", fontSize = 18.sp)
            }
            OutlinedButton(
                onClick = { onNextButtonClick() },
                modifier = buttonModifier
            ) {
                Text("完成此次調查", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun TabbedValidationResult(
    oldPlotData: PlotData,
    newPlotData: PlotData,
) {
    val context = LocalContext.current
    val windowInfo = rememberWindowInfo()
    val height = when(windowInfo.screenHeightInfo) {
        WindowInfo.WindowType.Expanded -> 600.dp
        WindowInfo.WindowType.Medium -> 550.dp
        else -> 600.dp
    }

    val tabList = listOf("量測樹高", "目視樹高", "分岔樹高", "胸徑")
    val stringList = listOf("Meas", "Vis", "Fork", "DBH")
    val tabSelected = remember {
        mutableStateOf(0)
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .width(400.dp)
            .height(height)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .width(400.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabRow(
                selectedTabIndex = tabSelected.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                tabList.forEachIndexed { index, title ->
                    Tab(
                        modifier = Modifier
                            .padding(5.dp)
                            .width(100.dp),
                        selected = (tabSelected.value == index),
                        onClick = {
                            tabSelected.value = index
                        }
                    ) {
                        Text(title)
                    }
                }
            }
        }
        val dbhThreshold = 0.02
        val htThreshold = 0.1
        val NotValidDBHSet = remember {
            mutableStateOf(compare2Plots(oldPlotData, newPlotData, dbhThreshold, stringList[3]))
        }

        val NotValidMeasHtSet = remember {
            mutableStateOf(compare2Plots(oldPlotData, newPlotData, htThreshold, stringList[0]))
        }

        val NotValidVisHtSet = remember {
            mutableStateOf(compare2Plots(oldPlotData, newPlotData, htThreshold, stringList[1]))
        }

        val NotValidForkHtSet = remember {
            mutableStateOf(compare2Plots(oldPlotData, newPlotData, htThreshold, stringList[2]))
        }

        Box(
            modifier = Modifier
                .padding(10.dp)
                .width(400.dp)
                .height(500.dp)
                .border(border = BorderStroke(1.dp, md_theme_light_primary))
                .background(
                    color = md_theme_light_inverseOnSurface,
                    shape = Shapes.medium
                )
        ) {
            when (tabSelected.value) {
                0 -> {
                    ValidationLazyColumn(
                        NotValidTreeList = NotValidMeasHtSet,
                        oldPlotData = oldPlotData,
                        type = stringList[0],
                        threshold = htThreshold
                    )
                }
                1 -> {
                    ValidationLazyColumn(
                        NotValidTreeList = NotValidVisHtSet,
                        oldPlotData = oldPlotData,
                        type = stringList[1],
                        threshold = htThreshold
                    )
                }
                2 -> {
                    ValidationLazyColumn(
                        NotValidTreeList = NotValidForkHtSet,
                        oldPlotData = oldPlotData,
                        type = stringList[2],
                        threshold = htThreshold
                    )
                }
                3 -> {
                    ValidationLazyColumn(
                        NotValidTreeList = NotValidDBHSet,
                        oldPlotData = oldPlotData,
                        type = stringList[3],
                        threshold = dbhThreshold
                    )
                }
            }
        }
    }
}

@Composable
fun ValidationLazyColumn(
    NotValidTreeList: MutableState<MutableSet<Tree>>,
    oldPlotData: PlotData,
    type: String,
    threshold: Double
) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
    ) {
        itemsIndexed(NotValidTreeList.value.toList()) { index, tree ->
            Row {
                ListItem(
                    modifier = Modifier
                        .padding(10.dp)
                        .width(55.dp)
                        .height(55.dp)
                        .clip(CircleShape)
                        .background(md_theme_light_inverseOnSurface),
                    headlineContent = {
                        Text(
                            text = String.format("%02d", tree.SampleNum),
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .wrapContentHeight(Alignment.CenterVertically)
                        )
                    }
                )

                val treeDBH = remember { mutableStateOf(tree.DBH.toString()) }
                val treeMeasHt = remember { mutableStateOf(tree.MeasHeight.toString()) }
                val treeVisHt = remember { mutableStateOf(tree.VisHeight.toString()) }
                val treeForkHt = remember { mutableStateOf(tree.ForkHeight.toString()) }
                val changeChance = remember { mutableStateOf(3) }

                ValidationLazyColumnInputTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .width(100.dp),
                    textContent = when(type) {
                        "DBH" -> treeDBH
                        "Vis" -> treeVisHt
                        "Meas" -> treeMeasHt
                        else -> treeForkHt
                    },
                    changeChance = changeChance
                )

                OutlinedButton(
                    modifier = Modifier
                        .padding(10.dp)
                        .width(200.dp),
                    onClick = {
                        val newValue = when(type) {
                            "DBH" -> treeDBH
                            "Vis" -> treeVisHt
                            "Meas" -> treeMeasHt
                            else -> treeForkHt
                        }.value.toDoubleOrNull()
                        if(newValue != null) {
                            val oldValue = when(type) {
                                "DBH" -> oldPlotData.searchTree(tree.SampleNum)!!.DBH
                                "Vis" -> oldPlotData.searchTree(tree.SampleNum)!!.VisHeight
                                "Meas" -> oldPlotData.searchTree(tree.SampleNum)!!.MeasHeight
                                else -> oldPlotData.searchTree(tree.SampleNum)!!.ForkHeight
                            }
                            if(kotlin.math.abs(newValue - oldValue) <= threshold) {
                                NotValidTreeList.value.remove(tree)
                            }
                            if (changeChance.value > 0) {
                                changeChance.value -= 1
                            }
                        } else {
                            // alarm
                        }
                    }
                ) {
                    Text("修改(剩餘${changeChance.value}次)", style = TextStyle(color = Color.Red))
                }
            }
        }
    }
}

@Composable
fun ValidationLazyColumnInputTextField(
    modifier: Modifier = Modifier,
    textContent: MutableState<String>,
    changeChance: MutableState<Int>
) {
    TextField(
        value = textContent.value,
        onValueChange = {
            if(changeChance.value > 0) {
                textContent.value = it
            }
        },
        modifier = modifier,
        label = {
            Text("本次調查")
        },
        textStyle = TextStyle(fontSize = 14.sp)
    )
}

@Composable
fun ScatterPlot(
    oldPlotData: PlotData,
    newPlotData: PlotData
) {
    val context = LocalContext.current
    val windowInfo = rememberWindowInfo()
    val height = when (windowInfo.screenHeightInfo) {
        WindowInfo.WindowType.Medium -> 550.dp
        WindowInfo.WindowType.Expanded -> 600.dp
        else -> 600.dp
    }

    Box(
        modifier = Modifier
            .padding(10.dp)
            .width(800.dp)
            .height(height)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            // Create paint for each data series
            val forkHeightPaint = Paint().apply { color = Color.Red }
            val visHeightPaint = Paint().apply { color = Color.Green }
            val measHeightPaint = Paint().apply { color = Color.Magenta }

            drawRect(
                color = Color.Black,
                topLeft = Offset(0f, 0f),
                size = Size(size.width, size.height),
                style = Stroke(width = 4f)
            )

            var xAxisMax = 0f
            var xAxisMin = 200f
            var yAxisMax = 0f
            var yAxisMin = 200f
            for (i in oldPlotData.PlotTrees) {
                if(i.DBH > xAxisMax) {
                    xAxisMax = i.DBH.toFloat()
                } else {
                    if(i.DBH < xAxisMin) {
                        xAxisMin = i.DBH.toFloat()
                    }
                }

                if(i.VisHeight > yAxisMax) {
                    yAxisMax = i.VisHeight.toFloat()
                } else if (i.MeasHeight > yAxisMax) {
                    yAxisMax = i.MeasHeight.toFloat()
                } else if (i.ForkHeight > yAxisMax) {
                    yAxisMax = i.ForkHeight.toFloat()
                } else {
                    if(i.VisHeight < yAxisMin) {
                        yAxisMin = i.VisHeight.toFloat()
                    } else if (i.MeasHeight < yAxisMin) {
                        yAxisMin = i.MeasHeight.toFloat()
                    } else if (i.ForkHeight < yAxisMin) {
                        yAxisMin = i.ForkHeight.toFloat()
                    }
                }
            }
            xAxisMax = if (xAxisMax == 0f) {
                80f
            } else {
                val temp = xAxisMax.toInt() / 5
                (temp + 1) * 5f
            }

            yAxisMax = if (yAxisMax == 0f) {
                40f
            } else {
                val temp = yAxisMax.toInt() / 5
                (temp + 1) * 5f
            }

            xAxisMin = if (xAxisMin >= 5f) {
                val temp = xAxisMin.toInt() / 5
                (temp - 1) * 5f
            } else {
                0f
            }

            yAxisMin = if (yAxisMin >= 5f) {
                val temp = yAxisMin.toInt() / 5
                (temp - 1) * 5f
            } else {
                0f
            }

            // Draw scatter points for each data series
            drawDataPoints(oldPlotData.PlotTrees, xAxisMin, xAxisMax, yAxisMin, yAxisMax, forkHeightPaint, "Fork", context)
            drawDataPoints(oldPlotData.PlotTrees, xAxisMin, xAxisMax, yAxisMin, yAxisMax, visHeightPaint, "Visual", context)
            drawDataPoints(oldPlotData.PlotTrees, xAxisMin, xAxisMax, yAxisMin, yAxisMax, measHeightPaint,  "Measure", context)

            drawAxis(xAxisMin, xAxisMax, yAxisMin, yAxisMax)

            // Add regression lines for each data series (you can implement this separately)
//        drawRegressionLine(dbhData, heightData, xAxisMin, xAxisMax, yAxisMin, yAxisMax, dbhPaint)
//        drawRegressionLine(visHeightData, heightData, xAxisMin, xAxisMax, yAxisMin, yAxisMax, visHeightPaint)
//        drawRegressionLine(measHeightData, heightData, xAxisMin, xAxisMax, yAxisMin, yAxisMax, measHeightPaint)
        }
    }
}
// ref: https://proandroiddev.com/creating-graph-in-jetpack-compose-312957b11b2
private fun DrawScope.drawAxis(
    xMin: Float,
    xMax: Float,
    yMin: Float,
    yMax: Float,
    padding: Float = 150f,
    axisStrokeWidth: Float = 4f,
    strokeWidth: Float = 2f,
) {
    val axisLabelPaint = Paint().asFrameworkPaint().apply {
        color = Color.Black.toArgb()
        textSize = 30f
    }

    val axisNamePaint = Paint().asFrameworkPaint().apply {
        color = Color.Black.toArgb()
        textSize = 50f
    }

    val xRange = xMax - xMin
    val yRange = yMax - yMin

    val canvasWidth = size.width
    val canvasHeight = size.height
    val xAxisList = mutableListOf<Int>()
    for (i in 0 until xRange.toInt() + 1 step 5) {
        xAxisList.add(i + xMin.toInt())
    }

    val yAxisList = mutableListOf<Int>()
    for (i in 0 until yRange.toInt() + 1 step 5) {
        yAxisList.add(i + yMin.toInt())
    }

    val xAxisSpace = (canvasWidth - 2 * padding) / xAxisList.size
    val yAxisSpace = (canvasHeight - 2 * padding) / yAxisList.size
    // Draw X-axis
    drawLine(
        color = Color.Black,
        start = Offset(padding, canvasHeight - padding),
        end = Offset(canvasWidth - padding, canvasHeight - padding),
        strokeWidth = axisStrokeWidth
    )


    for (i in 0 until xAxisList.size) {
        drawContext.canvas.nativeCanvas.drawText(
            "${xAxisList[i]}",
            xAxisSpace * i + padding,
            size.height - padding / 2f,
            axisLabelPaint,
        )
    }

    // Draw Y-axis
    drawLine(
        color = Color.Black,
        start = Offset(padding,  canvasHeight - padding),
        end = Offset(padding, padding),
        strokeWidth = axisStrokeWidth,
        cap = StrokeCap.Round
    )

    for (i in 0 until yAxisList.size) {
        drawContext.canvas.nativeCanvas.drawText(
            "${yAxisList[i]}",
            padding / 4f,
            size.height - yAxisSpace * i - padding,
            axisLabelPaint,
        )
    }

    drawContext.canvas.nativeCanvas.drawText(
        "DBH",
        size.width - padding,
        size.height - padding / 2f,
        axisNamePaint
    )

    drawContext.canvas.nativeCanvas.drawText(
        "樹高",
        padding / 4f,
        padding,
        axisNamePaint
    )

}
private fun DrawScope.drawDataPoints(
    PlotTrees: List<Tree>,
    xMin: Float,
    xMax: Float,
    yMin: Float,
    yMax: Float,
    paint: Paint,
    yType: String,
    context: Context,
    padding: Float = 150f
) {
    val plotHeight = size.height - 2 * padding
    val plotWidth = size.width - 2 * padding

    val xRange = xMax - xMin
    val yRange = yMax - yMin

    showMessage(context, "xMin: $xMin, xMax: $xMax, yMin: $yMin, yMax: $yMax")
    for (i in PlotTrees.indices) {
//        showMessage(context, "DBH: ${PlotTrees[i].DBH}, xMin: $xMin, xRange: $xRange, width: ${size.width}")
        val x = (PlotTrees[i].DBH - xMin) / xRange * plotWidth + padding
        val y = when(yType) {
            "Visual" -> ((PlotTrees[i].VisHeight - yMin) / yRange * plotHeight).toFloat()
            "Measure" -> ((PlotTrees[i].MeasHeight - yMin) / yRange * plotHeight).toFloat()
            else -> ((PlotTrees[i].ForkHeight - yMin) / yRange * plotHeight).toFloat()
        }
//        showMessage(context, "x: $x, y: ${size.height - y}")
        drawCircle(
            color = paint.color,
            radius = 6f,
            center = Offset(x.toFloat(), (plotHeight + padding - y).toFloat())
        )
    }
}