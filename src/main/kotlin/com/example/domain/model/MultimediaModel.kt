package com.example.domain.model

import com.example.domain.commons.aws.Bucket
import kotlinx.serialization.Serializable

@Serializable
data class MultimediaModel (
    val id: Int? = null,
    val userId: Int? = null,
    val description: String? = null,
    val relativeUrl: String? = null,
    val absoluteUrl: String? = if (relativeUrl != null) {
        Bucket.AWSUrl + relativeUrl} else null,
    val numberOfLikes: Int? = null,
    val likes: List<LikeSimplifiedModel>? = null)
