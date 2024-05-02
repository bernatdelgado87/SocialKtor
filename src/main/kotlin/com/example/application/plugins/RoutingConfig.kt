package com.example.application.plugins

import com.example.presentation.routes.frontend.frontendRoute
import com.example.presentation.routes.socialRoute
import com.example.presentation.routes.userRoute
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun Application.configureRouting() {

    routing {
        authenticate(SocialAuth.AUTH_BASIC) {
            get("/") {
                call.respondText("Hello World!")
            }
        }
        socialRoute()
        userRoute()
        frontendRoute()

    }
}
