package com.example.model

data class TreeData(
    var SiteNum: String,
    var PlotNum: String,
    var SampleNum: String,
    var DBH: Double,
    var State: String,
    var Species: String,
    var HeightInfo: TreeHeight
) {

}

data class TreeHeight(
    var Surveyor: String,
    var Date: String,
    var VisHeight: Double,
    var MeasHeight: Double,
    var ForkHeight: Double,
){

}