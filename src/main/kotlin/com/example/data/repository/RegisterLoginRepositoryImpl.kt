package com.example.data.repository

import com.example.data.entity.UserMapper
import com.example.data.entity.UserTable
import com.example.domain.model.ApiException
import com.example.domain.model.ApiException.ApiIncorrectException
import com.example.domain.model.ApiException.LoginIncorrectException
import com.example.domain.model.UserModel
import com.example.domain.repository.RegisterLoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt

class RegisterLoginRepositoryImpl() : RegisterLoginRepository {
    override suspend fun register(user: UserModel): Flow<UserModel> {
        //TODO return specific error when user exists!

        val encryptedPass = BCrypt.hashpw(user.password, BCrypt.gensalt())

        var randomApiKey = java.util.UUID.randomUUID().toString()
        while (randomApiKey.length < 36) {
            randomApiKey += java.util.UUID.randomUUID().toString()
        }
        randomApiKey = randomApiKey.substring(0, 36)

        if (transaction {
                UserTable.insert {
                    it[name] = user.name!!
                    it[email] = user.email!!
                    it[password] = encryptedPass
                    it[apikey] = randomApiKey
                    it[createTime] = DateTime.now()
                }
            }.resultedValues!!.isNotEmpty()) {
            return flowOf(user.copy(apikey = randomApiKey))
        } else {
            return flowOf(UserModel())
        }
    }

    override suspend fun login(email: String, password: String): Flow<UserModel> {
        val userLogged = transaction {
            UserTable.select { UserTable.email eq email }
                .map {
                    UserMapper.toModel(it)
                }.firstOrNull()
        }
        if (userLogged == null) throw LoginIncorrectException

        if (!BCrypt.checkpw(password, userLogged.password)) throw LoginIncorrectException

        return flowOf(userLogged)
    }

    override suspend fun authWithApiKey(apikey: String): Flow<UserModel> {
        if (apikey.isNullOrEmpty()) {
            throw Exception("Error: Empty or null apikey")
        }
        val userLogged = transaction {
            UserTable.select { UserTable.apikey eq apikey }
                .map {
                    UserMapper.toModel(it)
                }.firstOrNull()
        }
        userLogged?.let {
            return flowOf(userLogged)
        } ?: throw ApiIncorrectException

    }
}
