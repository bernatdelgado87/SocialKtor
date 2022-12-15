package com.example.domain.usecase.users

import com.example.data.repository.RegisterLoginRepositoryImpl
import com.example.domain.model.UserModel
import com.example.domain.repository.RegisterLoginRepository
import es.experis.app.domain.usecase.UseCase
import io.ktor.application.*
import kotlinx.coroutines.flow.Flow

class RegisterUserCase(
    val application: Application
) : UseCase<UserModel, RegisterUserCase.Input>() {
    data class Input(val user: UserModel)

    override suspend fun run(params: Input): Flow<UserModel> {
        val repository: RegisterLoginRepository = RegisterLoginRepositoryImpl(application)
        return repository.register(params.user)
    }
}
