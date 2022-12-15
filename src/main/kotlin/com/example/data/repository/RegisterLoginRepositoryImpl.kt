package com.example.data.repository

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.application.plugins.SocialAuth.USERNAME_JWT_TOKEN
import com.example.data.entity.UserMapper
import com.example.data.entity.UserTable
import com.example.domain.model.Exceptions.LoginIncorrectException
import com.example.domain.model.UserModel
import com.example.domain.repository.RegisterLoginRepository
import io.ktor.application.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class RegisterLoginRepositoryImpl(val application: Application) : RegisterLoginRepository {
    override suspend fun register(user: UserModel): Flow<UserModel> {
        val encryptedPass = BCrypt.hashpw(user.password, BCrypt.gensalt())

        if (!transaction {
                UserTable.insert {
                    it[UserTable.name] = user.name!!
                    it[UserTable.dogName] = user.dogName!!
                    it[UserTable.genre] = user.genre!!
                    it[UserTable.breed] = user.breed!!
                    it[UserTable.email] = user.email!!
                    it[UserTable.password] = encryptedPass
                    it[createTime] = DateTime.now()
                }
            }.resultedValues!!.isEmpty()) {
            return flowOf(user)
        } else {
            return flowOf(UserModel())
        }
    }

    override suspend fun login(user: String, password: String): Flow<UserModel> {
        val userLogged = transaction {
            UserTable.select { UserTable.email eq user }
                .map {
                    UserMapper.toModel(it)
                }.firstOrNull()
        }
        if (userLogged == null) throw LoginIncorrectException
        if (!BCrypt.checkpw(password, userLogged.password)) throw LoginIncorrectException

        val secret = application.environment.config.property("jwt.secret").getString()
        val issuer = application.environment.config.property("jwt.issuer").getString()
        val audience = application.environment.config.property("jwt.audience").getString()


        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim(USERNAME_JWT_TOKEN, user)
            //fixme.withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .withExpiresAt(Date(2023, 1, 1))
            .sign(Algorithm.HMAC256(secret))

        userLogged.token = token

        return flowOf(userLogged)
    }
}
