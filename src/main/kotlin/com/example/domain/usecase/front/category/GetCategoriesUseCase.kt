package com.example.domain.usecase.front.category

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.front.CategoryRepositoryImpl
import com.example.domain.model.course.CategoryModel
import com.example.domain.repository.front.CategoryRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(
    private val repository: CategoryRepository = CategoryRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
) : UseCase<List<CategoryModel>, GetCategoriesUseCase.Input>(backgroundCoroutine) {
    data class Input(val idCategory: Int? = null)

    override suspend fun run(params: Input): Flow<List<CategoryModel>> {
        return repository.getCategories()
    }
}
