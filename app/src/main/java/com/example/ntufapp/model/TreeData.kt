package com.example.ntufapp.model

data class Tree(
    var SiteNum: String = "", // redundant
    var PlotNum: Int = -1, // redundant
    var SampleNum: Int = -1,
    var Species: String = "", // 2
    var HtSurveyor: MutableList<String> = mutableListOf(), // 3
    var Date: String = "", // redundant
    var DBH: Double = 0.0,
    var State: String = "",
    var VisHeight: Double = 0.0,
    var MeasHeight: Double = 0.0,
    var ForkHeight: Double = 0.0,
) {

}