package com.example.bptracker.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import com.example.bptracker.AppContainer
import com.example.bptracker.R
import com.example.bptracker.data.VitalRecord
import com.example.bptracker.theme.*
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddBpScreen(onSave: () -> Unit, onCancel: () -> Unit) {
    var systolic by remember { mutableIntStateOf(120) }
    var diastolic by remember { mutableIntStateOf(80) }
    
    // New Context Fields
    var posture by remember { mutableStateOf("Sitting") }
    var arm by remember { mutableStateOf("Left") }
    var tags by remember { mutableStateOf(setOf<String>()) }
    
    val coroutineScope = rememberCoroutineScope()
    
    val vitals by AppContainer.repository.vitals.collectAsStateWithLifecycle()
    val recentBpRecords = vitals.filter { it.systolic > 0 }.take(3)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF25294A))
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        // Header Graphic
        Image(
            painter = painterResource(id = R.drawable.ic_bp), 
            contentDescription = null, 
            modifier = Modifier.size(80.dp).padding(top = 24.dp, bottom = 16.dp)
        )
        Text(
            text = "Record Blood Pressure",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Live Status Gauge
        val tempRecord = VitalRecord(systolic = systolic, diastolic = diastolic)
        val status = tempRecord.getBpStatus()
        
        // Normalize BP for gauge (0 to 1). Let's say systolic 80 is 0 and 200 is 1.
        val progress = ((systolic - 80f) / 120f).coerceIn(0f, 1f)
        
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            com.example.bptracker.ui.components.CircularGauge(
                progress = progress,
                color = Color(status.second),
                modifier = Modifier.size(220.dp).padding(bottom = 16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$systolic/$diastolic", color = Color.White, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.background(Color(status.second).copy(alpha = 0.2f), RoundedCornerShape(8.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                        Text(
                            text = status.first.uppercase(),
                            color = Color(status.second),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Wheel Pickers for BP
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark.copy(alpha = 0.9f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text("Systolic", color = TextSecondary, style = MaterialTheme.typography.titleMedium)
                    Text("Diastolic", color = TextSecondary, style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    ComposeWheelPicker(value = systolic, range = 40..250) { systolic = it }
                    ComposeWheelPicker(value = diastolic, range = 40..150) { diastolic = it }
                }
            }
        }
        
        // Medical Context Section
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark.copy(alpha = 0.9f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Measurement Context", color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Body Posture", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                LazyRow(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf("Sitting", "Standing", "Lying Down")) { p ->
                        ChoiceChip(text = p, selected = posture == p, onSelect = { posture = p })
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Text("Which Arm?", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                Row(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ChoiceChip(text = "Left", selected = arm == "Left", onSelect = { arm = "Left" })
                    ChoiceChip(text = "Right", selected = arm == "Right", onSelect = { arm = "Right" })
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tags", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                LazyRow(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf("Stressed", "After Exercise", "Just Woke Up", "Felt Dizzy")) { t ->
                        ChoiceChip(text = t, selected = tags.contains(t), onSelect = { 
                            if (tags.contains(t)) tags -= t else tags += t 
                        })
                    }
                }
            }
        }

        // Action Buttons
        Spacer(modifier = Modifier.height(100.dp)) // Extra space to ensure we can scroll past the bottom
    }
    
    // Fixed Action Buttons at Bottom
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF25294A).copy(alpha = 0.95f))
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark)
            ) {
                Text("Cancel", style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
            Button(
                onClick = {
                    val record = VitalRecord(
                        timestamp = Calendar.getInstance().timeInMillis,
                        systolic = systolic,
                        diastolic = diastolic,
                        posture = posture,
                        arm = arm,
                        tags = tags.joinToString(", ")
                    )
                    coroutineScope.launch {
                        AppContainer.repository.addRecord(record)
                        onSave()
                    }
                },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Save", style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
        }
    }
}
}

@Composable
fun ChoiceChip(text: String, selected: Boolean, onSelect: () -> Unit) {
    Surface(
        color = if (selected) PrimaryBlue else Color(0xFF333856),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.clickable { onSelect() }
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
