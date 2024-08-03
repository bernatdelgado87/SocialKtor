package com.example.data.entity

import com.example.domain.model.UserModel
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime

private const val TABLE_NAME = "user_registered"

object UserTable : IntIdTable(name = TABLE_NAME) {
    val name = text("name").uniqueIndex().nullable()
    val email = varchar("email", 255).uniqueIndex().nullable()
    val password = text("password").nullable()
    val profileImage = text("profile_image").nullable()
    val apikey = text("apikey").uniqueIndex()
    val createTime = datetime("create_time")
}

class UserMapper() {
    companion object {
        fun toModel(row: ResultRow): UserModel = UserModel(
            id = row[UserTable.id].value,
            relativeUrlImageProfile = row[UserTable.profileImage],
            name = row[UserTable.name],
            email = row[UserTable.email],
            apikey = row[UserTable.apikey],
            password = row[UserTable.password]
        )
    }
}
