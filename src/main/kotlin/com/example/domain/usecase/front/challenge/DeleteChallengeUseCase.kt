package com.example.domain.usecase.front.challenge

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.front.ChallengeRepositoryImpl
import com.example.domain.repository.front.ChallengeRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class DeleteChallengeUseCase(
    private val repository: ChallengeRepository = ChallengeRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
) : UseCase<Boolean, DeleteChallengeUseCase.Input>(backgroundCoroutine) {
    data class Input(val idChallenge: Int? = null)

    override suspend fun run(params: Input): Flow<Boolean> {
        return repository.deleteChallenge(params.idChallenge!!)
    }
}
