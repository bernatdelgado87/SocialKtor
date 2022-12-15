package com.example.data.entity.course

import com.example.domain.model.StringInLanguages
import com.example.domain.model.course.CategoryModel
import com.example.domain.model.course.CourseModel
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow

private const val TABLE_NAME = "courses"

object CoursesTable : IntIdTable(name = TABLE_NAME) {
    val category = integer("ref_category_id").references(CategoriesTable.id, onDelete = ReferenceOption.NO_ACTION)
    val requiredCourseId =
        integer("ref_required_course_id").references(CoursesTable.id, onDelete = ReferenceOption.NO_ACTION).nullable()
    val isPopular = bool("is_popular").nullable()
    val isDraft = bool("is_draft").nullable()
    val isFree = bool("is_free").nullable()
    val position = integer("position").nullable()
    val nameSP = text("name_es").nullable()
    val nameEN = text("name_en").nullable()
    val warningSP = text("warning_es").nullable()
    val warningEN = text("warning_en").nullable()
    val imageUrl = text("image_url").nullable()
    val videoHelpUrl = text("video_help_url").nullable()
}

class CourseMapper() {
    companion object {
        fun toModel(row: ResultRow): CourseModel = CourseModel(
            id = row[CoursesTable.id].value,
            category = CategoryModel(id = row[CoursesTable.category]),
            requiredCourse = CourseModel(id = row[CoursesTable.requiredCourseId]),
            isPopular = row[CoursesTable.isPopular],
            isDraft = row[CoursesTable.isDraft],
            isFree = row[CoursesTable.isFree],
            position = row[CoursesTable.position],
            name = StringInLanguages(row[CoursesTable.nameSP], row[CoursesTable.nameEN]),
            warning = StringInLanguages(row[CoursesTable.warningSP], row[CoursesTable.warningEN]),
            urlRelative = row[CoursesTable.imageUrl],
            videoHelpurlRelative = row[CoursesTable.videoHelpUrl]

        )


    }
}