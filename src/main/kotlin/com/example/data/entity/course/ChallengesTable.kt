package com.example.data.entity.challenge

import com.example.data.entity.course.CoursesTable
import com.example.domain.model.StringInLanguages
import com.example.domain.model.course.ChallengeModel
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Alias
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow

private const val TABLE_NAME = "challenges"

object ChallengesTable : IntIdTable(name = TABLE_NAME) {
    val course = integer("ref_course_id").references(CoursesTable.id, onDelete = ReferenceOption.NO_ACTION)
    val requiredChallengeId =
        integer("ref_required_challenge_id").references(ChallengesTable.id, onDelete = ReferenceOption.NO_ACTION)
            .nullable()
    val isDraft = bool("is_draft").nullable()
    val position = integer("position").nullable()
    val nameSP = text("name_es").nullable()
    val nameEN = text("name_en").nullable()
    val descriptionSP = text("description_es").nullable()
    val descriptionEN = text("description_en").nullable()
    val instructionsSP = text("instructions_es").nullable()
    val instructionsEN = text("instructions_en").nullable()
    val imageThumbUrl = text("image_thumb_url").nullable()
    val videoES = text("video_es_url").nullable()
    val videoEN = text("video_en_url").nullable()
}

class ChallengeMapper() {
    companion object {
        fun toModel(row: ResultRow): ChallengeModel = ChallengeModel(
            id = row[ChallengesTable.id].value,
            courseId = row[ChallengesTable.course],
            requiredChallenge = ChallengeModel(
                id = row[ChallengesTable.requiredChallengeId],
                courseId = row[ChallengesTable.course]
            ),
            isDraft = row[ChallengesTable.isDraft],
            position = row[ChallengesTable.position],
            name = StringInLanguages(row[ChallengesTable.nameSP], row[ChallengesTable.nameEN]),
            description = StringInLanguages(row[ChallengesTable.descriptionSP], row[ChallengesTable.descriptionEN]),
            instructions = StringInLanguages(
                row[ChallengesTable.instructionsSP], row[ChallengesTable.instructionsEN]
            ),
            imageUrlRelative = row[ChallengesTable.imageThumbUrl],
            videoEsUrlRelative = row[ChallengesTable.videoES],
            videoEnUrlRelative = row[ChallengesTable.videoEN],
        )

        fun toModelWithPair(pair: Pair<ResultRow, Alias<ChallengesTable>>): ChallengeModel = ChallengeModel(
            id = pair.first[ChallengesTable.id].value,
            courseId = pair.first[ChallengesTable.course],
            requiredChallenge =
            if (pair.first[ChallengesTable.requiredChallengeId] != null) ChallengeModel(
                id = pair.first[pair.second[ChallengesTable.id]].value,
                courseId = pair.first[pair.second[ChallengesTable.course]],
                name = StringInLanguages(
                    pair.first[pair.second[ChallengesTable.nameSP]],
                    pair.first[pair.second[ChallengesTable.nameEN]]
                )
            ) else null,
            isDraft = pair.first[ChallengesTable.isDraft],
            position = pair.first[ChallengesTable.position],
            name = StringInLanguages(pair.first[ChallengesTable.nameSP], pair.first[ChallengesTable.nameEN]),
            description = StringInLanguages(
                pair.first[ChallengesTable.descriptionSP],
                pair.first[ChallengesTable.descriptionEN]
            ),
            instructions = StringInLanguages(
                pair.first[ChallengesTable.instructionsSP], pair.first[ChallengesTable.instructionsEN]
            ),
            imageUrlRelative = pair.first[ChallengesTable.imageThumbUrl],
            videoEsUrlRelative = pair.first[ChallengesTable.videoES],
            videoEnUrlRelative = pair.first[ChallengesTable.videoEN],
        )
    }
}
