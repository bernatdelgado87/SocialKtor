package com.example.presentation.routes.frontend

import io.ktor.application.*
import io.ktor.mustache.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.frontendRoute() {
    route("/css") {
        get("/") {
            call.respond(MustacheContent("global.css", null))
        }
    }
}
