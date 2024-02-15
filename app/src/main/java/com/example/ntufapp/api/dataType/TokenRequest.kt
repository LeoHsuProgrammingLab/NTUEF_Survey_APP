package com.example.ntufapp.api.dataType

data class TokenRequest(
    val token: String
)

data class TokenRequestWithBody(
    val token: String,
    val location_mid: String
)
