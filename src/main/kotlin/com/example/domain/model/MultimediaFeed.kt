package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MultimediaFeed(
    val multimediaModel: List<MultimediaModel>,
    val morePages: Boolean = false
)