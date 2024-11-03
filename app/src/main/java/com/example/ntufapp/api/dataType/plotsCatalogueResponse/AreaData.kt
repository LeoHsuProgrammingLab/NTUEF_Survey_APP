package com.example.ntufapp.api.dataType.plotsCatalogueResponse

data class AreaData(
    val area_id: String,
    val area_kinds_name: String,
    val area_code: String,
    val area_name: String,
    val dept_name: String,
    val location_list: List<Location>,
    val area_compart: Int,
)