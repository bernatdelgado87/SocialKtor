package com.example.domain.repository.front

import com.example.domain.model.course.CourseModel
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    suspend fun createNewCourse(course: CourseModel): Flow<CourseModel>

    suspend fun updateCourse(course: CourseModel): Flow<CourseModel>

    suspend fun deleteCourse(idCourse: Int): Flow<Boolean>

    suspend fun getCourse(idCourse: Int): Flow<CourseModel>

    suspend fun getCourses(idCategory: Int? = null): Flow<List<CourseModel>>


}