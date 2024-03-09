package com.example.ntufapp.api.dataType.userAndConditionCodeResponse

data class UserCode(
    val dept_code: String,
    val dept_name: String,
    val user_list: List<User>
)