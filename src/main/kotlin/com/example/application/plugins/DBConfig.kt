package com.example.application.plugins

import com.example.data.application.datasource.local.db.DataBaseFactory
import io.ktor.application.*

fun Application.configureDatabase() {
    // Database Init
    val url = environment.config.property("ktor.database.url").getString()
    val driver = environment.config.property("ktor.database.driver").getString()
    val user = environment.config.property("ktor.database.user").getString()
    val password = environment.config.property("ktor.database.password").getString()
    val db = DataBaseFactory(url, driver, user, password)
    db.init()
}