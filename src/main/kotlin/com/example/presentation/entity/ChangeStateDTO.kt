package com.example.presentation.entity

import kotlinx.serialization.Serializable

@Serializable
data class ChangeStateDTO(
    val activity: String,
    val state: String,
    val activity_id: String,
    val step_id: String
)

