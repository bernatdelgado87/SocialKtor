package com.example.domain.repository

import com.example.domain.model.MultimediaFeed
import com.example.domain.model.MultimediaModel
import io.ktor.application.*
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface MultimediaPostRepository {

    fun getAllPostsFromUser(userId: Int, start: Int = 0, end: Long = 20): Flow<MultimediaFeed>

    fun getDetailPost(idPost: Int): Flow<MultimediaModel>

    fun publishMultimediaPost(
        userId: Int,
        inputStream: InputStream,
        extension: String,
        description: String,
        application: Application
    ): Flow<MultimediaModel>

    suspend fun getFeed(n: Int, offset: Long): Flow<MultimediaFeed>

}
