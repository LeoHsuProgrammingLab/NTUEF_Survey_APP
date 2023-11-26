package com.example.ntufapp.model

import androidx.compose.runtime.MutableState

data class Tree(
//    var SiteNum: String = "", // redundant
//    var PlotNum: Int = -1, // redundant
//    var Date: String = "", // redundant
    var SampleNum: Int = -1,
    var Species: String = "", // 2
    var DBH: Double = 0.0,
    var State: MutableList<String> = mutableListOf(),
    var VisHeight: Double = 0.0,
    var MeasHeight: Double = 0.0,
    var ForkHeight: Double = 0.0,
) {
    fun clone(): Tree {
        val cloneTree = Tree()
        cloneTree.SampleNum = SampleNum
        cloneTree.Species = Species
        cloneTree.DBH = DBH
        cloneTree.VisHeight = VisHeight
        cloneTree.MeasHeight = MeasHeight
        cloneTree.ForkHeight = ForkHeight
        for(state in State) {
            cloneTree.State.add(state)
        }

        return cloneTree
    }
    fun reset() {
        DBH = 0.0
        VisHeight = 0.0
        MeasHeight = 0.0
        ForkHeight = 0.0
    }
}