package com.example.bptracker.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.bptracker.AppContainer
import com.example.bptracker.data.VitalRecord
import com.example.bptracker.theme.*
import java.util.Calendar

@Composable
fun AddHeartScreen(onSave: () -> Unit, onCancel: () -> Unit) {
    var heartRate by remember { mutableIntStateOf(70) }
    
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF25294A))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Graphic
        Icon(
            Icons.Default.Favorite, 
            contentDescription = null, 
            tint = HighOrange, 
            modifier = Modifier.size(80.dp).padding(top = 24.dp, bottom = 16.dp)
        )
        Text(
            text = "Record Heart Rate",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Wheel Picker for Pulse
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark.copy(alpha = 0.9f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Pulse (bpm)", color = TextSecondary, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                ComposeWheelPicker(value = heartRate, range = 40..200) { heartRate = it }
            }
        }

        Button(
            onClick = {
                val record = VitalRecord(
                    timestamp = Calendar.getInstance().timeInMillis,
                    systolic = 0,
                    diastolic = 0,
                    heartRate = heartRate,
                    bloodSugar = 0,
                    bloodSugarPP = 0
                )
                coroutineScope.launch {
                    AppContainer.repository.addRecord(record)
                    onSave()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text("Save Entry", style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = onCancel) {
            Text("Cancel", color = TextSecondary, style = MaterialTheme.typography.titleMedium)
        }
    }
}
