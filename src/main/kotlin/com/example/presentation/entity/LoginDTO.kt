package com.example.presentation.entity

import kotlinx.serialization.Serializable

@Serializable
data class LoginDTO (
    val email: String,
    val password: String)