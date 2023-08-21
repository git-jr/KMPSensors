package model

import kotlinx.serialization.Serializable

@Serializable
data class CourseProgresse(
    val finished: Boolean,
    val id: Int,
    val lastAccessTime: Long,
    val name: String,
    val progress: Int,
    val readyToFinish: Boolean,
    val slug: String
)