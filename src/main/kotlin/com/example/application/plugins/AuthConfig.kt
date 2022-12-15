package com.example.application.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.application.plugins.SocialAuth.AUTH_BASIC
import com.example.application.plugins.SocialAuth.AUTH_FORM
import com.example.application.plugins.SocialAuth.AUTH_JWT_TOKEN
import com.example.application.plugins.SocialAuth.USERNAME_JWT_TOKEN
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

object SocialAuth {
    const val AUTH_FORM = "auth-form"
    const val AUTH_BASIC = "auth-basic"
    const val AUTH_JWT_TOKEN = "auth-jwt"

    const val USERNAME_JWT_TOKEN = "username"

}

fun Application.configureAuthentication() {
    install(Authentication) {
        basic(AUTH_BASIC) {
            validate { body ->
                if (body.name == "jetbrains" && body.password == "foobar") {
                    UserIdPrincipal(body.name)
                } else {
                    null
                }
            }
        }
        form(AUTH_FORM) {
            // Configure form authentication
        }
        jwt(AUTH_JWT_TOKEN) {
            val secret = environment.config.property("jwt.secret").getString()
            val issuer = environment.config.property("jwt.issuer").getString()
            val audience = environment.config.property("jwt.audience").getString()
            val myRealm = environment.config.property("jwt.realm").getString()

            realm = myRealm
            verifier(
                JWT
                .require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .build())
            validate { credential ->
                if (credential.payload.getClaim(USERNAME_JWT_TOKEN).asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}
