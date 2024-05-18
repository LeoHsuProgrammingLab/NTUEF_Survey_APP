package com.example.ntufapp.api.dataType.surveyDataForUpload

data class SurveyDataForUpload(
    val area_id: String,
    val area_investigation_setup_id: String,
    val investigation_date: String,
    val investigation_record_list: List<InvestigationRecord>, // Warning: Question, 1. 少一層List 2. 順序問題
    val investigation_treeHeight_user: Int, // Warning: Should be list of user_id
    val investigation_user: String,
    val investigation_year: String,
    val location_mid: String,
    val photo_list: List<Photo>,
    val token: String = "",
    val update_user: Int = 1,
)