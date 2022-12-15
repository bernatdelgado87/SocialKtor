package com.example.presentation.routes.frontend

import com.example.domain.commons.extractParts
import com.example.domain.model.StringInLanguages
import com.example.domain.model.course.CategoryModel
import com.example.domain.model.course.CourseModel
import com.example.domain.usecase.front.course.DeleteCourseUseCase
import com.example.domain.usecase.front.course.GetCoursesUseCase
import com.example.domain.usecase.front.course.NewCourseUseCase
import com.example.domain.usecase.front.course.UpdateCourseUseCase
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.mustache.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.collect

fun Route.courseRoute() {
    route("/courses") {
        get("/") {
            val idCategory: String? = call.request?.queryParameters["idCategory"]
            if (idCategory != null) {
                val usecase = GetCoursesUseCase()
                usecase(GetCoursesUseCase.Input(idCategory = idCategory.toInt())).collect() {
                    call.respond(MustacheContent("courses.hbs", mapOf("courses" to it)))
                }
            } else {
                val usecase = GetCoursesUseCase()
                usecase(GetCoursesUseCase.Input(idCategory = idCategory)).collect() {
                    call.respond(MustacheContent("courses.hbs", mapOf("courses" to it)))
                }
            }
        }

        post("/createCourseAction") {
            val post = call.extractParts()
            val nameEs: String? = (post["name_es"] as? PartData.FormItem)?.value
            val nameEn: String? = (post["name_en"] as? PartData.FormItem)?.value
            val idCategory: String? = (post["id_category"] as? PartData.FormItem)?.value
            val idCourse: String? = if ((post["id_course"] as?
                        PartData.FormItem)?.value?.isEmpty() == true
            ) null
            else
                (post["id_course"] as?
                        PartData.FormItem)?.value
            val warningEs: String? = (post["warning_es"] as? PartData.FormItem)?.value
            val warningEn: String? = (post["warning_en"] as? PartData.FormItem)?.value
            val fileItem: PartData.FileItem? =
                if ((post["file1"] as? PartData.FileItem)?.originalFileName?.isEmpty() == true) null else post["file1"] as? PartData.FileItem

            val course = CourseModel(
                id = idCourse?.toInt(),
                name = StringInLanguages(nameEs, nameEn),
                category = CategoryModel(
                    idCategory!!.toInt(),
                    StringInLanguages(null, null)
                ),
                warning = StringInLanguages(
                    warningEs,
                    warningEn
                ),
                isPopular = null,
                isDraft = null,
                isFree = null,
                position = null,
                fileName = fileItem?.originalFileName
            )


            if (idCourse?.isEmpty() == false) {
                val usecase = UpdateCourseUseCase()
                usecase(UpdateCourseUseCase.Input(course, fileItem)).collect() {
                    call.respondRedirect("/admin/courses/?idCategory=" + idCategory)
                }
            } else {
                val usecase = NewCourseUseCase()
                usecase(NewCourseUseCase.Input(course, fileItem)).collect() {
                    call.respondRedirect("/admin/courses/?idCategory=" + idCategory)
                }
            }
        }

        get("/deleteCourse/") {
            val idCourse: String? = call.request?.queryParameters["idCourse"]
            val idCategory: String? = call.request?.queryParameters["idCategory"]
            //return all categories
            val usecase = DeleteCourseUseCase()
            usecase(DeleteCourseUseCase.Input(idCourse?.toInt())).collect() {
                call.respondRedirect("/admin/courses/?idCategory=" + idCategory)
            }
        }

    }
}


