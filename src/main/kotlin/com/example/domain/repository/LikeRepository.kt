package com.example.domain.repository

import com.example.domain.model.LikeModel
import kotlinx.coroutines.flow.Flow

interface LikeRepository {

    fun publishLike(userId: String, postId: Int): Flow<LikeModel>

}