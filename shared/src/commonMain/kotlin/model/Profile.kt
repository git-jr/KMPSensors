package model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val courseProgresses: List<CourseProgresse> = emptyList(),
    val guides: List<Guide> = emptyList(),
    val id: Int = 0,
)