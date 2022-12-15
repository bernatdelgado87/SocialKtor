package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LikeModel (
    val userId: String?,
    val postReference: Int?,
    val multimediaModel: MultimediaModel? = null)

@Serializable
data class LikeSimplifiedModel (
    val userId: String?,
    val postReference: Int?)