package com.example.domain.model.course

import com.example.domain.commons.aws.Bucket
import com.example.domain.model.StringInLanguages

const val EXERCISES_FILES_URL_RELATIVE = "exercise/"


data class ExerciseModel (
        val id: Int? = null,
        val challengeId: Int,
        val isDraft: Boolean? = null,
        val name: StringInLanguages? = null,
        val position: Int? = null,
        val description: StringInLanguages? = null,
        val imageName: String? = null,
        val imageUrlRelative: String? = if (imageName?.isEmpty() != true)EXERCISES_FILES_URL_RELATIVE + imageName else null,
        val imageUrlAbsolute: String? = if (imageUrlRelative?.isEmpty() != true)
                Bucket.AWSUrl + imageUrlRelative
                else null,
        val videoEsName: String? = null,
        val videoEsUrlRelative: String? = if (imageName?.isEmpty() != true)
                EXERCISES_FILES_URL_RELATIVE + videoEsName else null,
        val videoEsUrlAbsolute: String? = if(imageUrlRelative?.isEmpty() != true)
                Bucket.AWSUrl + videoEsUrlRelative
                else null,
        val videoEnName: String? = null,
        val videoEnUrlRelative: String? = if (imageName?.isEmpty() != true)
                EXERCISES_FILES_URL_RELATIVE + videoEsName else null,
        val videoEnUrlAbsolute: String? = if (imageUrlRelative?.isEmpty() != true)
                Bucket.AWSUrl + videoEsUrlRelative else null,
        )
