package com.example.ntufapp.utils

import android.content.Context
import android.widget.Toast
import com.example.ntufapp.data.DataSource
import com.example.ntufapp.model.PlotData

fun maxThree(a: Double, b: Double, c: Double): Double {
    return maxOf(a, maxOf(b, c))
}

fun minThree(a: Double, b: Double, c: Double): Double {
    return minOf(a, minOf(b, c))
}

fun showMessage(context: Context, s: String) {
    Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
}

fun flattenPlotData(plotData: PlotData): List<List<String>> {
    val flattenedPlotData = mutableListOf<List<String>>()

    // Add the header
    val header = mutableListOf<String>()
    DataSource.columnName.forEach {
        header.add(it)
    }
    flattenedPlotData.add(header)

    // Add the data
    plotData.PlotTrees.forEach { tree ->
        val row = listOf(
            tree.SampleNum.toString(),
            tree.Species,
            tree.DBH.toString(),
            tree.State.joinToString(";"),
            tree.MeasHeight.toString(),
            tree.VisHeight.toString(),
            tree.ForkHeight.toString(),
            plotData.Date,
            plotData.ManageUnit,
            plotData.SubUnit,
            plotData.PlotNum,
            plotData.PlotName,
            plotData.PlotArea.toString(),
            plotData.PlotType,
            plotData.TWD97_X,
            plotData.TWD97_Y,
            plotData.Altitude.toString(),
            plotData.Slope.toString(),
            plotData.Aspect,
            plotData.Surveyor.map{"${it.key}: ${it.value}"}.joinToString(","),
            plotData.HtSurveyor!!.first.toString() + ": " + plotData.HtSurveyor!!.second,
        )

        flattenedPlotData.add(row)
    }
    return flattenedPlotData
}