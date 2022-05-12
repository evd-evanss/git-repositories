package com.sugarspoon.github.model.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepositoryEntity(
    val repositoryName: String,
    val forks: Int,
    val stars: Int,
    val avatar: String,
    val author: String,
    val url: String
): Parcelable
