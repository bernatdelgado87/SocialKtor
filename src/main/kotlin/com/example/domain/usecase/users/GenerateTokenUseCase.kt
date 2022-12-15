package com.example.domain.usecase.users

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.application.plugins.SocialAuth
import es.experis.app.domain.usecase.UseCase
import io.ktor.application.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.*

class GenerateTokenUseCase: UseCase<String, GenerateTokenUseCase.Input>() {
    data class Input(val application: Application, val user: String)

    override suspend fun run(params: Input): Flow<String> {

        val secret = params.application.environment.config.property("jwt.secret").getString()
        val issuer = params.application.environment.config.property("jwt.issuer").getString()
        val audience = params.application.environment.config.property("jwt.audience").getString()


        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim(SocialAuth.USERNAME_JWT_TOKEN, params.user)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(secret))

        return flowOf(token)
    }


}
