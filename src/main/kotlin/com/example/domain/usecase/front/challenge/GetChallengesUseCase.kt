package com.example.domain.usecase.front.challenge

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.front.ChallengeRepositoryImpl
import com.example.domain.model.course.ChallengeModel
import com.example.domain.repository.front.ChallengeRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class GetChallengesUseCase(
    private val repository: ChallengeRepository = ChallengeRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
) : UseCase<List<ChallengeModel>, GetChallengesUseCase.Input>(backgroundCoroutine) {
    data class Input(val idCourse: Int? = null)

    override suspend fun run(params: Input): Flow<List<ChallengeModel>> {
        return repository.getChallenges(idCourse = params.idCourse)
    }
}
