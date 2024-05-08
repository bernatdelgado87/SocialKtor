package com.example.data.entity

import com.example.data.entity.LikesTable.references
import com.example.data.entity.social.MultimediaPostTable
import com.example.domain.model.CommentModel
import com.example.domain.model.Question
import com.example.domain.model.UserModel
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime

object CommentsTable : IntIdTable(name = "comment") {
    val content = text("content")
    val userId = integer("userId").references(UserTable.id, onDelete = ReferenceOption.CASCADE)
    val postId = integer("postIdRef").references(MultimediaPostTable.id, onDelete = ReferenceOption.CASCADE)
    val createTime = datetime("create_time")
    val commentReference = integer("comment_response_id")
        .references(id, onDelete = ReferenceOption.CASCADE).nullable()
}

class CommentsMapper() {
    companion object {
        fun toModel(row: ResultRow): CommentModel = CommentModel(
        id = row[CommentsTable.id].value,
        content  = row[CommentsTable.content],
        createTime  = row[CommentsTable.createTime].toString(),
        user = UserMapper.toModel(row),
        commentReferent = null //todo map responses from other comments
        )
    }
}