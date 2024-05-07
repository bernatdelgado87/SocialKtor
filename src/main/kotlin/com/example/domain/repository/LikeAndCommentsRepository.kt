package com.example.domain.repository

import com.example.domain.model.LikeModel
import kotlinx.coroutines.flow.Flow

interface LikeAndCommentsRepository {

    fun publishLike(userId: Int, postId: Int): Flow<LikeModel>
    fun publishComment(userId: Int, postId: Int, comment: String): Flow<LikeModel>

}