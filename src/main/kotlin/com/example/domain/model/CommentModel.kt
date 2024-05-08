package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment (
    val id: Int,
    val content: String,
    val createTime: String,

    val questionID: Int?,
    val questionObject: Question? = null
        )