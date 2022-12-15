package com.example.data.repository

import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.data.entity.LikeMapper
import com.example.data.entity.LikesTable
import com.example.data.entity.mapper.PostMapper
import com.example.data.entity.social.MultimediaPostTable
import com.example.domain.commons.aws.Aws3Client
import com.example.domain.commons.aws.Bucket
import com.example.domain.model.LikeSimplifiedModel
import com.example.domain.model.MultimediaFeed
import com.example.domain.model.MultimediaModel
import com.example.domain.repository.MultimediaPostRepository
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.util.date.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.io.InputStream


class MultimediaPostRepositoryImpl : MultimediaPostRepository {

    override fun getAllPostsFromUser(userId: Int, start: Int, end: Long): Flow<MultimediaFeed> {
        val multimediaModelList = transaction {
            MultimediaPostTable
                .select { MultimediaPostTable.userRef eq userId }
                .limit(start, end)
                .map {
                    PostMapper.toModel(it)
                }.toList()
        }
        return flowOf(
            MultimediaFeed(
                multimediaModelList,
                start,
                end
            )
        )
    }

    override fun getDetailPost(idPost: Int): Flow<MultimediaModel> {
        var multimediaModel: MultimediaModel?
        val likeList = mutableListOf<LikeSimplifiedModel>()

        val likesResult = transaction {
            MultimediaPostTable
                .leftJoin(LikesTable)
                .select { MultimediaPostTable.id eq idPost }
                .map {
                    LikeMapper.toModel(it)
                }.toList()
        }
        if (likesResult.isEmpty()) {
            return flowOf(MultimediaModel())
        }
        multimediaModel = likesResult.get(0).multimediaModel
        for (item in likesResult) {
            likeList.add(LikeSimplifiedModel(item.userId, item.postReference))
        }


        val result = if (likeList != null && !likeList.isEmpty() && multimediaModel != null) {
            MultimediaModel(
                userId = multimediaModel!!.userId,
                description = multimediaModel!!.description,
                relativeUrl = multimediaModel!!.relativeUrl,
                numberOfLikes = multimediaModel!!.numberOfLikes,
                likes = likeList
            )
        } else {
            MultimediaModel()
        }

        return flowOf(result)
    }

    override fun publishMultimediaPost(applicationCall: ApplicationCall): Flow<MultimediaModel> =
        publishPost(applicationCall)

    override fun getFeed(idUser: String): Flow<MultimediaFeed> {
        TODO("Not yet implemented")
    }

    private fun publishPost(applicationCall: ApplicationCall): Flow<MultimediaModel> = runBlocking {
        withContext(Dispatchers.IO) {
            val multipart = applicationCall.receiveMultipart()
            lateinit var inputStream: InputStream
            var userId: Int = -1
            var description = ""

            multipart.forEachPart { part ->
                // if part is a file (could be form item)
                if (part is PartData.FileItem) {
                    inputStream = part.streamProvider()
                    //file = getFileFromMultipart(part)
                }
                if (part is PartData.FormItem) {
                    if (part.name.equals("userId")) {
                        userId = part.value.toInt()
                    } else if (part.name.equals("description")) {
                        description = part.value
                    }
                }
            }

            val nameInServer = "social/userId/" + getTimeMillis() + ".mp4"
            val initialState = "estado inicial"

            val result = Aws3Client().s3client.putObject(
                PutObjectRequest(
                    Bucket.bucket,
                    nameInServer,
                    inputStream,
                    ObjectMetadata()
                ).withCannedAcl(CannedAccessControlList.PublicRead)
            )

            if (result != null) {
                transaction {
                    MultimediaPostTable.insert {
                        it[url] = nameInServer
                        it[state] = initialState
                        it[MultimediaPostTable.description] = description
                        it[MultimediaPostTable.userRef] = userId!!
                        it[numberOfLikes] = 0
                        it[createTime] = DateTime.now()
                    }
                }
            }

            return@withContext flow {
                emit(
                    MultimediaModel(
                        relativeUrl = nameInServer,
                        description = description,
                        userId = userId
                    )
                )
            }
        }
    }
}


