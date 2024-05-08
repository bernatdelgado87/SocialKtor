package com.example.domain.model
import kotlinx.serialization.Serializable

@Serializable
data class Question (
    val id: Int,
    val textQuestion: String,
    val textAnswer: String,
    val comments: CommentModel? = null
    )