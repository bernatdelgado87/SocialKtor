package com.example.presentation.routes

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.LikeRepositoryImpl
import com.example.data.repository.MultimediaPostRepositoryImpl
import com.example.domain.usecase.GetAllPostsFromUserUseCase
import com.example.domain.usecase.GetDetailPostUseCase
import com.example.domain.usecase.LikePostUseCase
import com.example.domain.usecase.UploadUseCase
import com.example.presentation.entity.LikeDTO
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.collect

fun Route.socialRoute() {
    /* TODO this is for authentication
    authenticate(SocialAuth.AUTH_JWT_TOKEN) {

        route("/social") {
            get("/a") {
                loginUseCase(LoginUseCase.Input(call)).collect() {
                    call.respond("helloWorld")
                }
            }
        }
    }*/

    route("/social") {
        post("upload/") {
            //     val data = call.receive<MultimediaModel>()
            val uploadUseCase =
                UploadUseCase(
                    MultimediaPostRepositoryImpl(),
                    JobCoroutine()
                )
            uploadUseCase(UploadUseCase.Input(call)).collect() {
                call.respond(it)
            }
        }

        get("/") {
            val postId: String? = call.request.queryParameters["postId"]
            val userId: String?  = call.request.queryParameters["userId"]

            if (postId != null) {
                val getDetailPostUseCase =
                    GetDetailPostUseCase()
                getDetailPostUseCase(GetDetailPostUseCase.Input(postId.toInt())).collect() {
                    call.respond(it)
                }
            } else if (userId != null) {
                val getAllPostsFromUserUseCase =
                    GetAllPostsFromUserUseCase()
                getAllPostsFromUserUseCase(GetAllPostsFromUserUseCase.Input(userId)).collect() {
                    call.respond(it)
                }
            } else {

            }

        }

        route("/like") {
            post("/") {
                val likePostUseCase =
                    LikePostUseCase(
                        LikeRepositoryImpl(),
                        JobCoroutine()
                    )
                val data = call.receive<LikeDTO>()

                val userId = data.userId
                val postId = data.postReference

                likePostUseCase(LikePostUseCase.Input(userId, postId)).collect() {
                    call.respond(it)
                }
            }
        }

        route("/follow") {
            post("/") {
                val likePostUseCase =
                    LikePostUseCase(
                        LikeRepositoryImpl(),
                        JobCoroutine()
                    )
                val data = call.receive<LikeDTO>()

                val userId = data.userId
                val postId = data.postReference

                likePostUseCase(LikePostUseCase.Input(userId, postId)).collect() {
                    call.respond(it)
                }
            }
        }

    }

}
