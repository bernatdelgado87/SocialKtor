package com.example.presentation.routes.frontend

import com.example.domain.commons.extractParts
import com.example.domain.model.StringInLanguages
import com.example.domain.model.course.CategoryModel
import com.example.domain.usecase.front.category.DeleteCategoryUseCase
import com.example.domain.usecase.front.category.GetCategoriesUseCase
import com.example.domain.usecase.front.category.NewCategoryUseCase
import com.example.domain.usecase.front.category.UpdateCategoryUseCase
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.mustache.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.collect

fun Route.categoryRoute() {
    route("/categories") {

        get("/") {
            //return all categories
            val usecase = GetCategoriesUseCase()
            usecase(GetCategoriesUseCase.Input()).collect() {
                call.respond(MustacheContent("index.hbs", mapOf("categories" to it)))
            }
        }

        post("/createCategoryAction") {
            val post = call.extractParts()
            val nameEs: String? = (post["name_es"] as? PartData.FormItem)?.value
            val nameEn: String? = (post["name_en"] as? PartData.FormItem)?.value
            val idCategory: String? =
                if ((post["id_categoría"] as? PartData.FormItem)?.value?.isEmpty() == true) null else (post["id_categoría"] as?
                        PartData.FormItem)?.value
            val fileItem: PartData.FileItem? =
                if ((post["file1"] as? PartData.FileItem)?.originalFileName?.isEmpty() == true) null else post["file1"] as? PartData.FileItem

            val category = CategoryModel(
                id = idCategory?.toInt(),
                name = StringInLanguages(nameEs, nameEn),
                fileName = fileItem?.originalFileName
            )
            if (idCategory?.isEmpty() == false) {
                val usecase = UpdateCategoryUseCase()
                usecase(UpdateCategoryUseCase.Input(category, fileItem)).collect() {
                    call.respondRedirect("/admin/categories/")
                }
            } else {
                val usecase = NewCategoryUseCase()
                usecase(NewCategoryUseCase.Input(category, fileItem)).collect() {
                    call.respondRedirect("/admin/categories/")
                }
            }
        }

        get("/deleteCategory/") {
            val idCategory: String? = call.request?.queryParameters["idCategory"]
            //return all categories
            val usecase = DeleteCategoryUseCase()
            usecase(DeleteCategoryUseCase.Input(idCategory?.toInt())).collect() {
                call.respondRedirect("/admin/categories/")
            }
        }
    }

}