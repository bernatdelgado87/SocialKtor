package com.example.domain.usecase

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.MultimediaPostRepositoryImpl
import com.example.domain.model.MultimediaFeed
import com.example.domain.repository.MultimediaPostRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class GetFeedUseCase(
    private val repository: MultimediaPostRepository = MultimediaPostRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
): UseCase<MultimediaFeed, GetFeedUseCase.Input>(backgroundCoroutine) {
    data class Input(val userId: String)

    override suspend fun run(params: Input): Flow<MultimediaFeed> {
        return repository.getFeed(params.userId)
    }

}