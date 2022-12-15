package com.example.domain.usecase

import com.example.domain.model.MultimediaModel
import com.example.domain.repository.MultimediaPostRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import io.ktor.application.*
import kotlinx.coroutines.flow.Flow


class UploadUseCase(
    private val repository: MultimediaPostRepository,
    backgroundCoroutine: BackgroundCoroutine
) : UseCase<MultimediaModel, UploadUseCase.Input>(backgroundCoroutine) {
    data class Input(val call: ApplicationCall)

    override suspend fun run(params: Input): Flow<MultimediaModel> =
            repository.publishMultimediaPost(params.call)
    }