package com.example.ntufapp.model

import android.annotation.TargetApi
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.ntufapp.data.ntufappInfo.Companion.defaultTreeNum
import java.time.LocalDate
import kotlin.math.abs

data class PlotData(
    var Date: String = "",
    var ManageUnit: String = "",
    var SubUnit: String = "", //1-55

    var PlotName: String = "",
    var PlotNum: Int = 0,
    var PlotType: String = "",

    var PlotArea: Double = 0.0,
    var TWD97_X: String = "",
    var TWD97_Y: String = "",

    var Altitude: Double = 0.0,
    var Slope: Double = 0.0, //
    var Aspect: String = "",

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
        clonePlot.Aspect = Aspect

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

    fun initPlotTrees() {
        for (i in 0 until defaultTreeNum) {
            PlotTrees.add(Tree(SampleNum = i + 1))
        }
    }

    fun getPlotTreesNum(): Int {
        return PlotTrees.size
    }
}

fun compare2Plots(oldPlot: PlotData, newPlot: PlotData, threshold: Double, target: String): MutableSet<Tree> {
    val targetSet = mutableSetOf<Tree>()

    for(i in 0 until oldPlot.PlotTrees.size) {
        val tree = newPlot.searchTree(oldPlot.PlotTrees[i].SampleNum)

        when(target) {
            "DBH" -> {
                if(abs(oldPlot.PlotTrees[i].DBH - tree!!.DBH) > threshold) {
                    tree.DBH = oldPlot.PlotTrees[i].DBH
                }
            }
            "Meas" -> {
                if(abs(oldPlot.PlotTrees[i].MeasHeight - tree!!.MeasHeight) > threshold) {
                    tree.MeasHeight = oldPlot.PlotTrees[i].MeasHeight
                }
            }
            "Vis" -> {
                if(abs(oldPlot.PlotTrees[i].VisHeight - tree!!.VisHeight) > threshold) {
                    tree.VisHeight = oldPlot.PlotTrees[i].VisHeight
                }
            }
            "Fork" -> {
                if(abs(oldPlot.PlotTrees[i].ForkHeight - tree!!.ForkHeight) > threshold) {
                    tree.ForkHeight = oldPlot.PlotTrees[i].ForkHeight
                }
            }
        }
        targetSet.add(tree!!)
    }

    return targetSet
}