package com.mindmatrix.aksharadeepa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(completedCount: Int, streak: Int, userName: String) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF3B82F6), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.MenuBook, null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text("AksharaDeepa", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Namaste, ${userName.split(" ").firstOrNull() ?: ""}!", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text("Ready for today's mission?", color = Color.Gray)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        DailyGoalCard(completedCount)
        
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("🔥", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("$streak Day Streak!", fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                    Text("Keep learning to grow your streak!", fontSize = 12.sp, color = Color(0xFF92400E))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, null, tint = Color(0xFF3B82F6))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Complete 3 topics this week to unlock your mid-term streak!", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun DailyGoalCard(count: Int) {
    Card(
        modifier = Modifier.fillMaxWidth().height(160.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3B82F6)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Daily Goal", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            Text("Complete 1 Chapter", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            
            Spacer(modifier = Modifier.weight(1f))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = if (count > 0) 1f else 0f,
                    modifier = Modifier.weight(1f).height(8.dp),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(if (count > 0) "100%" else "0%", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}
