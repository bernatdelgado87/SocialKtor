package com.example.data.repository.front

import com.example.data.entity.course.ExerciseMapper
import com.example.data.entity.course.ExerciseTable
import com.example.domain.model.course.ExerciseModel
import com.example.domain.repository.front.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ExerciseRepositoryImpl : ExerciseRepository {
    override suspend fun createNewExercise(exercise: ExerciseModel): Flow<ExerciseModel> {
        transaction {
            ExerciseTable.insert {
                it[challengeId] = exercise.challengeId
                it[isDraft] = exercise.isDraft == true
                it[nameSP] = exercise.name?.es
                it[nameEN] = exercise.name?.en
                it[position] = exercise.position
                it[descriptionSP] = exercise.description?.es
                it[descriptionEN] = exercise.description?.en
                it[imageUrl] = exercise.imageUrlRelative
                it[videoSP] = exercise.videoEsUrlRelative
                it[videoEN] = exercise.videoEnUrlRelative
            }
        }
        return flowOf(
            exercise
        )
    }

    override suspend fun updateExercise(exercise: ExerciseModel): Flow<ExerciseModel> {
        transaction {
            ExerciseTable.update({ ExerciseTable.id eq exercise.id }) {
                it[challengeId] = exercise.challengeId
                it[isDraft] = exercise.isDraft == true
                it[nameSP] = exercise.name?.es
                it[nameEN] = exercise.name?.en
                it[position] = exercise.position
                it[descriptionSP] = exercise.description?.es
                it[descriptionEN] = exercise.description?.en
                it[imageUrl] = exercise.imageUrlRelative
                it[videoSP] = exercise.videoEsUrlRelative
                it[videoEN] = exercise.videoEnUrlRelative
            }
        }
        return flowOf(
            exercise
        )
    }

    override suspend fun deleteExercise(idExercise: Int): Flow<Boolean> {
        transaction {
            ExerciseTable.deleteWhere { ExerciseTable.id eq idExercise }
        }
        return flowOf(
            true
        )
    }

    override suspend fun getExercise(idExercise: Int): Flow<ExerciseModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getExercises(idChallenge: Int?): Flow<List<ExerciseModel>> {
        val exercises = transaction {
            ExerciseTable
                .selectAll()
                .map {
                    ExerciseMapper.toModel(it)
                }.toList()
        }
        return flowOf(exercises)
    }
}



