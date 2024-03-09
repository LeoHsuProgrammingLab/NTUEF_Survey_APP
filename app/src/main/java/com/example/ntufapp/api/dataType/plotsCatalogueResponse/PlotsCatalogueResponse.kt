package com.example.ntufapp.api.dataType.plotsCatalogueResponse

data class PlotsCatalogueResponse(
    val body: plotsCatalogue,
    val message: Message,
    val result: Boolean,
    val statusCode: String
)