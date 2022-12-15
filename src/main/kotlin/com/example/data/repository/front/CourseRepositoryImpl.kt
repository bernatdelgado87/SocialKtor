package com.example.data.repository.front

import com.example.data.entity.course.CourseMapper
import com.example.data.entity.course.CoursesTable
import com.example.domain.model.course.CourseModel
import com.example.domain.repository.front.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class CourseRepositoryImpl : CourseRepository {
    override suspend fun createNewCourse(course: CourseModel): Flow<CourseModel> {
        transaction {
            CoursesTable.insert {
                it[requiredCourseId] = course.requiredCourse?.id
                it[category] = course.category?.id!!
                it[isPopular] = course.isPopular == true
                it[isDraft] = course.isPopular == true
                it[isFree] = course.isPopular == true
                it[position] = course.position?.let { course.position } ?: null
                it[nameSP] = course.name?.es?.let { course.name.es } ?: null
                it[nameEN] = course.name?.en?.let { course.name.en } ?: null
                it[warningSP] = course.warning?.es?.let { course.warning.es } ?: null
                it[warningEN] = course.warning?.en?.let { course.warning.en } ?: null
                it[imageUrl] = course.urlRelative
            }
        }

        return flowOf(
            course
        )
    }

    override suspend fun updateCourse(course: CourseModel): Flow<CourseModel> {
        transaction {
            CoursesTable.update({ CoursesTable.id eq course.id }) {
                it[requiredCourseId] = course.requiredCourse?.id
                it[category] = course.category?.id!!
                it[isPopular] = course.isPopular == true
                it[isDraft] = course.isPopular == true
                it[isFree] = course.isPopular == true
                it[position] = course.position?.let { course.position } ?: null
                it[nameSP] = course.name?.es?.let { course.name.es } ?: null
                it[nameEN] = course.name?.en?.let { course.name.en } ?: null
                it[warningSP] = course.warning?.es?.let { course.warning.es } ?: null
                it[warningEN] = course.warning?.en?.let { course.warning.en } ?: null
                if(course.fileName != null) {
                    it[imageUrl] = course.urlRelative
                }
            }
        }

        return flowOf(
            course
        )
    }

    override suspend fun deleteCourse(idCourse: Int): Flow<Boolean> {
        transaction {
            CoursesTable.deleteWhere { CoursesTable.id eq idCourse }
        }
        return flowOf(
            true
        )
    }

    override suspend fun getCourse(idCourse: Int): Flow<CourseModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getCourses(idCategory: Int?): Flow<List<CourseModel>> {
        val courses = transaction {
            CoursesTable
                .selectAll()
                .map {
                    CourseMapper.toModel(it)
                }.toList()
        }
        return flowOf(courses)
    }
}


