package com.example.ntufapp.api.dataType

data class Connection(
    val body: List<Any>,
    val message: Message,
    val result: Boolean,
    val statusCode: String
)