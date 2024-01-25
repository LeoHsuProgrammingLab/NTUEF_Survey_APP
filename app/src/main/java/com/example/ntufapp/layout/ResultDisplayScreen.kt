package com.example.ntufapp.layout

import android.content.Context
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.R
import com.example.ntufapp.data.ntufappInfo.Companion.dbhThreshold
import com.example.ntufapp.data.ntufappInfo.Companion.htThreshold
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.model.Tree
import com.example.ntufapp.model.checkThreshold
import com.example.ntufapp.model.compareTwoPlots
import com.example.ntufapp.model.createTreeQuotaPair
import com.example.ntufapp.ui.widget.WindowInfo
import com.example.ntufapp.ui.widget.rememberWindowInfo
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.circleThreeDigitsModifier
import com.example.ntufapp.ui.theme.lightBorder
import com.example.ntufapp.ui.theme.md_theme_light_inverseOnSurface
import com.example.ntufapp.utils.maxThree
import com.example.ntufapp.utils.minThree
import com.example.ntufapp.utils.showMessage

@Composable
fun ResultDisplayScreen(
    oldPlotData: PlotData,
    newPlotData: PlotData,
    onBackButtonClick: () -> Unit = {},
    onNextButtonClick: () -> Unit,
    from: String
) {
//    DisableBackButtonHandler()
    BackHandler(enabled = true) {}

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val buttonModifier = Modifier
            .padding(top = 10.dp, end = 30.dp)
            .height(50.dp)
        // Main layout
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            if (from == "ReSurvey") {
                TabbedValidationResult(oldPlotData = oldPlotData, newPlotData = newPlotData)
            } else {
                TabbedResult(newPlotData = newPlotData)
            }
            ScatterPlot(plotData = newPlotData)
        }

        // Check Button
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
                Text(stringResource(id = (R.string.next)), fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun TabbedResult(
    newPlotData: PlotData,
) {
    val windowInfo = rememberWindowInfo()
    val height = when(windowInfo.screenHeightInfo) {
        WindowInfo.WindowType.Expanded -> 600.dp
        WindowInfo.WindowType.Medium -> 550.dp
        else -> 600.dp
    }
    val modifier = Modifier
        .padding(10.dp)
        .width(450.dp)

    val tabList = listOf("量測樹高", "目視樹高", "分岔樹高", "胸徑", "樹種", "生長狀況")
    val stringList = listOf("Meas", "Vis", "Fork", "DBH", "Species", "Condition")
    val tabSelected = remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .height(height),
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
                    onClick = { tabSelected.value = index }
                ) {
                    Text(
                        title,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .wrapContentHeight(Alignment.CenterVertically),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Box(
            modifier = modifier
                .height(500.dp)
                .border(border = lightBorder)
                .background(
                    color = md_theme_light_inverseOnSurface,
                    shape = Shapes.medium
                )
        ) {
            when (tabSelected.value) {
                in 0..5 -> {
                    ResultLazyColumn(
                        treeList = newPlotData.PlotTrees,
                        type = stringList[tabSelected.value]
                    )
                }
            }
        }
    }
}

@Composable
fun ResultLazyColumn(
    treeList: List<Tree>,
    type: String
) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
    ) {
        itemsIndexed(treeList) { id, tree ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ListItem(
                    modifier = circleThreeDigitsModifier,
                    headlineContent = {
                        Text(
                            text = String.format("%03d", tree.SampleNum),
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

                TextField(
                    value = when (type) {
                        "DBH" -> tree.DBH.toString()
                        "Vis" -> tree.VisHeight.toString()
                        "Meas" -> tree.MeasHeight.toString()
                        "Fork" -> tree.ForkHeight.toString()
                        "Species" -> tree.Species
                        else -> tree.State.joinToString()
                    },
                    readOnly = true,
                    onValueChange = {},
                    modifier = Modifier
                        .padding(10.dp),
                    label = { Text("調查結果", fontSize = 13.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(fontSize = 18.sp)
                )
            }
        }
    }
}

@Composable
fun TabbedValidationResult(
    oldPlotData: PlotData,
    newPlotData: PlotData,
) {
    val windowInfo = rememberWindowInfo()
    val height = when(windowInfo.screenHeightInfo) {
        WindowInfo.WindowType.Expanded -> 600.dp
        WindowInfo.WindowType.Medium -> 550.dp
        else -> 600.dp
    }
    val modifier = Modifier
        .padding(10.dp)
        .width(450.dp)

    val tabList = listOf("量測樹高", "目視樹高", "分岔樹高", "胸徑")
    val stringList = listOf("Meas", "Vis", "Fork", "DBH")
    val tabSelected = remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .height(height)
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

        val invalidDBHSet = remember { mutableStateOf(compareTwoPlots(oldPlotData, newPlotData, dbhThreshold, stringList[3])) }
        val invalidMeasHtSet = remember { mutableStateOf(compareTwoPlots(oldPlotData, newPlotData, htThreshold, stringList[0])) }
        val invalidVisHtSet = remember { mutableStateOf(compareTwoPlots(oldPlotData, newPlotData, htThreshold, stringList[1])) }
        val invalidForkHtSet = remember { mutableStateOf(compareTwoPlots(oldPlotData, newPlotData, htThreshold, stringList[2])) }
        val invalidDBHQuota = remember { mutableStateOf(createTreeQuotaPair(invalidDBHSet.value)) }
        val invalidMeasHtQuota = remember { mutableStateOf(createTreeQuotaPair(invalidMeasHtSet.value)) }
        val invalidVisHtQuota = remember { mutableStateOf(createTreeQuotaPair(invalidVisHtSet.value)) }
        val invalidForkHtQuota = remember { mutableStateOf(createTreeQuotaPair(invalidForkHtSet.value)) }

        Box(
            modifier = modifier
                .height(500.dp)
                .border(border = lightBorder)
                .background(
                    color = md_theme_light_inverseOnSurface,
                    shape = Shapes.medium
                )
        ) {
            when (tabSelected.value) {
                0 -> {
                    ValidationLazyColumn(
                        inValidTreeQuotaMap = invalidDBHQuota,
                        inValidTreeList = invalidMeasHtSet,
                        oldPlotData = oldPlotData,
                        type = stringList[0],
                        threshold = htThreshold
                    )
                }
                1 -> {
                    ValidationLazyColumn(
                        inValidTreeQuotaMap = invalidVisHtQuota,
                        inValidTreeList = invalidVisHtSet,
                        oldPlotData = oldPlotData,
                        type = stringList[1],
                        threshold = htThreshold
                    )
                }
                2 -> {
                    ValidationLazyColumn(
                        inValidTreeQuotaMap = invalidForkHtQuota,
                        inValidTreeList = invalidForkHtSet,
                        oldPlotData = oldPlotData,
                        type = stringList[2],
                        threshold = htThreshold
                    )
                }
                3 -> {
                    ValidationLazyColumn(
                        inValidTreeQuotaMap = invalidMeasHtQuota,
                        inValidTreeList = invalidDBHSet,
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
    inValidTreeQuotaMap: MutableState<MutableMap<Int, Int>>,
    inValidTreeList: MutableState<MutableSet<Tree>>,
    oldPlotData: PlotData,
    type: String,
    threshold: Double
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
    ) {
        itemsIndexed(inValidTreeList.value.toList()) { id, tree ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ListItem(
                    modifier = circleThreeDigitsModifier,
                    headlineContent = {
                        Text(
                            text = String.format("%03d", tree.SampleNum),
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

                // Store the value re-measured then check if it is valid
                val treeDBH = remember { mutableStateOf(tree.DBH.toString()) }
                val treeMeasHt = remember { mutableStateOf(tree.MeasHeight.toString()) }
                val treeVisHt = remember { mutableStateOf(tree.VisHeight.toString()) }
                val treeForkHt = remember { mutableStateOf(tree.ForkHeight.toString()) }
                val treeQuota = remember { mutableStateOf(inValidTreeQuotaMap.value[tree.SampleNum]) }

                ValidationLazyColumnInputTextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .width(120.dp),
                    textContent = when(type) {
                        "DBH" -> treeDBH
                        "Vis" -> treeVisHt
                        "Meas" -> treeMeasHt
                        else -> treeForkHt
                    },
                    changeQuota = inValidTreeQuotaMap.value[tree.SampleNum]!!
                )

                OutlinedButton(
                    modifier = Modifier
                        .padding(10.dp)
                        .width(150.dp),
                    onClick = {
                        val newValue = when(type) {
                            "DBH" -> treeDBH
                            "Vis" -> treeVisHt
                            "Meas" -> treeMeasHt
                            else -> treeForkHt
                        }.value.toDoubleOrNull()

                        if(newValue != null) {
                            // Update by the new value
                            when(type) {
                                "DBH" -> tree.DBH = newValue
                                "Vis" -> tree.VisHeight = newValue
                                "Meas" -> tree.MeasHeight = newValue
                                else -> tree.ForkHeight = newValue
                            }
                            // Get the old value
                            val oldValue = when(type) {
                                "DBH" -> oldPlotData.getTreeBySampleNum(tree.SampleNum)!!.DBH
                                "Vis" -> oldPlotData.getTreeBySampleNum(tree.SampleNum)!!.VisHeight
                                "Meas" -> oldPlotData.getTreeBySampleNum(tree.SampleNum)!!.MeasHeight
                                else -> oldPlotData.getTreeBySampleNum(tree.SampleNum)!!.ForkHeight
                            }
                            // Check if the new value is valid
                            if(checkThreshold(oldValue, newValue, threshold)) {
                                inValidTreeList.value.remove(tree)
                            }
                            // Update the quota
                            if (treeQuota.value!! > 0) {
                                treeQuota.value = treeQuota.value!! - 1
                                inValidTreeQuotaMap.value[tree.SampleNum] = inValidTreeQuotaMap.value[tree.SampleNum]!! - 1
                            }
                        } else {
                            showMessage(context, "請輸入欲修改之數字")
                        }
                    }
                ) {
                    Text("修改(剩餘${treeQuota.value}次)", style = TextStyle(color = Color.Red))
                }
            }
        }
    }
}

@Composable
fun ValidationLazyColumnInputTextField(
    modifier: Modifier = Modifier,
    textContent: MutableState<String>,
    changeQuota: Int
) {
    TextField(
        value = textContent.value,
        readOnly = (changeQuota == 0),
        onValueChange = { textContent.value = it },
        modifier = modifier,
        label = {
            Text("本次調查", fontSize = 16.sp)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(fontSize = 13.sp)
    )
}

@Composable
fun ScatterPlot(
    plotData: PlotData,
) {
    val context = LocalContext.current
    val windowInfo = rememberWindowInfo()
    val height = when (windowInfo.screenHeightInfo) {
        WindowInfo.WindowType.Medium -> 550.dp
        WindowInfo.WindowType.Expanded -> 600.dp
        else -> 550.dp
    }
    // Result Plot
    Canvas(
        modifier = Modifier
            .width(800.dp)
            .height(height)
            .fillMaxSize()
            .padding(10.dp)
    ) {
        // Draw the background and box
        drawRect(
            color = Color.Black,
            topLeft = Offset(0f, 0f),
            size = Size(size.width, size.height),
            style = Stroke(width = 3f)
        )

        var xAxisMax = 0f
        var xAxisMin = 200f
        var yAxisMax = 0f
        var yAxisMin = 200f
        for (i in plotData.PlotTrees) {
            if(i.DBH > xAxisMax) {
                xAxisMax = i.DBH.toFloat()
            } else {
                if(i.DBH < xAxisMin) {
                    xAxisMin = i.DBH.toFloat()
                }
            }

            if (maxThree(i.VisHeight, i.MeasHeight, i.ForkHeight) > yAxisMax) {
                yAxisMax = maxThree(i.VisHeight, i.MeasHeight, i.ForkHeight).toFloat()
            } else {
                if (minThree(i.VisHeight, i.MeasHeight, i.ForkHeight) < yAxisMin) {
                    yAxisMin = minThree(i.VisHeight, i.MeasHeight, i.ForkHeight).toFloat()
                }
            }
        }

        // Set the max and min value of x-axis and y-axis
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
        // Set padding & Paint for each height
        val padding = 150f
        val heightList = listOf("Meas", "Vis", "Fork")
        val forkHeightPaint = Paint().apply { color = Color.Red }
        val visHeightPaint = Paint().apply { color = Color.Green }
        val measHeightPaint = Paint().apply { color = Color.Magenta }
        val paintList = listOf(measHeightPaint, visHeightPaint, forkHeightPaint)
        // Draw scatter points for each data series
        for (yType in heightList) {
            drawDataPoints(plotData.PlotTrees, xAxisMin, xAxisMax, yAxisMin, yAxisMax, yType, padding = padding, paintList = paintList, context)
        }
        // Draw axis
        drawAxis(xAxisMin, xAxisMax, yAxisMin, yAxisMax, padding = padding)
        // Draw legend
        drawLegend(xAxisMin, xAxisMax, yAxisMin, yAxisMax, padding = 150f, heightList, paintList)

        // TODO: Draw the regression line
    }
}

// ref: https://proandroiddev.com/creating-graph-in-jetpack-compose-312957b11b2
// the width start from top left to right, and the height start from top left to bottom
private fun DrawScope.drawAxis(
    xMin: Float,
    xMax: Float,
    yMin: Float,
    yMax: Float,
    padding: Float,
    axisStrokeWidth: Float = 4f
) {
    // Set the Paint
    val axisColor = Color.Black.toArgb()
    val axisTicksPaint = Paint().asFrameworkPaint().apply {
        color = axisColor
        textSize = 20f
    }
    val axisLabelPaint = Paint().asFrameworkPaint().apply {
        color = axisColor
        textSize = 40f
    }
    // Set the center offset of the text horizontally and vertically
    val labelVerticalHalfOffset = (axisLabelPaint.fontMetrics.descent + axisLabelPaint.fontMetrics.ascent) / 2f
    val ticksVerticalHalfOffset = (axisTicksPaint.fontMetrics.descent + axisTicksPaint.fontMetrics.ascent) / 2f
    val yLabelHorizontalHalfOffset = axisLabelPaint.measureText("樹高") / 2f
    val labelPadding = 30f
    val ticksPadding = 10f

    val xRange = xMax - xMin
    val yRange = yMax - yMin

    val canvasWidth = size.width
    val canvasHeight = size.height

    // Set the X-axis ticks and Y-axis ticks
    val xAxisList = mutableListOf<Int>()
    for (i in 0 until (xRange.toInt() + 1) step 5) {
        xAxisList.add(i + xMin.toInt())
    }
    val yAxisList = mutableListOf<Int>()
    for (i in 0 until (yRange.toInt() + 1) step 5) {
        yAxisList.add(i + yMin.toInt())
    }
    // Set the space between each tick
    val xAxisSpace = (canvasWidth - 2 * padding) / (xAxisList.size - 1)
    val yAxisSpace = (canvasHeight - 2 * padding) / (yAxisList.size - 1)

    // Draw X-axis
    drawLine(
        color = Color.Black,
        start = Offset(padding, canvasHeight - padding),
        end = Offset(canvasWidth - padding, canvasHeight - padding),
        strokeWidth = axisStrokeWidth
    )
    // Draw X-axis ticks
    for (i in xAxisList.indices) {
        val textHorizontalHalfOffset = axisTicksPaint.measureText("${xAxisList[i]}") / 2f
        drawContext.canvas.nativeCanvas.drawText(
            "${xAxisList[i]}",
            xAxisSpace * i + padding - textHorizontalHalfOffset ,
            canvasHeight - (padding / 4 * 3),
            axisTicksPaint,
        )
    }
    // draw X axis label name
    drawContext.canvas.nativeCanvas.drawText(
        "DBH",
        canvasWidth - padding + labelPadding,
        canvasHeight - padding - labelVerticalHalfOffset,
        axisLabelPaint
    )

    // Draw Y-axis
    drawLine(
        color = Color.Black,
        start = Offset(padding,  canvasHeight - padding), // Offset(x, y)
        end = Offset(padding, padding),
        strokeWidth = axisStrokeWidth,
        cap = StrokeCap.Round
    )
    // Draw Y-axis ticks
    for (i in yAxisList.indices) {
        drawContext.canvas.nativeCanvas.drawText(
            "${yAxisList[i]}",
            padding / 2f,
            canvasHeight - (yAxisSpace * i) - padding - ticksVerticalHalfOffset,
            axisTicksPaint,
        )
    }
    // draw Y axis label name
    drawContext.canvas.nativeCanvas.drawText(
        "樹高",
        padding - yLabelHorizontalHalfOffset,
        padding - labelPadding,
        axisLabelPaint
    )
}

private fun DrawScope.drawDataPoints(
    PlotTrees: List<Tree>,
    xMin: Float,
    xMax: Float,
    yMin: Float,
    yMax: Float,
    yType: String,
    padding: Float,
    paintList: List<Paint>,
    context: Context
) {
    val plotHeight = size.height - 2 * padding
    val plotWidth = size.width - 2 * padding

    val xRange = xMax - xMin
    val yRange = yMax - yMin

    for (i in PlotTrees.indices) {
        val x = (PlotTrees[i].DBH - xMin) / xRange * plotWidth + padding
        val y = when(yType) {
            "Meas" -> ((PlotTrees[i].MeasHeight - yMin) / yRange * plotHeight)
            "Vis" -> ((PlotTrees[i].VisHeight - yMin) / yRange * plotHeight)
            else -> ((PlotTrees[i].ForkHeight - yMin) / yRange * plotHeight)
        }.toFloat()

        drawCircle(
            color = when(yType) {
                "Meas" -> paintList[0]
                "Vis" -> paintList[1]
                else -> paintList[2]
            }.color,
            radius = 4f,
            center = Offset(x.toFloat(), (plotHeight + padding - y).toFloat())
        )
    }
}

private fun DrawScope.drawLegend(
    xMin: Float,
    xMax: Float,
    yMin: Float,
    yMax: Float,
    padding: Float,
    heightList: List<String>,
    paintList: List<Paint>,
) {
    val canvasHeight = size.height
    val canvasWidth = size.width
    val legendPadding = 30f
    val circlePadding = 20f
    val textPadding = 15f
    val circleRadius = 8f
    val cornerRadius = 10f

    val legendWidth = (3 * padding) + textPadding + circlePadding
    val legendHeight = padding / 2f
    val legendStartX = canvasWidth - legendPadding - legendWidth

    drawRoundRect(
        color = Color.Black,
        topLeft = Offset(legendStartX, legendPadding),
        size = Size(legendWidth, legendHeight),
        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        style = Stroke(width = 2f)
    )

    for (i in heightList.indices) {
        // draw text
        val paint = Paint().asFrameworkPaint().apply {
            color = paintList[i].color.toArgb()
            textSize = 30f
        }
        val textVerticalHalfOffset = (paint.fontMetrics.descent + paint.fontMetrics.ascent) / 2f
//        val textWidth = paint.measureText(
//            when(heightList[i]) {
//                "Meas" -> "量測樹高"
//                "Vis" -> "目視樹高"
//                else -> "分岔樹高"
//            }
//        )
//        Log.i(dTag, "textWidth: $textWidth")

        // draw point
        drawCircle(
            color = paintList[i].color,
            radius = circleRadius,
            center = Offset(legendStartX + padding * i + circlePadding, legendPadding + legendHeight / 2f )
        )

        drawContext.canvas.nativeCanvas.drawText(
            when(heightList[i]) {
                "Meas" -> "量測樹高"
                "Vis" -> "目視樹高"
                else -> "分岔樹高"
            },
            legendStartX + padding * i + (circlePadding + textPadding),
            legendPadding + legendHeight / 2f - textVerticalHalfOffset,
            paint
        )
    }
}