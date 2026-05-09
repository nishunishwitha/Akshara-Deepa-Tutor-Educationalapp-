package com.mindmatrix.aksharadeepa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindmatrix.aksharadeepa.data.Chapter
import com.mindmatrix.aksharadeepa.data.AppData
import com.mindmatrix.aksharadeepa.data.SubjectType

@Composable
fun SyllabusScreen(
    completedChapters: Set<String>,
    onToggleChapter: (String) -> Unit,
    onStartQuiz: (Chapter) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Syllabus Tracker", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val subjects = SubjectType.values()
            items(subjects) { subject ->
                SubjectCard(
                    subject = subject,
                    allChapters = AppData.chapters.filter { it.subject == subject },
                    completedIds = completedChapters,
                    onToggle = onToggleChapter,
                    onQuiz = onStartQuiz
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectCard(
    subject: SubjectType,
    allChapters: List<Chapter>,
    completedIds: Set<String>,
    onToggle: (String) -> Unit,
    onQuiz: (Chapter) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val progress = allChapters.count { completedIds.contains(it.id) }.toFloat() / allChapters.size

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(subject.displayName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxWidth().height(4.dp).padding(top = 8.dp),
                        color = Color(0xFF3B82F6)
                    )
                }
                Icon(if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null)
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                allChapters.forEach { chapter ->
                    val isDone = completedIds.contains(chapter.id)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onToggle(chapter.id) }) {
                            Icon(
                                if (isDone) Icons.Default.CheckCircle else Icons.Default.AddCircle,
                                contentDescription = null,
                                tint = if (isDone) Color(0xFF10B981) else Color.LightGray
                            )
                        }
                        Text(
                            chapter.title,
                            modifier = Modifier.weight(1f),
                            fontSize = 14.sp,
                            color = if (isDone) Color.Gray else Color.Black
                        )
                        IconButton(onClick = { onQuiz(chapter) }) {
                            Icon(Icons.Default.PlayArrow, null, tint = Color(0xFF3B82F6))
                        }
                    }
                }
            }
        }
    }
}
