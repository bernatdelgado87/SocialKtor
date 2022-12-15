package com.example.data.entity.course

import com.example.data.entity.challenge.ChallengesTable
import com.example.domain.model.StringInLanguages
import com.example.domain.model.course.ExerciseModel
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow

private const val TABLE_NAME = "exercises"

object ExerciseTable : IntIdTable(name = TABLE_NAME) {
    val challengeId = integer("ref_challenge_id").references(ChallengesTable.id, onDelete = ReferenceOption.NO_ACTION)
    val isDraft = bool("is_draft")
    val nameSP = text("name_es").nullable()
    val nameEN = text("name_en").nullable()
    val position = integer("position").nullable()
    val descriptionSP = text("description_es").nullable()
    val descriptionEN = text("description_en").nullable()
    val imageUrl = text("imageUrl").nullable()
    val videoSP = text("video_es").nullable()
    val videoEN = text("video_en").nullable()
}

class ExerciseMapper() {
    companion object {
        fun toModel(row: ResultRow): ExerciseModel = ExerciseModel(
            id = row[ExerciseTable.id].value,
            challengeId = row[ExerciseTable.challengeId],
            isDraft = row[ExerciseTable.isDraft],
            name = StringInLanguages(row[ExerciseTable.nameSP], row[ExerciseTable.nameEN]),
            position = row[ExerciseTable.position],
            description = StringInLanguages(row[ExerciseTable.descriptionSP], row[ExerciseTable.descriptionEN]),
            imageUrlRelative = row[ExerciseTable.imageUrl],
            videoEsUrlRelative = row[ExerciseTable.videoSP],
            videoEnUrlRelative = row[ExerciseTable.videoEN]
        )
    }

}
