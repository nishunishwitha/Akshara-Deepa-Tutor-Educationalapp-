package com.mindmatrix.aksharadeepa.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    userName: String,
    onNameChange: (String) -> Unit,
    onReset: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Profile Settings", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(userName.take(1).uppercase(), fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFF3B82F6))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(userName, fontWeight = FontWeight.Bold)
                    Text("Grade 10 • SSLC", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Personal Details", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = userName,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Your Name") },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF2F2)),
            contentPadding = PaddingValues(16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Refresh, null, tint = Color(0xFFDC2626))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reset All Progress", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
        }
    }
}
