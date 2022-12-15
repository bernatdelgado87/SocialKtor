package com.example.data.entity.course

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

private const val TABLE_NAME = "traits"

object TraitTable : IntIdTable(name = TABLE_NAME) {
    val name = text("name")
    val createTime = datetime("create_time")
}
