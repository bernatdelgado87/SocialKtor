package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OperationSuccess(
    val status: String,
    val message: String
)
