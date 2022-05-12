package com.sugarspoon.github.model.response

data class RepositoriesResponse(
    val incomplete_results: Boolean,
    val items: List<Items>,
    val total_count: Int
)