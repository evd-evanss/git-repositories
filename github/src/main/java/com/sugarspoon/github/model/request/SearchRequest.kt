package com.sugarspoon.github.model.request

import retrofit2.http.Query

data class SearchRequest(
    @Query("q")
    val language: String = "language:kotlin",
    @Query("sort")
    val terms: String = "stars",
    @Query("page")
    val page: Number
)