package com.example.data.application.datasource.remote

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*

class KtorClient {
    val client = HttpClient(CIO) {
        expectSuccess = false
        install(JsonFeature) {
            serializer = GsonSerializer() {
                setPrettyPrinting()
                disableHtmlEscaping()
            }
        }
    }
}