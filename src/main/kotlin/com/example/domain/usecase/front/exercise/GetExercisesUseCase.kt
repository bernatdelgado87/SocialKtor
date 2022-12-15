package com.example.domain.usecase.front.exercise

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.front.ExerciseRepositoryImpl
import com.example.domain.model.course.ExerciseModel
import com.example.domain.repository.front.ExerciseRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class GetExercisesUseCase(
    private val repository: ExerciseRepository = ExerciseRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
) : UseCase<List<ExerciseModel>, GetExercisesUseCase.Input>(backgroundCoroutine) {
    data class Input(val idChallenge: Int? = null)

    override suspend fun run(params: Input): Flow<List<ExerciseModel>> {
        return repository.getExercises(idChallenge = params.idChallenge)
    }
}
