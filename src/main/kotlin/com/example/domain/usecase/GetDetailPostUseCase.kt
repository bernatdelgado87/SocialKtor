package com.example.domain.usecase

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.MultimediaPostRepositoryImpl
import com.example.domain.model.MultimediaModel
import com.example.domain.repository.MultimediaPostRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class GetDetailPostUseCase(
    private val repository: MultimediaPostRepository = MultimediaPostRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
): UseCase<MultimediaModel, GetDetailPostUseCase.Input>(backgroundCoroutine) {
    data class Input(val postID: Int)

    override suspend fun run(params: Input): Flow<MultimediaModel> {
        return repository.getDetailPost(params.postID)
    }

}