package com.example.ntufapp.api.dataType.plotInfoResponse

data class Body(
    val location_info: LocationInfo,
    val newest_investigation: NewestInvestigation,
    val newest_location: List<NewestLocation>,
    val newest_location_count: Int
)