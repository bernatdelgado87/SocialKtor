package com.example.domain.commons.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder

object Bucket{
    const val bucket = "mistercooperapp"
    const val amazonDomain = ".s3.eu-west-2.amazonaws.com/"
    const val AWSUrl = bucket + amazonDomain
}


class Aws3Client {
    val accessk = System.getProperty("accessk") ?: ""
    val secretk = System.getProperty("secretk") ?: ""


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