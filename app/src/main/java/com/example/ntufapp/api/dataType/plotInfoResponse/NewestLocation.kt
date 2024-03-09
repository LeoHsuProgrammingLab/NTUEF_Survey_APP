package com.example.ntufapp.api.dataType.plotInfoResponse

data class NewestLocation(
    val location_breed_name: String,
    val location_growState_list: List<Any>,
    val location_remark: String,
    val location_sid: String,
    val location_sort: Int,
    val location_tree_code: String,
    val location_wx: String,
    val location_wy: String
)