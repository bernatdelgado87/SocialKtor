package com.example.domain.model.course

import com.example.domain.commons.aws.Bucket
import com.example.domain.model.StringInLanguages

const val CHALLENGE_FILES_URL_RELATIVE = "challenge/"

data class ChallengeModel(
    val id: Int? = null,
    val courseId: Int,
    val requiredChallenge: ChallengeModel? = null,
    val isDraft: Boolean? = null,
    val position: Int? = null,
    val name: StringInLanguages? = null,
    val description: StringInLanguages? = null,
    val instructions: StringInLanguages? = null,
    val imageName: String? = null,
    val imageUrlRelative: String? = if (imageName?.isEmpty() != true)CHALLENGE_FILES_URL_RELATIVE + imageName else null,
    val imageUrlAbsolute: String? = if (imageUrlRelative?.isEmpty() != true)Bucket.AWSUrl + imageUrlRelative else null,
    val videoEsName: String? = null,
    val videoEsUrlRelative: String? = if (imageName?.isEmpty() != true)CHALLENGE_FILES_URL_RELATIVE + videoEsName else null,
    val videoEsUrlAbsolute: String? = if (imageUrlRelative?.isEmpty() != true) Bucket.AWSUrl + videoEsUrlRelative else null,
    val videoEnName: String? = null,
    val videoEnUrlRelative: String? = if (imageName?.isEmpty() != true)CHALLENGE_FILES_URL_RELATIVE + videoEnName else null,
    val videoEnUrlAbsolute: String? = if (imageUrlRelative?.isEmpty() != true)Bucket.AWSUrl + videoEnUrlRelative else null,
)