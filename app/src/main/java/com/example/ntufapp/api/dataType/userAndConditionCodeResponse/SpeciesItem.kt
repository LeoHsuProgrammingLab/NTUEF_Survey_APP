package com.example.ntufapp.api.dataType.userAndConditionCodeResponse

data class SpeciesItem(
    val code: String,
    val code_name: String,
    val code_name_en: String,
    val sort: Int,
    val enabled: Boolean
)