package com.example.domain.usecase

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.LikeAndCommentsRepositoryImpl
import com.example.domain.model.LikeModel
import com.example.domain.repository.LikeAndCommentsRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class LikePostUseCase(
    private val repository: LikeAndCommentsRepository = LikeAndCommentsRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
) : UseCase<LikeModel, LikePostUseCase.Input>(backgroundCoroutine) {
    data class Input(val userId: Int, val postId: Int)

    override suspend fun run(params: Input): Flow<LikeModel> {
        return repository.publishLike(params.userId, params.postId)
    }
}
