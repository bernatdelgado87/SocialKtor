package com.example.domain.usecase

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.LikeRepositoryImpl
import com.example.domain.model.LikeModel
import com.example.domain.repository.LikeRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class FollowUserUseCase(
    private val repository: LikeRepository = LikeRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
) : UseCase<LikeModel, FollowUserUseCase.Input>(backgroundCoroutine) {
    data class Input(val userId: String, val postId: Int)

    override suspend fun run(params: Input): Flow<LikeModel> {
        return repository.publishLike(params.userId, params.postId)
    }
}
