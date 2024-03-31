package com.example.ntufapp.api.dataType.plotsCatalogueResponse

data class PlotsCatalogueResponse(
    val body: PlotsCatalogue,
    val message: Message,
    val result: Boolean,
    val statusCode: String
)