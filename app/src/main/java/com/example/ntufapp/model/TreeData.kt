package com.example.ntufapp.model


data class Tree(
    var SampleNum: Int = -1,
    var Species: String = "", // 2
    var State: MutableList<String> = mutableListOf(),
    var DBH: Double = 0.0,
    var VisHeight: Double = 0.0,
    var MeasHeight: Double = 0.0,
    var ForkHeight: Double = 0.0,
    var location_sid: String = "",
    var location_wx: String = "",
    var location_wy: String = "",
) {
    fun clone(): Tree {
        return Tree(
            SampleNum = SampleNum,
            Species = Species,
            State = State,
            DBH = DBH,
            VisHeight = VisHeight,
            MeasHeight = MeasHeight,
            ForkHeight = ForkHeight,
            location_sid = location_sid,
            location_wx = location_wx,
            location_wy = location_wy,
        )
    }
    fun reset() {
        DBH = 0.0
        VisHeight = 0.0
        MeasHeight = 0.0
        ForkHeight = 0.0
    }
}