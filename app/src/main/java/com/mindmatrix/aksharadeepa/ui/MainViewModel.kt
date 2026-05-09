package com.mindmatrix.aksharadeepa.ui

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.mindmatrix.aksharadeepa.data.UserProgress
import com.mindmatrix.aksharadeepa.data.Chapter

class MainViewModel : ViewModel() {
    private val _progress = mutableStateOf(UserProgress())
    val progress: State<UserProgress> = _progress

    fun toggleChapter(chapterId: String) {
        val current = _progress.value.completedChapters
        val next = if (current.contains(chapterId)) current - chapterId else current + chapterId
        _progress.value = _progress.value.copy(completedChapters = next)
    }

    fun saveScore(chapterId: String, score: Int) {
        val currentScores = _progress.value.quizScores
        val bestScore = currentScores[chapterId]?.let { maxOf(it, score) } ?: score
        val currentStreak = _progress.value.streak
        
        // Simple streak logic: increment if score is good (e.g. > 40)
        val nextStreak = if (score >= 40) currentStreak + 1 else currentStreak

        _progress.value = _progress.value.copy(
            quizScores = currentScores + (chapterId to bestScore),
            streak = nextStreak
        )
    }

    fun updateUserName(newName: String) {
        _progress.value = _progress.value.copy(userName = newName)
    }

    fun resetProgress() {
        _progress.value = UserProgress()
    }
}
