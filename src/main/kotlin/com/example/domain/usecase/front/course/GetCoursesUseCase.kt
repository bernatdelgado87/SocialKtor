package com.example.domain.usecase.front.course

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.front.CourseRepositoryImpl
import com.example.domain.model.course.CourseModel
import com.example.domain.repository.front.CourseRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class GetCoursesUseCase(
    private val repository: CourseRepository = CourseRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
) : UseCase<List<CourseModel>, GetCoursesUseCase.Input>(backgroundCoroutine) {
    data class Input(val idCategory: Int? = null)

    override suspend fun run(params: Input): Flow<List<CourseModel>> {
        return repository.getCourses(idCategory = params.idCategory)
    }
}
