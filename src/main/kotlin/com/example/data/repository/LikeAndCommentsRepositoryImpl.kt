package com.example.data.repository

import com.example.data.entity.*
import com.example.data.entity.social.MultimediaPostTable
import com.example.domain.model.CommentModel
import com.example.domain.model.CommentWrapperResponse
import com.example.domain.model.LikeModel
import com.example.domain.model.UserModel
import com.example.domain.repository.LikeAndCommentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime


class LikeAndCommentsRepositoryImpl : LikeAndCommentsRepository {

    override fun publishLike(userIdParam: Int, postIdParam: Int, like: Boolean): Flow<LikeModel> {
        transaction {
            if (like) {
                LikesTable.insert {
                    it[userId] = userIdParam
                    it[postId] = postIdParam
                    it[createTime] = DateTime.now()
                }
            } else {
                LikesTable.deleteWhere { (LikesTable.userId eq userIdParam) and (LikesTable.postId eq postIdParam) }
            }

            val totalLikes: Int = LikesTable.select { LikesTable.postId eq postIdParam }.count().toInt()

            MultimediaPostTable.update({ MultimediaPostTable.id eq postIdParam }) {
                it[numberOfLikes] = totalLikes
            }
        }

        return flowOf(
            LikeModel(userIdParam, postIdParam)
        )
    }

    override suspend fun publishComment(
        userId: Int,
        postId: Int,
        comment: String,
        commentReference: Int?
    ): Flow<CommentWrapperResponse> {
        transaction {
            CommentsTable.insert {
                it[CommentsTable.userId] = userId
                it[CommentsTable.postId] = postId
                it[CommentsTable.content] = comment
                it[CommentsTable.commentReference] = commentReference
                it[createTime] = DateTime.now()
            }
        }
        val comments = getComments(userId, postId, 20, 0)
            comments.collect { commentWrapper ->
                transaction {
                    MultimediaPostTable.update({ MultimediaPostTable.id eq postId }) {
                        it[MultimediaPostTable.numberOfComments] = commentWrapper.comments.size
                    }
                }
        }
        return comments
    }

    override fun getComments(user: Int, postId: Int, n: Int, offset: Long): Flow<CommentWrapperResponse> {
        val userImage = transaction {
            UserTable
                .select { UserTable.id eq user }
                .map { UserMapper.toModel(it) }
                .first()
                .profileImage
        }

        val comments = transaction {
            CommentsTable
                .leftJoin(UserTable)
                .select { CommentsTable.postId eq postId }
                .limit(n, offset)
                .map {
                    CommentsMapper.toModel(it)
                }
        }
        return flowOf(CommentWrapperResponse(userImage!!, comments))
    }
}


