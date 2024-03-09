package com.example.ntufapp.api.dataType.plotInfoResponse

data class NewestInvestigation(
    val investigation_date: String,
    val investigation_mid: String,
    val investigation_record_list: List<InvestigationRecord>,
    val investigation_record_list_count: Int,
    val investigation_treeHeight_user_list: InvestigationTreeHeightUserList,
    val investigation_user_list: List<InvestigationUser>,
    val investigation_year: String
)