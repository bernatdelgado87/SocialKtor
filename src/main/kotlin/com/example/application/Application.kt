package com.example.application

import com.example.application.plugins.*
import io.ktor.application.*
import io.ktor.auth.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module(testing: Boolean = false) {
    configureAuthentication()
    configureRouting()
    configureTemplating()
    configureDatabase()
    configureInOutContent()
}


