package com.mindmatrix.aksharadeepa.data

object AppData {
    val chapters: List<Chapter> = listOf(
        // Science
        Chapter("sci-1", "Chemical Reactions and Equations", SubjectType.SCIENCE),
        Chapter("sci-2", "Acids, Bases and Salts", SubjectType.SCIENCE),
        Chapter("sci-3", "Metals and Non-metals", SubjectType.SCIENCE),
        Chapter("sci-4", "Carbon and its Compounds", SubjectType.SCIENCE),
        Chapter("sci-5", "Life Processes", SubjectType.SCIENCE),
        
        // Math
        Chapter("math-1", "Arithmetic Progressions", SubjectType.MATH),
        Chapter("math-2", "Triangles", SubjectType.MATH),
        Chapter("math-3", "Pair of Linear Equations in Two Variables", SubjectType.MATH),
        Chapter("math-4", "Quadratic Equations", SubjectType.MATH),
        Chapter("math-5", "Introduction to Trigonometry", SubjectType.MATH),

        // Social Studies
        Chapter("soc-1", "Advent of Europeans to India", SubjectType.SOCIAL_STUDIES),
        Chapter("soc-2", "The Extension of British Rule", SubjectType.SOCIAL_STUDIES),
        Chapter("soc-3", "Impact of British Rule in India", SubjectType.SOCIAL_STUDIES),
        Chapter("soc-4", "Indian Challenge to British Rule", SubjectType.SOCIAL_STUDIES),
        Chapter("soc-5", "Freedom Movement", SubjectType.SOCIAL_STUDIES)
    )

    val questions: List<Question> = run {
        val list = mutableListOf<Question>()
        // Sample questions for initial chapters
        list.add(Question("q1", "sci-1", "Which of the following is a physical change?", listOf("Rusting of iron", "Melting of ice", "Burning of wood", "Cooking of food"), 1))
        list.add(Question("q2", "sci-1", "Chemical name of slaked lime?", listOf("Calcium carbonate", "Calcium oxide", "Calcium hydroxide", "Calcium chloride"), 2))
        
        // Generate bulk mock data
        chapters.forEach { chapter ->
            (1..5).forEach { i ->
                val qId = "${chapter.id}-q$i"
                if (list.none { it.id == qId }) {
                   list.add(Question(
                       id = qId,
                       chapterId = chapter.id,
                       text = "Mock Question $i for ${chapter.title}: What is the primary concept?",
                       options = listOf("Concept A", "Concept B", "Concept C", "Concept D"),
                       correctAnswer = (0..3).random()
                   ))
                }
            }
        }
        list
    }
}
