package com.example.data.entity

import com.example.data.entity.social.MultimediaPostTable
import com.example.domain.model.LikeModel
import com.example.domain.model.MultimediaModel
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime

private const val TABLE_NAME = "likes"

object LikesTable : IntIdTable(name = TABLE_NAME) {
    val userId = text("userId") //todo references here
    val postId = integer("postIdRef").references(MultimediaPostTable.id, onDelete = ReferenceOption.NO_ACTION)
    val createTime = datetime("create_time")
}

class LikeMapper() {
    companion object {
        fun toModel(row: ResultRow): LikeModel = LikeModel(
            userId = row[LikesTable.userId],
            postReference = row[LikesTable.postId],
            multimediaModel = MultimediaModel(
                userId = row[MultimediaPostTable.userRef],
                description = row[MultimediaPostTable.description],
                relativeUrl = row[MultimediaPostTable.url],
                numberOfLikes = row[MultimediaPostTable.numberOfLikes]
            )
        )
    }
}