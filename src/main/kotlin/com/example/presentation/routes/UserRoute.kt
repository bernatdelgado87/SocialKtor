package com.example.presentation.routes

import com.example.data.repository.RegisterLoginRepositoryImpl
import com.example.domain.model.UserModel
import com.example.presentation.entity.LoginDTO
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.collect

fun Route.userRoute() {
    post("/register") {
        val data = call.receive<UserModel>()
        val repository = RegisterLoginRepositoryImpl()
        repository.register(data).collect() {
            call.respond(it)
        }
    }
    post("/login") {
        val data = call.receive<LoginDTO>()
        val repository = RegisterLoginRepositoryImpl()
        repository.login(data.email, data.password).collect() {
                call.respond(it)
            }
    }
}
