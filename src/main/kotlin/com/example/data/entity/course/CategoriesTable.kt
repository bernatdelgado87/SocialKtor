package com.example.data.entity.course

import com.example.domain.model.StringInLanguages
import com.example.domain.model.course.CategoryModel
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

private const val TABLE_NAME="categories"

object CategoriesTable: IntIdTable(name=TABLE_NAME){
    val nameSP = text("name_es").nullable()
    val nameEN = text("name_en").nullable()
    val imageUrl = text("image_url").nullable()
}

class CategoryMapper() {
    companion object {
        fun toModel(row: ResultRow): CategoryModel = CategoryModel(
            id = row[CategoriesTable.id].value,
            StringInLanguages(es = row[CategoriesTable.nameSP], en = row[CategoriesTable.nameEN]),
            urlRelative = row[CategoriesTable.imageUrl]
        )
    }
}