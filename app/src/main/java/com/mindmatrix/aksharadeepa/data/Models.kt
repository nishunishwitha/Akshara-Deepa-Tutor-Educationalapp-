package com.mindmatrix.aksharadeepa.data

data class Chapter(
    val id: String,
    val title: String,
    val subject: SubjectType
)

enum class SubjectType(val displayName: String) {
    SCIENCE("Science"),
    MATH("Maths"),
    SOCIAL_STUDIES("Social Studies")
}

data class Question(
    val id: String,
    val chapterId: String,
    val text: String,
    val options: List<String>,
    val correctAnswer: Int
)

data class UserProgress(
    val userName: String = "Student User",
    val completedChapters: Set<String> = emptySet(),
    val quizScores: Map<String, Int> = emptyMap(),
    val streak: Int = 0
)
