package com.example.presentation.routes.frontend

import com.example.domain.commons.extractParts
import com.example.domain.model.StringInLanguages
import com.example.domain.model.course.ChallengeModel
import com.example.domain.usecase.front.challenge.DeleteChallengeUseCase
import com.example.domain.usecase.front.challenge.GetChallengesUseCase
import com.example.domain.usecase.front.challenge.NewChallengeUseCase
import com.example.domain.usecase.front.challenge.UpdateChallengeUseCase
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.mustache.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.collect

fun Route.challengeRoute() {
    route("/challenges") {
        get("/") {
            val idCourse: String? = call.request?.queryParameters["idCourse"]
            if (idCourse != null) {
                val usecase = GetChallengesUseCase()
                usecase(GetChallengesUseCase.Input(idCourse = idCourse!!.toInt())).collect() {
                    call.respond(MustacheContent("challenges.hbs", mapOf("challenges" to it)))
                }
            } else {
                val usecase = GetChallengesUseCase()
                usecase(GetChallengesUseCase.Input()).collect() {
                    call.respond(MustacheContent("challenges.hbs", mapOf("challenges" to it)))
                }
            }
        }

        post("/createChallengeAction") {
            val post = call.extractParts()
            val nameEs: String? = (post["name_es"] as? PartData.FormItem)?.value
            val nameEn: String? = (post["name_en"] as? PartData.FormItem)?.value
            val idCourse: String? = (post["id_course"] as? PartData.FormItem)?.value
            val idChallenge: String? = if ((post["id_challenge"] as?
                        PartData.FormItem)?.value?.isEmpty() != true
            ) (post["id_challenge"] as?
                    PartData.FormItem)?.value else
                null
            val warningEs: String? = (post["warning_es"] as? PartData.FormItem)?.value
            val warningEn: String? = (post["warning_en"] as? PartData.FormItem)?.value
            val requiredChallengeId: String? = (post["requiredChallengeId"] as? PartData.FormItem)?.value

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

            val challenge = ChallengeModel(
                id = idChallenge?.toInt(),
                courseId = idCourse?.toInt()!!,
                name = StringInLanguages(nameEs, nameEn),
                description = StringInLanguages(
                    warningEs,
                    warningEn
                ),
                isDraft = null,
                imageName = imageItem?.originalFileName,
                videoEsName = videoES?.originalFileName,
                videoEnName = videoEN?.originalFileName,
                requiredChallenge = if (requiredChallengeId?.isEmpty()!!) null else ChallengeModel(id = requiredChallengeId?.toInt(), courseId = idCourse.toInt())
            )


            if (idChallenge?.isEmpty() == false) {
                val usecase = UpdateChallengeUseCase()
                usecase(UpdateChallengeUseCase.Input(challenge, fileItems)).collect() {
                    call.respondRedirect("/admin/challenges/?idCourse=" + idCourse)
                }
            } else {
                val usecase = NewChallengeUseCase()
                usecase(NewChallengeUseCase.Input(challenge, fileItems)).collect() {
                    call.respondRedirect("/admin/challenges/?idCourse=" + idCourse)
                }
            }
        }

        get("/deleteChallenge/") {
            val idCourse: String? = call.request?.queryParameters["idCourse"]
            val idChallenge: String? = call.request?.queryParameters["idChallenge"]
            //return all categories
            val usecase = DeleteChallengeUseCase()
            usecase(DeleteChallengeUseCase.Input(idChallenge?.toInt())).collect() {
                call.respondRedirect("/admin/challenges/?idCourse=" + idCourse)
            }
        }
    }
}
