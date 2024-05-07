package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel (
    val id: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val profileImage: String? = null,
    val apikey: String? = null,
    val password: String? = null,
    )
