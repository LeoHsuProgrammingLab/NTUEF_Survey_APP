package com.example.ntufapp.api.dataType.uploadDataResponse

data class uploadDataResponse(
    val body: List<Any>,
    val message: Message,
    val result: Boolean,
    val statusCode: String
)