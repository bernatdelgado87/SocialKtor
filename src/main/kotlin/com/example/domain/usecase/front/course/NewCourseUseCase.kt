package com.example.domain.usecase.front.course

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.FileUploader
import com.example.data.repository.front.CourseRepositoryImpl
import com.example.domain.model.course.CourseModel
import com.example.domain.repository.front.CourseRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import io.ktor.http.content.*
import kotlinx.coroutines.flow.Flow

class NewCourseUseCase(
    private val repository: CourseRepository = CourseRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
) : UseCase<CourseModel, NewCourseUseCase.Input>(backgroundCoroutine) {
    data class Input(val courseModel: CourseModel, val fileItem: PartData.FileItem?)

    override suspend fun run(params: Input): Flow<CourseModel> {
        if (params.fileItem != null) {
            val inputStream = params.fileItem.streamProvider()
            FileUploader.uploadFileToAWS(path = params.courseModel.urlRelative!!, inputStream = inputStream)
        }
        return repository.createNewCourse(params.courseModel)
    }
}
