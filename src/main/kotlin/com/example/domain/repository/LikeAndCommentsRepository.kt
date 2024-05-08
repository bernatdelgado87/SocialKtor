package com.example.domain.repository

import com.example.domain.model.CommentModel
import com.example.domain.model.CommentWrapperResponse
import com.example.domain.model.LikeModel
import kotlinx.coroutines.flow.Flow

interface LikeAndCommentsRepository {

    fun publishLike(userId: Int, postId: Int): Flow<LikeModel>
    suspend fun publishComment(userId: Int, postId: Int, comment: String, commentReference: Int? = null): Flow<CommentWrapperResponse>

    fun getComments(userId: Int, postId: Int, n: Int, offset: Long): Flow<CommentWrapperResponse>
}