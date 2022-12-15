package com.example.domain.model.course

import com.example.domain.commons.aws.Bucket
import com.example.domain.model.StringInLanguages
import kotlinx.serialization.Serializable

@Serializable
data class CategoryModel(
    val id: Int? = null,
    val name: StringInLanguages? = null,
    val fileName: String? = null,
    val urlRelative: String? = if (fileName?.isEmpty() != true)"category/" + fileName else null,
    val urlAbsolute: String? = if (urlRelative?.isEmpty() != true) Bucket.AWSUrl + urlRelative else null,
    )