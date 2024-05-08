package com.example.data.entity.social

import com.example.data.entity.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.jodatime.datetime

private const val TABLE_NAME = "multimedia_post"

object MultimediaPostTable : IntIdTable(name = TABLE_NAME) {
    val url = text("url")
    val description = text("description")
    val userRef = integer("user_ref").references(UserTable.id, onDelete = ReferenceOption.CASCADE)
    val numberOfLikes = integer("total_likes")
    val numberOfComments = integer("total_comments")
    val createTime = datetime("create_time")
}
