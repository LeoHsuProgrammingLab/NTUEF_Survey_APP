package com.example.ntufapp.model

import androidx.compose.runtime.MutableState

data class Tree(
    var SampleNum: Int = -1,
    var Species: String = "", // 2
    var State: MutableList<String> = mutableListOf(),
    var DBH: Double = 0.0,
    var VisHeight: Double = 0.0,
    var MeasHeight: Double = 0.0,
    var ForkHeight: Double = 0.0,
) {
    fun clone(): Tree {
        return Tree(
            SampleNum = SampleNum,
            Species = Species,
            State = State,
            DBH = DBH,
            VisHeight = VisHeight,
            MeasHeight = MeasHeight,
            ForkHeight = ForkHeight
        )
    }
    fun reset() {
        DBH = 0.0
        VisHeight = 0.0
        MeasHeight = 0.0
        ForkHeight = 0.0
    }
}