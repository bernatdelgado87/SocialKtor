package com.example.data.entity.course

import com.example.data.entity.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.jodatime.datetime

private const val TABLE_NAME = "progress_course"

object ProgressCourseTable : IntIdTable(name = TABLE_NAME) {
    val userEmail = varchar("user_email_course_ref", 250).references(UserTable.email, onDelete =  ReferenceOption.NO_ACTION)
    val courseId = integer("course_id").references(CoursesTable.id, onDelete =  ReferenceOption.NO_ACTION)
    val isAssisted = integer("is_assisted").nullable()
    val state = text("course_state")
    val createTime = datetime("create_time")
}
