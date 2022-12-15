package com.example.data.repository

import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.domain.commons.aws.Aws3Client
import com.example.domain.commons.aws.Bucket
import io.ktor.util.date.*
import kotlinx.coroutines.runBlocking
import java.io.InputStream

class FileUploader {
    companion object {
        suspend fun uploadFileToAWS(
            path: String = "noName/" + getTimeMillis().toString(),
            inputStream: InputStream
        ): Boolean = runBlocking {

            val initialState = "estado inicial"

            val result = Aws3Client().s3client.putObject(
                PutObjectRequest(
                    Bucket.bucket,
                    path,
                    inputStream,
                    ObjectMetadata()
                ).withCannedAcl(CannedAccessControlList.PublicRead)
            )
            return@runBlocking result != null
        }
    }
}