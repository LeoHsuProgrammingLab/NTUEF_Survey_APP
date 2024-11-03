package com.example.ntufapp.api.dataType.userAndConditionCodeResponse

data class Body(
    val user_code_list: List<UserCode>,
    val user_code_list_count: Int,
    val growth_code_list: List<GrowthCode>,
    val growth_code_list_count: Int,
    val area_investigation_setup_list: List<InvestigationItem>,
    val area_investigation_setup_list_count: Int,
    val breed_list: List<SpeciesItem>,
    val breed_list_count: Int,
    val area_kinds_list: List<AreaDetail>,
    val area_kinds_list_count: Int,
    val location_type_list: List<LocationDetail>,
    val location_type_list_count: Int,
    val forest_dept_list: List<ForestDept>,
    val forest_dept_list_count: Int
)