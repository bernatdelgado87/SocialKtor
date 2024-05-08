package com.example.presentation.routes

import com.example.application.plugins.AuthType
import com.example.data.repository.LikeAndCommentsRepositoryImpl
import com.example.data.repository.MultimediaPostRepositoryImpl
import com.example.domain.repository.MultimediaPostRepository
import com.example.presentation.entity.CommentDTO
import com.example.presentation.entity.LikeDTO
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.collect
import java.io.InputStream

fun Route.socialRoute() {

    authenticate(AuthType.USER_TYPE.name) {
        route("/social") {
            post("/upload") {
                val multipart = call.receiveMultipart()
                val user = call.principal<UserIdPrincipal>()!!.name

                lateinit var inputStream: InputStream
                var description = ""
                var extension = ""

                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        inputStream = part.streamProvider()
                        extension = part.originalFileName?.substringAfter(".").orEmpty()
                    }

                    if (part is PartData.FormItem) {
                        if (part.name.equals("description")) {
                            description = part.value
                        }
                    }
                }

                MultimediaPostRepositoryImpl().publishMultimediaPost(
                    user.toInt(),
                    inputStream,
                    extension,
                    description,
                    call.application
                ).collect {
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

            get("/detail") {
                val postId: String? = call.request.queryParameters["postId"]
                val userIdRequested: String? = call.request.queryParameters["userId"]
                val n = call.request.queryParameters["n"]
                val offset = call.request.queryParameters["offset"]
                val user = call.principal<UserIdPrincipal>()!!.name

                if (postId != null) {
                    LikeAndCommentsRepositoryImpl().getComments(user.toInt(), postId.toInt(), n!!.toInt(), offset!!.toLong()).collect() {
                        call.respond(it)
                    }
                } else if (userIdRequested != null) {
                    MultimediaPostRepositoryImpl().getAllPostsFromUser(userIdRequested.toInt()).collect() {
                        call.respond(it)
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Not postId or userId sent")
                }

            }

            post("/like") {
                val userLogged = call.principal<UserIdPrincipal>()!!.name
                val data = call.receive<LikeDTO>()

                val postId = data.postReference

                LikeAndCommentsRepositoryImpl().publishLike(userLogged.toInt(), postId).collect() {
                    call.respond(it)
                }
            }

            post("/comment") {
                val userLogged = call.principal<UserIdPrincipal>()!!.name
                val data = call.receive<CommentDTO>()

                val postId = data.postReference
                val comment = data.text

                LikeAndCommentsRepositoryImpl().publishComment(userLogged.toInt(), postId, comment).collect() {
                    call.respond(it)
                }
            }

            route("/follow") {
                post("/") {
                    call.respond("Pending implementation")
                }
            }
        }
    }
}
