package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel (
    val id: Int? = null,
    val name: String? = null,
    val dogName: String? = null,
    val genre: String? = null,
    val breed: Int? = null,
    val email: String? = null,
    val password: String? = null,
    var token: String? = null
    )
