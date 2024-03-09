package com.example.ntufapp.api.dataType.plotInfoResponse

data class InvestigationRecord(
    val investigation_mid: String,
    val investigation_result_list: List<InvestigationResult>,
    val location_sid: String
)