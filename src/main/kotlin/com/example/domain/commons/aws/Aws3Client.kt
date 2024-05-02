package com.example.domain.commons.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import io.ktor.application.*

object Bucket{
    const val bucket = "mistercooperapp"
    const val amazonDomain = ".s3.eu-west-2.amazonaws.com/"
    const val AWSUrl = "https://" + bucket + amazonDomain
}


class Aws3Client(val application: Application) {
    val accessk = application.environment.config.property("ktor.awsw3.accessk").getString()
    val secretk = application.environment.config.property("ktor.awsw3.secretk").getString()

    val credentials: AWSCredentials = BasicAWSCredentials(
        accessk,
        secretk
    )

    val s3client by lazy {
        AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.EU_WEST_2)
            .build()
    }
}