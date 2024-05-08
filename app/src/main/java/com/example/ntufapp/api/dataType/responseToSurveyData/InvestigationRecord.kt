package com.example.ntufapp.api.dataType.responseToSurveyData

data class InvestigationRecord(
    val investigation_item_code: String,
    val investigation_item_result: String,
    val location_sid: String,
    val location_wx: String,
    val location_wy: String
)