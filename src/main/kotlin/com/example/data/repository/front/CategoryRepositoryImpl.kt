package com.example.data.repository.front

import com.example.data.entity.course.CategoriesTable
import com.example.data.entity.course.CategoryMapper
import com.example.domain.model.course.CategoryModel
import com.example.domain.repository.front.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class CategoryRepositoryImpl : CategoryRepository {
    override suspend fun createNewCategory(category: CategoryModel): Flow<CategoryModel> {
        transaction {
            CategoriesTable.insert {
                it[nameSP] = category.name?.es
                it[nameEN] = category.name?.en
                it[imageUrl] = category.urlRelative
            }
        }
        return flowOf(
            category
        )
    }

    override suspend fun updateCategory(category: CategoryModel): Flow<CategoryModel> {
        transaction {
            CategoriesTable.update({ CategoriesTable.id eq category.id }) {
                it[nameSP] = category.name?.es
                it[nameEN] = category.name?.en
                if(category.fileName != null) {
                    it[imageUrl] = category.urlRelative
                }
            }
        }
        return flowOf(
            category
        )
    }

    override suspend fun deleteCategory(idCategory: Int): Flow<Boolean> {
        transaction {
            CategoriesTable.deleteWhere { CategoriesTable.id eq idCategory }
        }
        return flowOf(
            true
        )
    }

    override suspend fun getCategories(): Flow<List<CategoryModel>> {
        val categories = transaction {
            CategoriesTable
                .selectAll()
                .map {
                    CategoryMapper.toModel(it)
                }.toList()
        }
        return flowOf(categories)
    }


}