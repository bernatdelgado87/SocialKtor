package com.example.presentation.routes.frontend

import com.example.domain.commons.extractParts
import com.example.domain.model.StringInLanguages
import com.example.domain.model.course.ExerciseModel
import com.example.domain.usecase.front.exercise.DeleteExerciseUseCase
import com.example.domain.usecase.front.exercise.GetExercisesUseCase
import com.example.domain.usecase.front.exercise.NewExerciseUseCase
import com.example.domain.usecase.front.exercise.UpdateExerciseUseCase
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.mustache.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.collect

fun Route.exerciseRoute() {
        route("/exercises") {
            get("/") {
                val idChallenge: String? = call.request?.queryParameters["idChallenge"]
                if(idChallenge != null) {
                    val usecase = GetExercisesUseCase()
                    usecase(GetExercisesUseCase.Input(idChallenge = idChallenge.toInt())).collect() {
                        call.respond(MustacheContent("exercises.hbs", mapOf("exercises" to it)))
                    }
                } else {
                    val usecase = GetExercisesUseCase()
                    usecase(GetExercisesUseCase.Input()).collect() {
                        call.respond(MustacheContent("exercises.hbs", mapOf("exercises" to it)))
                    }
                }
            }

            post("/createExerciseAction") {
                val post = call.extractParts()
                val nameEs: String? = (post["name_es"] as? PartData.FormItem)?.value
                val nameEn: String? = (post["name_en"] as? PartData.FormItem)?.value
                val idChallenge: String? = (post["id_challenge"] as? PartData.FormItem)?.value
                val idExercise: String? = if ((post["id_exercise"] as?
                            PartData.FormItem)?.value?.isEmpty() != true
                ) (post["id_exercise"] as?
                        PartData.FormItem)?.value else
                    null
                val descriptionEs: String? = (post["description_es"] as? PartData.FormItem)?.value
                val descriptionEn: String? = (post["description_en"] as? PartData.FormItem)?.value

                val fileItems = mutableListOf<PartData.FileItem>()
                val imageItem: PartData.FileItem? =
                    if ((post["image1"] as? PartData.FileItem)?.originalFileName?.isEmpty() == true) null
                    else post["image1"] as? PartData.FileItem
                if (imageItem != null) fileItems.add(imageItem)

                val videoES: PartData.FileItem? =
                    if ((post["video1"] as? PartData.FileItem)?.originalFileName?.isEmpty() == true) null
                    else post["video1"] as? PartData.FileItem
                if (videoES != null) fileItems.add(videoES)
                val videoEN: PartData.FileItem? =
                    if ((post["video2"] as? PartData.FileItem)?.originalFileName?.isEmpty() == true) null
                    else post["video2"] as? PartData.FileItem
                if (videoEN != null) fileItems.add(videoEN)

                val Exercise = ExerciseModel(
                    id = idExercise?.toInt(),
                    challengeId = idChallenge?.toInt()!!,
                    name = StringInLanguages(nameEs, nameEn),
                    description = StringInLanguages(
                        descriptionEs,
                        descriptionEn
                    ),
                    isDraft = null,
                    imageName = imageItem?.originalFileName,
                    videoEsName = videoES?.originalFileName,
                )


                if (idExercise?.isEmpty() == false) {
                    val usecase = UpdateExerciseUseCase()
                    usecase(UpdateExerciseUseCase.Input(Exercise, fileItems)).collect() {
                        call.respondRedirect("/admin/exercises/?idChallenge=" + idChallenge)
                    }
                } else {
                    val usecase = NewExerciseUseCase()
                    usecase(NewExerciseUseCase.Input(Exercise, fileItems)).collect() {
                        call.respondRedirect("/admin/exercises/?idChallenge=" + idChallenge)
                    }
                }
            }

            get("/deleteExercise/") {
                val idChallenge: String? = call.request?.queryParameters["idChallenge"]
                val idExercise: String? = call.request?.queryParameters["idExercise"]
                //return all categories
                val usecase = DeleteExerciseUseCase()
                usecase(DeleteExerciseUseCase.Input(idExercise?.toInt())).collect() {
                    call.respondRedirect("/admin/Exercises/?idexercise=" + idChallenge)
                }
            }
        }
}
