package com.example.ntufapp.api.dataType.userAndConditionCodeResponse

data class UserAndConditionCodeResponse(
    val body: Body,
    val message: Message,
    val result: Boolean,
    val statusCode: String
)