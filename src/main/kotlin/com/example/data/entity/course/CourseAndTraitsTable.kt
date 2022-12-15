package com.example.data.entity.course

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.jodatime.datetime

private const val TABLE_NAME = "course_traits"

object CourseTraitsTable : IntIdTable(name = TABLE_NAME) {
    val courseId = integer("refs_course_id").references(CoursesTable.id, onDelete =  ReferenceOption.NO_ACTION)
    val traitId = integer("refs_trait_id").references(TraitTable.id, onDelete =  ReferenceOption.NO_ACTION)
    val createTime = datetime("create_time")
}
