package com.example.application.plugins

import com.example.data.repository.RegisterLoginRepositoryImpl
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.coroutines.flow.firstOrNull

enum class AuthType {
    USER_TYPE,
    ADMIN_TYPE
}

class CustomAuthenticationProvider internal constructor(config: Configuration) : AuthenticationProvider(config) {

    internal val authToken: (ApplicationCall) -> String? = config.authToken
    internal val authenticationFunction = config.authenticationFunction

    //Configuration
    class Configuration internal constructor(name: String?) : AuthenticationProvider.Configuration(name) {

        internal var authenticationFunction: AuthenticationFunction<String> = {
            throw NotImplementedError(
                "auth validate function is not specified. Use CustomAuth { validate { ... } } to fix."
            )
        }

        internal var authToken: (ApplicationCall) -> String? = { call -> call.request.header("auth-token") }

        internal fun build() = CustomAuthenticationProvider(this)

        internal fun validate(body: AuthenticationFunction<String>) {
            authenticationFunction = body
        }
    }
}

suspend fun buildUserContext(creds: String, admin: Boolean): UserIdPrincipal? {
    if (admin) {
        return if (creds == "token") {
            UserIdPrincipal(creds)
        } else {
            null
        }
    } else {
        val user = RegisterLoginRepositoryImpl().authWithApiKey(creds).firstOrNull()
        return user?.let {
            UserIdPrincipal(it.id.toString())
        }
    }
    return null
}

fun Authentication.Configuration.userAuth(
    name: String? = null,
    configure: CustomAuthenticationProvider.Configuration.() -> Unit,
) {
    configureCustomAuth(this, name, configure)
}

fun Authentication.Configuration.adminAuth(
    name: String? = null,
    configure: CustomAuthenticationProvider.Configuration.() -> Unit,
) {
    configureCustomAuth(this, name, configure)
}

private fun configureCustomAuth(
    auth: Authentication.Configuration, name: String? = null,
    configure: CustomAuthenticationProvider.Configuration.() -> Unit
) {
    val provider = CustomAuthenticationProvider.Configuration(name).apply(configure).build()
    val authenticate = provider.authenticationFunction
    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        val credentials = call.request.getAuthenticationCredentials(provider)
        val principal = credentials?.let { authenticate(call, it) }
        val cause = when {
            credentials == null -> AuthenticationFailedCause.NoCredentials
            principal == null -> AuthenticationFailedCause.InvalidCredentials
            else -> null
        }
        if (cause != null) {
            context.challenge("Invalid Credentials", cause) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid Credentials")
                it.complete()
            }
        }
        if (principal != null) {
            context.principal(principal)
        }
    }
    auth.register(provider)
}

fun ApplicationRequest.getAuthenticationCredentials(provider: CustomAuthenticationProvider): String? {
    val token = provider.authToken(call)
    return token
}

fun Application.configureAuthentication() {
    install(Authentication) {
        userAuth(AuthType.USER_TYPE.name) {
            validate { creds ->
                buildUserContext(creds, false)
            }
        }
        adminAuth(AuthType.ADMIN_TYPE.name) {
            validate { creds ->
                buildUserContext(creds, true)
            }
        }
    }
}
