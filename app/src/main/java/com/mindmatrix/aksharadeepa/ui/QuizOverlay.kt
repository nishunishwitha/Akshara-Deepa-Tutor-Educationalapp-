package com.mindmatrix.aksharadeepa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mindmatrix.aksharadeepa.data.Chapter
import com.mindmatrix.aksharadeepa.data.AppData
import com.mindmatrix.aksharadeepa.data.Question

@Composable
fun QuizOverlay(
    chapter: Chapter,
    onDismiss: () -> Unit,
    onComplete: (Int) -> Unit
) {
    val questions = remember { AppData.questions.filter { it.chapterId == chapter.id }.take(5) }
    var currentIndex by remember { mutableStateOf(0) }
    val selectedAnswers = remember { mutableStateMapOf<Int, Int>() }
    var showResults by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, null) }
                    Text("Quiz: ${currentIndex + 1}/${questions.size}", fontWeight = FontWeight.Bold)
                    Box(modifier = Modifier.size(48.dp))
                }

                if (!showResults) {
                    // Question area
                    val currentQuestion = questions[currentIndex]
                    Column(modifier = Modifier.weight(1f).padding(24.dp).fillMaxWidth()) {
                        Text(currentQuestion.text, fontSize = 20.sp, fontWeight = FontWeight.Bold, lineHeight = 28.sp)
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        currentQuestion.options.forEachIndexed { index, option ->
                            val isSelected = selectedAnswers[currentIndex] == index
                            Button(
                                onClick = { selectedAnswers[currentIndex] = index },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF9FAFB),
                                    contentColor = if (isSelected) Color(0xFF2563EB) else Color.Black
                                ),
                                elevation = ButtonDefaults.buttonElevation(0.dp)
                            ) {
                                Text(option, modifier = Modifier.fillMaxWidth().padding(8.dp), textAlign = TextAlign.Start)
                            }
                        }
                    }

                    // Footer
                    Button(
                        onClick = {
                            if (currentIndex < questions.size - 1) {
                                currentIndex++
                            } else {
                                showResults = true
                                val score = questions.indices.count { selectedAnswers[it] == questions[it].correctAnswer } * 20
                                onComplete(score)
                            }
                        },
                        enabled = selectedAnswers.containsKey(currentIndex),
                        modifier = Modifier.fillMaxWidth().padding(24.dp).height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(if (currentIndex == questions.size - 1) "Finish Quiz" else "Next Question")
                    }
                } else {
                    // Results View
                    val score = questions.indices.count { selectedAnswers[it] == questions[it].correctAnswer } * 20
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                        item {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Box(modifier = Modifier.size(80.dp).background(Color(0xFFEFF6FF), CircleShape), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2563EB), modifier = Modifier.size(40.dp))
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Quiz Complete!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                Text("${score}/100", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2563EB))
                                Spacer(modifier = Modifier.height(32.dp))
                            }
                        }
                        
                        itemsIndexed(questions) { idx, q ->
                            val isCorrect = selectedAnswers[idx] == q.correctAnswer
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("${idx+1}. ${q.text}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(
                                        "Your answer: ${if (selectedAnswers[idx] != null) q.options[selectedAnswers[idx]!!] else "N/A"}",
                                        color = if (isCorrect) Color(0xFF10B981) else Color(0xFFEF4444),
                                        fontSize = 12.sp
                                    )
                                    if (!isCorrect) {
                                        Text("Correct: ${q.options[q.correctAnswer]}", color = Color(0xFF10B981), fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                        
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                                Text("Back to Syllabus")
                            }
                        }
                    }
                }
            }
        }
    }
}
