package com.example.data.repository

import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.data.entity.LikeMapper
import com.example.data.entity.LikesTable
import com.example.data.entity.UserTable
import com.example.data.entity.mapper.PostDetailMapper
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
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.io.InputStream


class MultimediaPostRepositoryImpl : MultimediaPostRepository {

    override fun getAllPostsFromUser(userId: Int, start: Int, end: Long): Flow<MultimediaFeed> {
        val multimediaModelList = transaction {
            MultimediaPostTable
                .leftJoin(UserTable)
                .select { MultimediaPostTable.userRef eq userId }
                .limit(start, end)
                .map {
                    PostMapper.toModel(it)
                }.toList()
        }
        return flowOf(
            MultimediaFeed(
                multimediaModelList,
                true //fixme!! control pagination
            )
        )
    }

    override fun getDetailPost(idPost: Int): Flow<MultimediaModel> {
        //todo pending test
        val transaction = transaction {
            MultimediaPostTable
                .leftJoin(UserTable)
                .select { MultimediaPostTable.id eq idPost }
                .groupBy({ it [MultimediaPostTable.id] })
                .toList().first()
            }

        return flowOf(PostDetailMapper.toModel(transaction.second.first()))
    }

    override suspend fun getFeed(n: Int, offset: Long): Flow<MultimediaFeed> {
        val multimediaModelList = transaction {
            MultimediaPostTable
                .leftJoin(UserTable)
                .selectAll()
                .orderBy(MultimediaPostTable.createTime to SortOrder.DESC)
                .limit(n, offset)
                .map {
                    PostMapper.toModel(it)
                }.toList()
        }
        return flowOf(
            MultimediaFeed(
                multimediaModelList,
                true  //fixme!! control pagination
            )
        )
    }

    override fun publishMultimediaPost(
        userId: Int,
        inputStream: InputStream,
        extension: String,
        description: String,
        application: Application
    ): Flow<MultimediaModel> = runBlocking {
        withContext(Dispatchers.IO) {


            val nameInServer = "social/userId/" + getTimeMillis() + ".$extension"
            val initialState = "estado inicial"

            val result = Aws3Client(application).s3client.putObject(
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
                        it[MultimediaPostTable.userRef] = userId
                        it[numberOfLikes] = 0
                        it[numberOfComments] = 0
                        it[createTime] = DateTime.now()
                    }
                }
            }

            return@withContext flow {
                emit(
                    MultimediaModel(
                        relativeUrl = nameInServer,
                        description = description
                    )
                )
            }
        }
    }
}


