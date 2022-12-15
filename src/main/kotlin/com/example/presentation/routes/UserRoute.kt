package com.example.presentation.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.application.plugins.SocialAuth.USERNAME_JWT_TOKEN
import com.example.domain.model.Exceptions
import com.example.domain.model.UserModel
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.users.RegisterUserCase
import com.example.presentation.entity.UserDTO
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.collect
import java.util.*

fun Route.userRoute() {
    post("/register") {
        try {
            val data = call.receive<UserModel>()
            val usecase = RegisterUserCase(application)
            usecase(RegisterUserCase.Input(data!!)).collect() {
                call.respond(it)
            }
        } catch (throwable: Throwable) {
            call.respond(HttpStatusCode.Conflict, "User Already exists")
        }
    }
    post("/login") {
        val data = call.receive<UserDTO>()
        val usecase = LoginUseCase(application)
        try {
            usecase(LoginUseCase.Input(data.username, data.password)).collect() {
                call.respond(it)
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is Exceptions.LoginIncorrectException -> call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }

    post("/1") {
        val secret = application.environment.config.property("jwt.secret").getString()
        val issuer = application.environment.config.property("jwt.issuer").getString()
        val audience = application.environment.config.property("jwt.audience").getString()
        val myRealm = application.environment.config.property("jwt.realm").getString()


        val user = call.receive<UserDTO>()


        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim(USERNAME_JWT_TOKEN, user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(secret))
        call.respond(hashMapOf("token" to token))
    }

    authenticate("auth-jwt") {
        get("/1") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim(USERNAME_JWT_TOKEN).asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
        }
    }
}
