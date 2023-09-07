package com.example.ntufapp.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

data class PlotData(
    var Date: String = "",
    var ManageUnit: String = "",
    var SubUnit: String = "", //1-55

    var PlotName: String = "",
    var PlotNum: Int = 0,
    var PlotType: String = "",

    var PlotArea: Double = 0.0,
    var TWD97_X: Double = 0.0,
    var TWD97_Y: Double = 0.0,

    var Altitude: Double = 0.0,
    var Slope: Double = 0.0, //
    var aspect: String = "",

    var Surveyor: MutableList<String> = mutableListOf(), // 1
    var HtSurveyor: MutableList<String> = mutableListOf(), // 3
    var PlotTrees: MutableList<Tree> = mutableListOf()
) {
   fun setToday() {
       val today: LocalDate = LocalDate.now()
       Date = today.toString()
   }
    fun searchTree(treeNum: Int): Tree? {
        for(tree in PlotTrees) {
            if(tree.SampleNum == treeNum) {
                return tree
            }
        }
        return null
    }

    fun clone(): PlotData {
        val clonePlot = PlotData()
        clonePlot.Date = Date
        clonePlot.ManageUnit = ManageUnit
        clonePlot.SubUnit = SubUnit
        clonePlot.PlotName = PlotName
        clonePlot.PlotNum = PlotNum
        clonePlot.PlotType = PlotType
        clonePlot.PlotArea = PlotArea
        clonePlot.TWD97_X = TWD97_X
        clonePlot.TWD97_Y = TWD97_Y
        clonePlot.Altitude = Altitude
        clonePlot.Slope = Slope
        clonePlot.aspect = aspect

        for (surveyor in Surveyor) {
            clonePlot.Surveyor.add(surveyor)
        }

        for (htSurveyor in HtSurveyor) {
            clonePlot.HtSurveyor.add(htSurveyor)
        }

        for (tree in PlotTrees) {
            clonePlot.PlotTrees.add(tree.clone())
        }

        return clonePlot
    }

    fun resetAllTrees() {
        for (tree in PlotTrees) {
            tree.reset()
        }
    }
}