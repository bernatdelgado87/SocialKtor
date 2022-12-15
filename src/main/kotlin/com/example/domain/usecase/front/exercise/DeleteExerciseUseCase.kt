package com.example.domain.usecase.front.exercise

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.front.ExerciseRepositoryImpl
import com.example.domain.repository.front.ExerciseRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class DeleteExerciseUseCase(
    private val repository: ExerciseRepository = ExerciseRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
) : UseCase<Boolean, DeleteExerciseUseCase.Input>(backgroundCoroutine) {
    data class Input(val idExercise: Int? = null)

    override suspend fun run(params: Input): Flow<Boolean> {
        return repository.deleteExercise(params.idExercise!!)
    }
}
