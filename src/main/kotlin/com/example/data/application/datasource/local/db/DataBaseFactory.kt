package com.example.data.application.datasource.local.db

import com.example.data.entity.CommentsTable
import com.example.data.entity.LikesTable
import com.example.data.entity.UserTable
import com.example.data.entity.social.MultimediaPostTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

class DataBaseFactory(private val db: Database) {

    constructor(url: String, driver: String, user: String, password: String) : this(
        Database.connect(
            url = url,
            driver = driver,
            user = user,
            password = password
        )
    )

    fun init() {
        TransactionManager.defaultDatabase = db

        // Create used tables
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                CommentsTable,
                MultimediaPostTable,
                LikesTable,
                UserTable
            )
            addLogger(StdOutSqlLogger)
        }
    }
}
