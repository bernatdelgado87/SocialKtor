package com.example.data.entity.mapper

import com.example.data.entity.social.MultimediaPostTable
import com.example.domain.model.MultimediaModel
import org.jetbrains.exposed.sql.ResultRow


class PostMapper(){
    companion object {
        fun toModel(row: ResultRow): MultimediaModel = MultimediaModel(
            id = row[MultimediaPostTable.id].value,
            relativeUrl = row[MultimediaPostTable.url],
            description = row[MultimediaPostTable.description],
            userId = row[MultimediaPostTable.userRef],
            numberOfLikes = row[MultimediaPostTable.numberOfLikes]
        )
    }
}