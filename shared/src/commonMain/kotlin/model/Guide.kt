package model

import kotlinx.serialization.Serializable

@Serializable
data class Guide(
    val author: String,
    val code: String,
    val color: String,
    val finishedCourses: Int,
    val finishedSteps: Int,
    val id: Int,
    val kind: String,
    val lastAccessTime: Long,
    val name: String,
    val totalCourses: Int,
    val totalSteps: Int,
    val url: String
)