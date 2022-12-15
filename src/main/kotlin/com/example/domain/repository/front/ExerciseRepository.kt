package com.example.domain.repository.front

import com.example.domain.model.course.ExerciseModel
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    suspend fun createNewExercise(course: ExerciseModel): Flow<ExerciseModel>

    suspend fun updateExercise(course: ExerciseModel): Flow<ExerciseModel>

    suspend fun deleteExercise(idExercise: Int): Flow<Boolean>

    suspend fun getExercise(idExercise: Int): Flow<ExerciseModel>

    suspend fun getExercises(idChallenge: Int? = null): Flow<List<ExerciseModel>>

}
