package com.example.domain.repository.front

import com.example.domain.model.course.CategoryModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun createNewCategory(Category: CategoryModel): Flow<CategoryModel>

    suspend fun updateCategory(Category: CategoryModel): Flow<CategoryModel>

    suspend fun deleteCategory(idCategory: Int): Flow<Boolean>

    suspend fun getCategories(): Flow<List<CategoryModel>>
}