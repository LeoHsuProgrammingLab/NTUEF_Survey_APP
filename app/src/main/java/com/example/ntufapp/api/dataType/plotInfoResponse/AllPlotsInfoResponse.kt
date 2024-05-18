package com.example.ntufapp.api.dataType.plotInfoResponse

import com.example.ntufapp.api.dataType.userAndConditionCodeResponse.User

data class PlotInfoResponse(
    val body: Body,
    val message: Message,
    val result: Boolean,
    val statusCode: String,
    var location_mid: String = "",
    var userList: List<User> = listOf()
)