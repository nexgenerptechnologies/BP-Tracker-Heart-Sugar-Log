package com.example.bptracker.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
fun AddSugarScreen(onSave: () -> Unit, onCancel: () -> Unit) {
    var bloodSugar by remember { mutableIntStateOf(90) }
    
    // Context Fields
    var sugarContext by remember { mutableStateOf("Fasting") }
    var symptoms by remember { mutableStateOf(setOf<String>()) }
    
    val coroutineScope = rememberCoroutineScope()
    
    val vitals by AppContainer.repository.vitals.collectAsStateWithLifecycle()
    val recentSugarRecords = vitals.filter { it.bloodSugar > 0 }.take(3)

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
            painter = painterResource(id = R.drawable.ic_sugar), 
            contentDescription = null, 
            modifier = Modifier.size(80.dp).padding(top = 24.dp, bottom = 16.dp)
        )
        Text(
            text = "Record Blood Sugar",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Live Status Gauge
        val tempRecord = VitalRecord(bloodSugar = bloodSugar, bloodSugarPP = if(sugarContext == "After Meal") bloodSugar else 0)
        val status = if(sugarContext == "After Meal") tempRecord.getBloodSugarPPStatus() else tempRecord.getBloodSugarStatus()
        
        // Normalize for gauge (0 to 1). Let's say 40 is 0 and 300 is 1.
        val progress = ((bloodSugar - 40f) / 260f).coerceIn(0f, 1f)
        
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            com.example.bptracker.ui.components.CircularGauge(
                progress = progress,
                color = Color(status.second),
                modifier = Modifier.size(220.dp).padding(bottom = 16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$bloodSugar", color = Color.White, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                    Text("mg/dL", color = TextSecondary, style = MaterialTheme.typography.labelMedium)
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

        // Wheel Picker for Sugar
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark.copy(alpha = 0.9f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Glucose Level (mg/dL)", color = TextSecondary, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                ComposeWheelPicker(value = bloodSugar, range = 40..400) { bloodSugar = it }
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
                
                Text("When was this taken?", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                LazyRow(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf("Fasting", "Before Meal", "After Meal", "Before Sleep")) { c ->
                        ChoiceChip(text = c, selected = sugarContext == c, onSelect = { sugarContext = c })
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Text("Symptoms?", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                LazyRow(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf("Feeling Dizzy", "Excessive Thirst", "Fatigue", "Frequent Urination")) { s ->
                        ChoiceChip(text = s, selected = symptoms.contains(s), onSelect = { 
                            if (symptoms.contains(s)) symptoms -= s else symptoms += s 
                        })
                    }
                }
            }
        }

        // Action Buttons
        Spacer(modifier = Modifier.height(100.dp)) // Extra space to scroll past the bottom
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
                        bloodSugar = bloodSugar,
                        bloodSugarPP = if(sugarContext == "After Meal") bloodSugar else 0,
                        sugarContext = sugarContext,
                        symptoms = symptoms.joinToString(", ")
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
