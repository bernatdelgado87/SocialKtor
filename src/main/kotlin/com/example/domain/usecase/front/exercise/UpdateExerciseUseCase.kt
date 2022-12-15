package com.example.domain.usecase.front.exercise

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.FileUploader
import com.example.data.repository.front.ExerciseRepositoryImpl
import com.example.domain.model.course.EXERCISES_FILES_URL_RELATIVE
import com.example.domain.model.course.ExerciseModel
import com.example.domain.repository.front.ExerciseRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import io.ktor.http.content.*
import kotlinx.coroutines.flow.Flow

class UpdateExerciseUseCase(
    private val repository: ExerciseRepository = ExerciseRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
) : UseCase<ExerciseModel, UpdateExerciseUseCase.Input>(backgroundCoroutine) {
    data class Input(val ExerciseModel: ExerciseModel, val fileItems: List<PartData.FileItem>?)

    override suspend fun run(params: Input): Flow<ExerciseModel> {
        if (params.fileItems != null && !params.fileItems.isEmpty()) {
            for (file in params.fileItems) {
                val inputStream = file.streamProvider()
                FileUploader.uploadFileToAWS(EXERCISES_FILES_URL_RELATIVE + file.originalFileName, inputStream = inputStream)
            }
        }
        return repository.updateExercise(params.ExerciseModel)
    }
}
