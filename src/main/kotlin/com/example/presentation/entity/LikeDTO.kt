package com.example.presentation.entity

import kotlinx.serialization.Serializable

@Serializable
data class LikeDTO (
    val userId: String,
    val postReference: Int)