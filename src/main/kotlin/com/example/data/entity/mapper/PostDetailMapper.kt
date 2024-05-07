package com.example.data.entity.mapper

import com.example.data.entity.LikesTable
import com.example.data.entity.UserTable
import com.example.data.entity.social.MultimediaPostTable
import com.example.domain.model.LikeSimplifiedModel
import com.example.domain.model.MultimediaModel
import com.example.domain.model.UserModel
import org.jetbrains.exposed.sql.ResultRow

class PostDetailMapper() {
    companion object {
        fun toModel(row: ResultRow): MultimediaModel = MultimediaModel(
            id = row[MultimediaPostTable.id].value,
            relativeUrl = row[MultimediaPostTable.url],
            description = row[MultimediaPostTable.description],
            user = UserModel(
                id = row[MultimediaPostTable.userRef],
                name = row[UserTable.name],
                profileImage = row[UserTable.profileImage],
            ),
            likes = listOf(
                LikeSimplifiedModel(
                    userId = row[LikesTable.userId],
                    postReference = row[LikesTable.postId]
                )
            )

        )
    }
}