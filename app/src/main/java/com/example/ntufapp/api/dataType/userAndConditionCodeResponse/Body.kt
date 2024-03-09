package com.example.ntufapp.api.dataType.userAndConditionCodeResponse

data class Body(
    val growth_code_list: List<GrowthCode>,
    val growth_code_list_count: Int,
    val user_code_list: List<UserCode>,
    val user_code_list_count: Int
)