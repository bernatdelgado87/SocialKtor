package com.example.domain.model

sealed class Exceptions {
    object LoginIncorrectException: Throwable()
}