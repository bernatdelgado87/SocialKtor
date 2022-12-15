package com.example.domain.repository.front

import com.example.domain.model.course.ChallengeModel
import kotlinx.coroutines.flow.Flow

interface ChallengeRepository {
    suspend fun createNewChallenge(course: ChallengeModel): Flow<ChallengeModel>

    suspend fun updateChallenge(course: ChallengeModel): Flow<ChallengeModel>

    suspend fun deleteChallenge(idChallenge: Int): Flow<Boolean>

    suspend fun getChallenge(idChallenge: Int): Flow<ChallengeModel>

    suspend fun getChallenges(idChallenge: Int? = null, idCourse: Int? = null): Flow<List<ChallengeModel>>

}