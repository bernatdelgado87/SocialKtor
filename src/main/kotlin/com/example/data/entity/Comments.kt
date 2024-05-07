package com.example.data.entity

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.jodatime.datetime

object Comments : IntIdTable(name = "comment") {

    val content = text("content")
    val createTime = datetime("create_time")

    val questionID = integer("comment_response_id")
        .references(id, onDelete = ReferenceOption.CASCADE)
}