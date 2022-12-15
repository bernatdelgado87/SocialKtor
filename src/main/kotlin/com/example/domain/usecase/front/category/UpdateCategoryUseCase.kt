package com.example.domain.usecase.front.category

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.FileUploader
import com.example.data.repository.front.CategoryRepositoryImpl
import com.example.domain.model.course.CategoryModel
import com.example.domain.repository.front.CategoryRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import io.ktor.http.content.*
import kotlinx.coroutines.flow.Flow

class UpdateCategoryUseCase (
    private val repository: CategoryRepository = CategoryRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
    ) : UseCase<CategoryModel, UpdateCategoryUseCase.Input>(backgroundCoroutine) {
    data class Input(val categoryModel: CategoryModel, val fileItem: PartData.FileItem?)

    override suspend fun run(params: Input): Flow<CategoryModel> {
        if (params.fileItem != null) {
            val inputStream = params.fileItem.streamProvider()
            FileUploader.uploadFileToAWS(path = params.categoryModel.urlRelative!!, inputStream = inputStream)
        }
            return repository.updateCategory(params.categoryModel)
    }
}