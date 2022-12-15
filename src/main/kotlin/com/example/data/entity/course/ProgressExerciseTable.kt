package com.example.data.entity.course

import com.example.data.entity.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.jodatime.datetime

private const val TABLE_NAME = "progress_exercise"

object ProgressExerciseTable : IntIdTable(name = TABLE_NAME) {
    val userEmail = varchar("user_email_exercise_ref", 250).references(UserTable.email, onDelete =  ReferenceOption.NO_ACTION)
    val exerciseId = integer("exercise_id").references(ExerciseTable.id, onDelete =  ReferenceOption.NO_ACTION)
    val negative = integer("negatives").nullable()
    val numMalesActual = integer("num_males_actual")
    val createTime = datetime("create_time")
}
