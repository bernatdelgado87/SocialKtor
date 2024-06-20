package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CommentWrapperResponse(
    val multimediaModel: MultimediaModel,
    val userImageUrl: String,
    val comments: List<CommentModel>
)

@Serializable
data class CommentModel (
    val id: Int,
    val content: String,
    val createTime: String,
    val user: UserModel,
    val commentReferent: Int? = null
        )