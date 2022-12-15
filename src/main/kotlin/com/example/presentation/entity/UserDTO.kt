package com.example.presentation.entity

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO (
    val username: String,
    val password: String)