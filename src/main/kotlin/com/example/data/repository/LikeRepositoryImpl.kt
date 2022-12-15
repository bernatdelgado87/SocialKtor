package com.example.data.repository

import com.example.data.entity.LikesTable
import com.example.data.entity.social.MultimediaPostTable
import com.example.domain.model.LikeModel
import com.example.domain.repository.LikeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime


class LikeRepositoryImpl : LikeRepository {

    override fun publishLike(userIdParam: String, postIdParam: Int): Flow<LikeModel> {
        transaction {
            LikesTable.insert {
                it[userId] = userIdParam
                it[postId] = postIdParam
                it[createTime] = DateTime.now()
            }
            val totalLikes: Int = LikesTable.select { LikesTable.postId eq postIdParam }.count().toInt()

            MultimediaPostTable.update({ MultimediaPostTable.id eq postIdParam}) {
                it[numberOfLikes] = totalLikes
            }
        }

        return flowOf(
            LikeModel(userIdParam, postIdParam)
        )
    }


}


