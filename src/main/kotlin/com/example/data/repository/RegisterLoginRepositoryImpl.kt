package com.example.data.repository

import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.data.entity.UserMapper
import com.example.data.entity.UserTable
import com.example.domain.commons.aws.Aws3Client
import com.example.domain.commons.aws.Bucket
import com.example.domain.model.ApiException.ApiIncorrectException
import com.example.domain.model.ApiException.LoginIncorrectException
import com.example.domain.model.UserModel
import com.example.domain.repository.RegisterLoginRepository
import io.ktor.application.*
import io.ktor.util.date.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt
import java.io.InputStream

class RegisterLoginRepositoryImpl() : RegisterLoginRepository {
    override suspend fun register(
        name: String,
        email: String,
        password: String,
        photo: InputStream,
        extension: String,
        application: Application
    ): Flow<UserModel> {
        //TODO return specific error when user exists!

        val encryptedPass = BCrypt.hashpw(password, BCrypt.gensalt())

        var randomApiKey = java.util.UUID.randomUUID().toString()
        while (randomApiKey.length < 36) {
            randomApiKey += java.util.UUID.randomUUID().toString()
        }
        randomApiKey = randomApiKey.substring(0, 36)

        val nameInServer = "social/profile/$name" + getTimeMillis() + ".$extension"

        val result = Aws3Client(application).s3client.putObject(
            PutObjectRequest(
                Bucket.bucket,
                nameInServer,
                photo,
                ObjectMetadata()
            ).withCannedAcl(CannedAccessControlList.PublicRead)
        )

        if (result != null) {
            if (transaction {
                    UserTable.insert {
                        it[UserTable.name] = name
                        it[UserTable.email] = email
                        it[UserTable.password] = encryptedPass
                        it[profileImage] = nameInServer
                        it[apikey] = randomApiKey
                        it[createTime] = DateTime.now()
                    }
                }.resultedValues!!.isNotEmpty()) {
                return flowOf(UserModel(name = name, apikey = randomApiKey))
            } else {
                throw Exception("Error uploading file")
            }
        }
        throw Exception("Error uploading file")
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
