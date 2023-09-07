package com.example.ntufapp.layout

import android.content.Context
import androidx.compose.ui.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.model.Tree

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
        ScatterPlot(oldPlotData = oldPlotData, newPlotData = newPlotData)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = {
                    onBackButtonClick()
                },
                modifier = Modifier
                    .padding(end = 16.dp)
                    .height(70.dp)
            ) {
                Text("繼續調查", fontSize = 20.sp)
            }
            OutlinedButton(
                onClick = {
                    onNextButtonClick()
                },
                modifier = Modifier
                    .padding(end = 16.dp)
                    .height(70.dp)
            ) {
                Text("完成此次調查", fontSize = 20.sp)
            }
        }
    }
}
@Composable
fun ScatterPlot(
    oldPlotData: PlotData,
    newPlotData: PlotData
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(10.dp)
            .width(800.dp)
            .height(600.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
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