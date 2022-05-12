package com.sugarspoon.github.model.response

data class LicenseResponse(
    val key: String,
    val name: String,
    val node_id: String,
    val spdx_id: String,
    val url: String
)