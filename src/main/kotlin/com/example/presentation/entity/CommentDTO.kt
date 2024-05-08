package com.example.presentation.entity

import kotlinx.serialization.Serializable

@Serializable
data class CommentDTO (
    val userId: String,
    val postReference: Int,
    val text: String)