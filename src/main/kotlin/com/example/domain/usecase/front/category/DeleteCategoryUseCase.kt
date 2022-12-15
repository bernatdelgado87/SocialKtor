package com.example.domain.usecase.front.category

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.front.CategoryRepositoryImpl
import com.example.domain.repository.front.CategoryRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class DeleteCategoryUseCase(
    private val repository: CategoryRepository = CategoryRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
) : UseCase<Boolean, DeleteCategoryUseCase.Input>(backgroundCoroutine) {
    data class Input(val idCategory: Int? = null)

    override suspend fun run(params: Input): Flow<Boolean> {
        return repository.deleteCategory(params.idCategory!!)
    }
}
