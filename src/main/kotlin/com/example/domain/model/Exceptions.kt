package com.example.domain.model

import io.ktor.http.*

sealed class ApiException(message: String): Exception(message) {
    object LoginIncorrectException: ApiException("Email or password incorrect")
    object ApiIncorrectException: ApiException("Apikey not valid")
}