package com.example.data.entity

import com.example.domain.model.UserModel
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime

private const val TABLE_NAME = "user_registered"

object UserTable : IntIdTable(name = TABLE_NAME) {
    val name = text("name")
    val email = varchar("email", 255).uniqueIndex()
    val password = text("password")
    val dogName = text("dog_name")
    val genre = text("genre")
    val breed = integer("breed")
    val createTime = datetime("create_time")
}

class UserMapper() {
    companion object {
        fun toModel(row: ResultRow): UserModel = UserModel(
            id = row[UserTable.id].value,
            name = row[UserTable.name],
            dogName = row[UserTable.dogName],
            genre = row[UserTable.genre],
            breed = row[UserTable.breed],
            email = row[UserTable.email],
            password = row[UserTable.password]
        )
    }
}
