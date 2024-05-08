package com.example.domain.model

import com.example.domain.commons.aws.Bucket
import kotlinx.serialization.Serializable

@Serializable
data class UserModel (
    val id: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val relativeUrlImageProfile: String? = null,
    val profileImage: String?  = if (relativeUrlImageProfile != null) {
        Bucket.AWSUrl + relativeUrlImageProfile} else null,
    val apikey: String? = null,
    val password: String? = null,
    )
