package com.example.ntufapp.api.dataType.userAndConditionCodeResponse

data class ForestDept(
    val forest_dept_name: String,
    val forest_dept_code: String,
    val area_compart_list: List<AreaCompartment>
)
