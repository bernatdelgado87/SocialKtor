package com.example.presentation.routes

import com.example.application.plugins.AuthType
import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.LikeRepositoryImpl
import com.example.data.repository.MultimediaPostRepositoryImpl
import com.example.domain.repository.MultimediaPostRepository
import com.example.domain.usecase.GetAllPostsFromUserUseCase
import com.example.domain.usecase.GetDetailPostUseCase
import com.example.domain.usecase.LikePostUseCase
import com.example.presentation.entity.LikeDTO
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.collect

fun Route.socialRoute() {

    authenticate(AuthType.USER_TYPE.name) {
        route("/social") {
            post("/upload") {
                val userLogged = call.principal<UserIdPrincipal>()!!.name

                //     val data = call.receive<MultimediaModel>()
                MultimediaPostRepositoryImpl().publishMultimediaPost(call).collect {
                    call.respond(it)
                }
            }

            get("/feed") {
                val userLogged = call.principal<UserIdPrincipal>()!!.name
                val LIMIT = 20
                val n: String? = call.request.queryParameters["n"]
                val offset: String? = call.request.queryParameters["offset"]

                val repository: MultimediaPostRepository = MultimediaPostRepositoryImpl()
                repository.getFeed(n?.toInt() ?: LIMIT, offset?.toLong() ?: 0).collect {
                    call.respond(it)
                }
            }

            get("/") {
                val userLogged = call.principal<UserIdPrincipal>()!!.name
                val postId: String? = call.request.queryParameters["postId"]
                val userId: String? = call.request.queryParameters["userId"]

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
                    val userLogged = call.principal<UserIdPrincipal>()!!.name
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
                    val userLogged = call.principal<UserIdPrincipal>()!!.name
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
}
