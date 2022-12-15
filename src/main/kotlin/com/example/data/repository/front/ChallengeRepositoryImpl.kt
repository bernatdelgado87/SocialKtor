package com.example.data.repository.front

import com.example.data.entity.challenge.ChallengeMapper
import com.example.data.entity.challenge.ChallengesTable
import com.example.domain.model.course.ChallengeModel
import com.example.domain.repository.front.ChallengeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ChallengeRepositoryImpl : ChallengeRepository {
    override suspend fun createNewChallenge(challenge: ChallengeModel): Flow<ChallengeModel> {
        transaction {
            ChallengesTable.insert {
                it[course] = challenge.courseId
                it[requiredChallengeId] = challenge.requiredChallenge?.id
                it[isDraft] = challenge.isDraft
                it[position] = challenge.position
                it[nameSP] = challenge.name?.es
                it[nameEN] = challenge.name?.en
                it[descriptionSP] = challenge.description?.es
                it[descriptionEN] = challenge.description?.en
                it[instructionsSP] = challenge.instructions?.es
                it[instructionsEN] = challenge.instructions?.en
                it[imageThumbUrl] = challenge.imageUrlRelative
                it[videoES] = challenge.videoEsUrlRelative
                it[videoEN] = challenge.videoEnUrlRelative
            }
        }
        return flowOf(
            challenge
        )
    }

    override suspend fun updateChallenge(challenge: ChallengeModel): Flow<ChallengeModel> {
        transaction {
            ChallengesTable.update({ ChallengesTable.id eq challenge.id }) {
                it[course] = challenge.courseId
                it[requiredChallengeId] = challenge.requiredChallenge?.id
                it[isDraft] = challenge.isDraft
                it[position] = challenge.position
                it[nameSP] = challenge.name?.es
                it[nameEN] = challenge.name?.en
                it[descriptionSP] = challenge.description?.es
                it[descriptionEN] = challenge.description?.en
                it[instructionsSP] = challenge.instructions?.es
                it[instructionsEN] = challenge.instructions?.en

                if (challenge.imageName != null) {
                    it[imageThumbUrl] = challenge.imageUrlRelative
                }
                if (challenge.videoEsName != null) {
                    it[videoES] = challenge.videoEsUrlRelative
                }
                if (challenge.videoEnName != null) {
                    it[videoEN] = challenge.videoEnUrlRelative
                }
            }
        }
        return flowOf(
            challenge
        )
    }

    override suspend fun deleteChallenge(idChallenge: Int): Flow<Boolean> {
        transaction {
            ChallengesTable.deleteWhere { ChallengesTable.id eq idChallenge }
        }
        return flowOf(
            true
        )
    }

    override suspend fun getChallenge(idChallenge: Int): Flow<ChallengeModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getChallenges(idChallenge: Int?, idCourse: Int?): Flow<List<ChallengeModel>> {
        val aliasTable = ChallengesTable.alias("required")
        val challenges = transaction {
            ChallengesTable
                .leftJoin(aliasTable, {ChallengesTable.requiredChallengeId}, {aliasTable[ChallengesTable.id]} )
                .selectAll()
                .map {
                    ChallengeMapper.toModelWithPair(it to
                            aliasTable)
                }.toList()
        }
        return flowOf(challenges)
    }

}


