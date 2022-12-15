package com.example.data.entity.course

import com.example.data.entity.UserTable
import com.example.data.entity.challenge.ChallengesTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.jodatime.datetime

private const val TABLE_NAME = "progress_challenge"

object ProgressChallengeTable : IntIdTable(name = TABLE_NAME) {
    val userEmail = varchar("user_email_challenge_ref", 250).references(UserTable.email, onDelete =  ReferenceOption.NO_ACTION)
    val challengeId = integer("challenge_id").references(ChallengesTable.id, onDelete =  ReferenceOption.NO_ACTION)
    val isAssisted = integer("is_assisted").nullable()
    val state = text("challenge_state")
    val createTime = datetime("create_time")
}
