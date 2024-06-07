package com.example.ntufapp.model

import com.example.ntufapp.api.dataType.userAndConditionCodeResponse.User
import java.time.LocalDate
import kotlin.math.abs

data class PlotData(
    var Date: String = "",
    var Year: String = "",
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

    var Surveyor: Map<Int, String> = emptyMap(), // 1
    var HtSurveyor: Pair<Int, String> = Pair(0, ""), // 3
    var PlotTrees: MutableList<Tree> = mutableListOf(),

    var area_id: String = "",
    var area_investigation_setup_id: String = "",
    var area_investigation_setup_list: MutableMap<String, String> = mutableMapOf(),
    var location_mid: String = "",
    var investigation_user_map: MutableMap<Int, String> = mutableMapOf(), // user_code, user_name
    var userList: List<User> = listOf()
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
            Year = Year,
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
            Surveyor = Surveyor,
            HtSurveyor = HtSurveyor,
            PlotTrees = PlotTrees.toMutableList(),
            area_id = area_id,
            area_investigation_setup_id = area_investigation_setup_id,
            area_investigation_setup_list = area_investigation_setup_list,
            location_mid = location_mid,
            investigation_user_map = investigation_user_map.toMutableMap(),
            userList = userList
        )
    }

    fun resetAllTrees() {
        PlotTrees.forEach { it.reset() }
    }

    fun getDBHCode(): String? {
        return area_investigation_setup_list["基徑"]
    }

    fun getHeightCode(): String? {
        return area_investigation_setup_list["樹高"]
    }

    fun getStateCode(): String? {
        return area_investigation_setup_list["生長狀態"]
    }

    fun getForkedHeightCode(): String? {
        return area_investigation_setup_list["分叉高"]
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