package com.example.data.entity.social

import com.example.data.entity.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.jodatime.datetime

private const val TABLE_NAME = "multimedia_post"

object MultimediaPostTable : IntIdTable(name = TABLE_NAME) {
    val url = text("url")
    val state = text("state")
    val description = text("description")

    //todo onDelete=cascade!!!
    val userRef = integer("user_ref").references(UserTable.id, onDelete = ReferenceOption.NO_ACTION)
    val numberOfLikes = integer("likes")
    val createTime = datetime("create_time")
}
