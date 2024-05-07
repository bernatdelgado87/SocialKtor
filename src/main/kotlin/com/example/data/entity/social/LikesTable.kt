package com.example.data.entity

import com.example.data.entity.social.MultimediaPostTable
import com.example.domain.model.LikeModel
import com.example.domain.model.MultimediaModel
import com.example.domain.model.UserModel
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime

private const val TABLE_NAME = "likes"

object LikesTable : IntIdTable(name = TABLE_NAME) {
    val userId = integer("userId").references(MultimediaPostTable.id, onDelete = ReferenceOption.CASCADE)
    val postId = integer("postIdRef").references(MultimediaPostTable.id, onDelete = ReferenceOption.CASCADE)
    val createTime = datetime("create_time")
}

class LikeMapper() {
    companion object {
        fun toModel(row: ResultRow): LikeModel = LikeModel(
            userId = row[LikesTable.userId],
            postReference = row[LikesTable.postId],
            multimediaModel = MultimediaModel(
                user = UserModel(
                    id = row[MultimediaPostTable.userRef],
                    name = row[UserTable.name],
                    profileImage = row [ UserTable.profileImage],
                ),
                description = row[MultimediaPostTable.description],
                relativeUrl = row[MultimediaPostTable.url],
                numberOfLikes = row[MultimediaPostTable.numberOfLikes]
            )
        )
    }
}