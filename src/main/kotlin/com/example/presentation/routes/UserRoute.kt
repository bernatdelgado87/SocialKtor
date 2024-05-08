package com.example.presentation.routes

import com.example.data.repository.RegisterLoginRepositoryImpl
import com.example.presentation.entity.LoginDTO
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.collect
import java.io.InputStream

fun Route.userRoute() {
    post("/register") {

        val multipart = call.receiveMultipart()

        lateinit var inputStream: InputStream
        var extension = ""
        lateinit var name: String
        lateinit var email: String
        lateinit var password: String


        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                inputStream = part.streamProvider()
                extension = part.originalFileName?.substringAfter(".").orEmpty()
            }

            if (part is PartData.FormItem) {
                if (part.name.equals("name")) {
                    name = part.value
                }
                if (part.name.equals("email")) {
                    email = part.value
                }
                if (part.name.equals("password")) {
                    password = part.value
                }
            }
        }
        RegisterLoginRepositoryImpl().register(
            name,
            email,
            password,
            inputStream,
            extension,
            application
        ).collect() {
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
