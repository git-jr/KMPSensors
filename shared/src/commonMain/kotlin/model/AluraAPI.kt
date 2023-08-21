package model

import kotlinx.serialization.Serializable

@Serializable
data class AluraAPI(
    val courseProgresses: List<CourseProgresse>,
    val guides: List<Guide>,
    val id: Int
)