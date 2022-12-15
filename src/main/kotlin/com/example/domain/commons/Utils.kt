package com.example.domain.commons

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

class Utils {
    companion object {
        suspend fun getFileFromMultipart(part: PartData.FileItem): File =
        withContext(Dispatchers.IO)
        {
            lateinit var file: File
                // if part is a file (could be form item)
                if (part is PartData.FileItem) {
                    // retrieve file name of upload
                    val name = part.originalFileName!!
                    file = File("$name")

                    // use InputStream from part to save file
                    part.streamProvider().use { its ->
                        // copy the stream to the file with buffering
                        file.outputStream().buffered().use {
                            // note that this is blocking
                            its.copyTo(it)
                        }
                    }
                } else throw Exception("Is not a FileItem")
                part.dispose()
            return@withContext file
        }
    }
}

suspend fun ApplicationCall.extractParts(): Map<String, Any> {
    val multipart = receiveMultipart()
    lateinit var inputStream: InputStream

    val result = mutableMapOf<String, Any>()

    multipart.forEachPart { part ->
        // if part is a file (could be form item)
        result.put(part.name!!, part)
    }
    return result
}