package com.example.domain.repository

import com.example.domain.model.UserModel
import io.ktor.application.*
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface RegisterLoginRepository {
    suspend fun register(apikey: String, name: String?, email: String?, password: String?, photo: InputStream?, extension: String?, application: Application): Flow<UserModel>
    suspend fun update(userId: Int, name: String, email: String?, password: String?, photo: InputStream, extension: String, application: Application): Flow<UserModel>

    suspend fun login(user: String, password: String): Flow<UserModel>
    suspend fun authWithApiKey(apikey: String): Flow<UserModel>


}
