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
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt
import java.io.InputStream

class RegisterLoginRepositoryImpl() : RegisterLoginRepository {
    override suspend fun register(
        apikey: String,
        name: String?,
        email: String?,
        password: String?,
        photo: InputStream?,
        extension: String?,
        application: Application
    ): Flow<UserModel> {
        //TODO return specific error when user exists!

        val encryptedPass = password?.let {BCrypt.hashpw(password, BCrypt.gensalt())}
        val nameInServer = photo?.let { "social/profile/$name" + getTimeMillis() + ".$extension"}

        val result = photo?.let {Aws3Client(application).s3client.putObject(
            PutObjectRequest(
                Bucket.bucket,
                nameInServer,
                photo,
                ObjectMetadata()
            ).withCannedAcl(CannedAccessControlList.PublicRead)
        ) }

            if (transaction {
                    UserTable.insert {insert ->
                        name?.let { insert[UserTable.name] = name }
                        email?.let { insert[UserTable.email] = email}
                        encryptedPass?.let {insert[UserTable.password] = encryptedPass }
                        nameInServer?.let { insert[profileImage] = nameInServer }
                        insert[UserTable.apikey] = apikey
                        insert[createTime] = DateTime.now()
                    }
                }.resultedValues!!.isNotEmpty()) {
                return flowOf(UserModel(name = name, apikey = apikey))
            } else {
                throw Exception("Error uploading file")
            }
        throw Exception("Error uploading file")
    }

    override suspend fun update(
        userId: Int,
        name: String,
        email: String?,
        password: String?,
        photo: InputStream,
        extension: String,
        application: Application
    ): Flow<UserModel> {
        val encryptedPass = BCrypt.hashpw(password, BCrypt.gensalt())
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
            transaction {
                UserTable.update({ UserTable.id eq userId }) { update ->
                    update[UserTable.name] = name
                    email?.let { update[UserTable.email] = email }
                    password?.let { update[UserTable.password] = encryptedPass }
                    update[profileImage] = nameInServer
                    update[createTime] = DateTime.now()
                }
            }
            return flowOf(UserModel(name = name))
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
