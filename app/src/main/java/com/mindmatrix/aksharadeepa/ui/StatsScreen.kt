package com.mindmatrix.aksharadeepa.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindmatrix.aksharadeepa.data.AppData
import com.mindmatrix.aksharadeepa.data.SubjectType
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun StatsScreen(quizScores: Map<String, Int>) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Strength Map", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Focusing on core SSLC subjects", fontSize = 14.sp, color = Color.Gray)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Box(
            modifier = Modifier.fillMaxWidth().height(260.dp).background(Color.White, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            RadarChart(quizScores)
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Subject Performance", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.LightGray)
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SubjectType.values().forEach { subject ->
                val subjectChapters = AppData.chapters.filter { it.subject == subject }
                val avg = if (subjectChapters.isEmpty()) 0 else {
                    subjectChapters.sumOf { quizScores[it.id] ?: 0 } / subjectChapters.size
                }
                SubjectPerformanceCard(subject.displayName, avg)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = Color(0xFF4F46E5))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("${quizScores.size} Chapters Mastered", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Total knowledge acquired so far", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun SubjectPerformanceCard(name: String, percentage: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("$percentage%", fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color(0xFF4F46E5))
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = percentage / 100f,
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = Color(0xFF4F46E5),
                trackColor = Color.White
            )
        }
    }
}

@Composable
fun RadarChart(quizScores: Map<String, Int>) {
    val subjects = SubjectType.values()
    val scores = subjects.map { subject ->
        val subjectChapters = AppData.chapters.filter { it.subject == subject }
        val avg = if (subjectChapters.isEmpty()) 0f else {
            subjectChapters.sumOf { quizScores[it.id] ?: 0 }.toFloat() / subjectChapters.size
        }
        avg / 100f // Normalize 0..1
    }

    Canvas(modifier = Modifier.size(240.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 2
        val step = (2 * Math.PI / subjects.size).toFloat()

        // Draw circles/webs
        for (i in 1..4) {
            val r = radius * (i / 4f)
            drawCircle(Color.LightGray, radius = r, center = Offset(centerX, centerY), style = Stroke(1.dp.toPx()))
        }

        // Draw axes and points
        val path = Path()
        subjects.forEachIndexed { index, _ ->
            val angle = index * step - Math.PI.toFloat() / 2
            val x = centerX + radius * cos(angle)
            val y = centerY + radius * sin(angle)
            drawLine(Color.LightGray, Offset(centerX, centerY), Offset(x, y))

            val px = centerX + radius * scores[index] * cos(angle)
            val py = centerY + radius * scores[index] * sin(angle)
            if (index == 0) path.moveTo(px, py) else path.lineTo(px, py)
        }
        path.close()
        drawPath(path, color = Color(0x803B82F6))
        drawPath(path, color = Color(0xFF3B82F6), style = Stroke(2.dp.toPx()))
    }
}
