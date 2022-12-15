package com.example.domain.usecase

import com.example.data.repository.RegisterLoginRepositoryImpl
import com.example.domain.model.UserModel
import com.example.domain.repository.RegisterLoginRepository
import es.experis.app.domain.usecase.UseCase
import io.ktor.application.*
import kotlinx.coroutines.flow.Flow

class LoginUseCase(val application: Application
) : UseCase<UserModel, LoginUseCase.Input>() {
    data class Input(val user: String, val password: String)

    override suspend fun run(params: Input): Flow<UserModel> {
        val repository: RegisterLoginRepository = RegisterLoginRepositoryImpl(application)
        return repository.login(params.user, params.password)
    }
}
