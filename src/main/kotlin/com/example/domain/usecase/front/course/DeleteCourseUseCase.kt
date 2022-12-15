package com.example.domain.usecase.front.course

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.front.CourseRepositoryImpl
import com.example.domain.repository.front.CourseRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class DeleteCourseUseCase(
    private val repository: CourseRepository = CourseRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
) : UseCase<Boolean, DeleteCourseUseCase.Input>(backgroundCoroutine) {
    data class Input(val idCourse: Int? = null)

    override suspend fun run(params: Input): Flow<Boolean> {
        return repository.deleteCourse(params.idCourse!!)
    }
}
