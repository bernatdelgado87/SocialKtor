package com.example.domain.repository

import com.example.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface RegisterLoginRepository {
    suspend fun register(user: UserModel): Flow<UserModel>

    suspend fun login(user: String, password: String): Flow<UserModel>
    suspend fun authWithApiKey(apikey: String): Flow<UserModel>


}
