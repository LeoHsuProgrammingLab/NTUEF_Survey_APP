package com.example.ntufapp.model

import com.example.ntufapp.data.ntufappInfo.Companion.defaultTreeNum
import java.time.LocalDate
import kotlin.math.abs

data class PlotData(
    var Date: String = "",
    var ManageUnit: String = "",
    var SubUnit: String = "", //1-55

    var PlotName: String = "",
    var PlotNum: String = "0",
    var PlotType: String = "",

    var PlotArea: Double = 0.0,
    var TWD97_X: String = "",
    var TWD97_Y: String = "",

    var Altitude: Double = 0.0,
    var Slope: Double = 0.0, //
    var Aspect: String = "",

    var Surveyor: MutableList<String> = mutableListOf(), // 1
    var HtSurveyor: MutableList<String> = mutableListOf(), // 3
    var PlotTrees: MutableList<Tree> = mutableListOf(),

    var area_id: String = "",
    var area_investigation_setup_id: String = "",
    var location_mid: String = "",
) {
   fun setToday() {
       val today: LocalDate = LocalDate.now()
       Date = today.toString()
   }
    fun getTreeBySampleNum(sampleNum: Int): Tree? {
        return PlotTrees.find { it.SampleNum == sampleNum }
    }

    fun clone(): PlotData {
        return PlotData(
            Date = Date,
            ManageUnit = ManageUnit,
            SubUnit = SubUnit,
            PlotName = PlotName,
            PlotNum = PlotNum,
            PlotType = PlotType,
            PlotArea = PlotArea,
            TWD97_X = TWD97_X,
            TWD97_Y = TWD97_Y,
            Altitude = Altitude,
            Slope = Slope,
            Aspect = Aspect,
            Surveyor = Surveyor.toMutableList(),
            HtSurveyor = HtSurveyor.toMutableList(),
            PlotTrees = PlotTrees.toMutableList()
        )
    }

    fun resetAllTrees() {
        PlotTrees.forEach { it.reset() }
    }

    fun initPlotTrees(treeNumber: Int) {
        PlotTrees.clear()
        PlotTrees.addAll((1..treeNumber).map { Tree(SampleNum = it) })
    }
}

fun compareTwoPlots(oldPlot: PlotData, newPlot: PlotData, threshold: Double, target: String): MutableSet<Tree> {
    val targetSet = mutableSetOf<Tree>()

    oldPlot.PlotTrees.forEach { oldTree ->
        val newTree = newPlot.getTreeBySampleNum(oldTree.SampleNum)
        if (newTree != null && checkThreshold(oldTree.getFieldValue(target), newTree.getFieldValue(target), threshold)) {
            targetSet.add(newTree)
        }
    }

    return targetSet
}

private inline fun <reified T> Tree.getFieldValue(fieldName: String): T =
    when(fieldName) {
        "DBH" -> DBH as T
        "Meas" -> MeasHeight as T
        "Vis" -> VisHeight as T
        "Fork" -> ForkHeight as T
        else -> throw IllegalArgumentException("Invalid field name")
    }

fun createTreeQuotaPair(invalidSet: MutableSet<Tree>): MutableMap<Int, Int> {
    return invalidSet.associate { it.SampleNum to 3 }.toMutableMap()
}

fun checkThreshold(old: Double, new: Double, threshold: Double): Boolean {
    return abs(old - new) > threshold * old
}

fun checkThresholdByInterval(old: Double, new: Double, threshold: Double): Boolean {
    return abs(old - new) > threshold
}