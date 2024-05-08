package com.example.ntufapp.api.dataType.plotInfoResponse

data class PlotInfoResponse(
    val body: Body,
    val message: Message,
    val result: Boolean,
    val statusCode: String,
    var location_mid: String = ""
)