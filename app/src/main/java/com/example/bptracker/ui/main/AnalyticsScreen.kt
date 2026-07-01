package com.example.bptracker.ui.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bptracker.data.VitalRecord
import com.example.bptracker.theme.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue

@Composable
fun AnalyticsScreen(vitals: List<VitalRecord>) {
    val activeProfileId by com.example.bptracker.AppContainer.repository.activeProfileId.collectAsStateWithLifecycle()
    val waterLogs by com.example.bptracker.AppContainer.repository.waterLogs.collectAsStateWithLifecycle()
    val profiles by com.example.bptracker.AppContainer.repository.profiles.collectAsStateWithLifecycle()
    val medications by com.example.bptracker.AppContainer.repository.medications.collectAsStateWithLifecycle()
    val medLogs by com.example.bptracker.AppContainer.repository.medicationLogs.collectAsStateWithLifecycle()
    
    val sysAvg = if (vitals.isNotEmpty()) vitals.map { it.systolic }.filter { it > 0 }.average().toInt() else 0
    val diaAvg = if (vitals.isNotEmpty()) vitals.map { it.diastolic }.filter { it > 0 }.average().toInt() else 0
    
    val highestBp = vitals.filter { it.systolic > 0 }.maxByOrNull { it.systolic }
    val lowestBp = vitals.filter { it.systolic > 0 }.minByOrNull { it.systolic }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = androidx.compose.ui.platform.LocalContext.current
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Health Analytics", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            IconButton(onClick = {
                val profileVitals = vitals.filter { it.profileId == activeProfileId }
                val profileWater = waterLogs.filter { it.profileId == activeProfileId }
                val userName = profiles.find { it.id == activeProfileId }?.name ?: "Unknown"
                val profileMeds = medications.filter { it.profileId == activeProfileId }
                val profileMedLogs = medLogs.filter { it.profileId == activeProfileId }
                val file = com.example.bptracker.data.PdfExporter.exportToPdf(
                    context, "Complete_Report", userName, profileVitals, profileWater, profileMeds, profileMedLogs
                )
                if (file != null) {
                    android.widget.Toast.makeText(context, "PDF saved to Downloads!", android.widget.Toast.LENGTH_LONG).show()
                } else {
                    android.widget.Toast.makeText(context, "Error saving PDF", android.widget.Toast.LENGTH_SHORT).show()
                }
            }) {
                Icon(Icons.Default.Download, contentDescription = "Export PDF", tint = PrimaryBlue)
            }
        }

        // Overall Health Score Gauge
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("WEEKLY HEALTH SCORE", color = PrimaryBlue, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(32.dp))
                
                // Calculate a mock score based on whether averages are in normal ranges
                var score = 100
                var deductionReason = "Your vitals are looking great this week!"
                if (sysAvg > 130 || diaAvg > 90) {
                    score -= 25
                    deductionReason = "Your average Blood Pressure is slightly elevated."
                } else if (sysAvg > 120 || diaAvg > 80) {
                    score -= 10
                    deductionReason = "Watch your Blood Pressure, it's above optimal."
                }
                
                com.example.bptracker.ui.components.CircularGauge(
                    progress = score / 100f,
                    color = if (score >= 90) NormalGreen else if (score >= 75) ElevatedYellow else HighOrange,
                    modifier = Modifier.size(200.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$score", color = Color.White, style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold)
                        Text("out of 100", color = TextSecondary, style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(if (score >= 90) "Excellent" else if (score >= 75) "Fair" else "Needs Attention", color = if (score >= 90) NormalGreen else if (score >= 75) ElevatedYellow else HighOrange, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Text(deductionReason, color = TextSecondary, style = MaterialTheme.typography.bodyMedium, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        }

        // Vitals Overview Donut
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 100.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("LOGGING CONSISTENCY", color = PrimaryBlue, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Text("Which metrics are you tracking most?", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(24.dp))
                
                val bpCount = vitals.count { it.systolic > 0 }.toFloat()
                val hrCount = vitals.count { it.heartRate > 0 }.toFloat()
                val sugarCount = vitals.count { it.bloodSugar > 0 }.toFloat()
                val totalCount = bpCount + hrCount + sugarCount
                
                if (totalCount == 0f) {
                    Text("No measurements recorded yet.", color = TextSecondary)
                } else {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        com.example.bptracker.ui.components.DonutChart(
                            data = listOf(bpCount, hrCount, sugarCount),
                            colors = listOf(PrimaryBlue, HighOrange, NormalGreen),
                            totalText = "${totalCount.toInt()} Logs",
                            modifier = Modifier.size(180.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Legend
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        LegendItem("BP", PrimaryBlue, "${((bpCount/totalCount)*100).toInt()}%")
                        LegendItem("Heart Rate", HighOrange, "${((hrCount/totalCount)*100).toInt()}%")
                        LegendItem("Blood Sugar", NormalGreen, "${((sugarCount/totalCount)*100).toInt()}%")
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color, countText: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(12.dp).background(color, RoundedCornerShape(6.dp)))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = TextSecondary, style = MaterialTheme.typography.labelSmall)
        Text(countText, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}
