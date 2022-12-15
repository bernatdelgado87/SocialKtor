package com.example.data.entity

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

object Questions : IntIdTable(name = "question"){

    val textQuestion = text("question_text")
    val textAnswer = text("answer_text")
    val createTime = datetime("create_time")

}