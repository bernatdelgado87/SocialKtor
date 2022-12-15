package com.example.domain.model.course

import com.example.domain.commons.aws.Bucket
import com.example.domain.model.StringInLanguages
import kotlinx.serialization.Serializable

@Serializable
data class CourseModel (
    val id: Int? = null,
    val category: CategoryModel? = null,
    val requiredCourse: CourseModel? = null,
    val isPopular: Boolean? = null,
    val isDraft: Boolean? = null,
    val isFree: Boolean? = null,
    val position: Int? = null,
    val name: StringInLanguages? = null,
    val warning: StringInLanguages? = null,
    val fileName: String? = null,
    val urlRelative: String? = if (fileName?.isEmpty() != true)"course/" + fileName else null,
    val urlAbsolute: String? = if (urlRelative?.isEmpty() != true) Bucket.AWSUrl + urlRelative else null,
    val videoHelpFileName: String? = null,
    val videoHelpurlRelative: String? = if (fileName?.isEmpty() != true)"course/" + videoHelpFileName else null,
    val videoHelpurlAbsolute: String? = if (urlRelative?.isEmpty() != true)Bucket.AWSUrl + videoHelpurlRelative else null,
    val videoHelpEnFileName: String? = null,
    val videoHelpEnurlRelative: String? = if (fileName?.isEmpty() != true)"course/" + videoHelpEnFileName else null,
    val videoHelpEnurlAbsolute: String? = if (urlRelative?.isEmpty() != true)Bucket.AWSUrl + videoHelpEnurlRelative else null,
)