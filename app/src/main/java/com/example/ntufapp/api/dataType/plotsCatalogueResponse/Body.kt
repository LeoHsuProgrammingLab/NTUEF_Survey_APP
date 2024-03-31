package com.example.ntufapp.api.dataType.plotsCatalogueResponse

data class PlotsCatalogue(
    val data_list: List<AreaData>,
    val total: Int // total number of plots
)