package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LikeModel (
    val userId: Int?,
    val postReference: Int?,
    val multimediaModel: MultimediaModel? = null)

@Serializable
data class LikeSimplifiedModel (
    val userId: Int?,
    val postReference: Int?)