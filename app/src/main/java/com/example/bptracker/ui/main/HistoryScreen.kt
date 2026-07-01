package com.example.bptracker.ui.main

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bptracker.AppContainer
import com.example.bptracker.data.PdfExporter
import com.example.bptracker.data.VitalRecord
import com.example.bptracker.data.WaterLog
import com.example.bptracker.theme.*
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(onBack: () -> Unit = {}, onAddRecord: (String) -> Unit = {}, initialFilter: String = "All") {
    val vitals by AppContainer.repository.vitals.collectAsStateWithLifecycle()
    val waterLogs by AppContainer.repository.waterLogs.collectAsStateWithLifecycle()
    val profiles by AppContainer.repository.profiles.collectAsStateWithLifecycle()
    val medications by AppContainer.repository.medications.collectAsStateWithLifecycle()
    val medLogs by AppContainer.repository.medicationLogs.collectAsStateWithLifecycle()
    val activeProfileId by AppContainer.repository.activeProfileId.collectAsStateWithLifecycle()
    val stepLogs by AppContainer.repository.stepLogs.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val profileVitals = vitals.filter { it.profileId == activeProfileId }
    val profileWater = waterLogs.filter { it.profileId == activeProfileId }
    val profileSteps = stepLogs.filter { it.profileId == activeProfileId }

    // Interactive Analytics Filters
    var selectedFilter by remember { mutableStateOf(if (initialFilter == "Water") "Hydration" else initialFilter) }
    val filters = listOf("All", "Blood Pressure", "Blood Sugar", "Heart Rate", "Hydration", "Daily Steps")

    val filteredVitals = profileVitals.filter { record ->
        when (selectedFilter) {
            "All" -> true
            "Blood Pressure" -> record.systolic > 0
            "Blood Sugar" -> record.bloodSugar > 0 || record.bloodSugarPP > 0
            "Heart Rate" -> record.heartRate > 0
            "Stressed" -> record.tags.contains("Stressed") || record.heartRateState == "Anxious"
            "Morning" -> {
                val cal = Calendar.getInstance()
                cal.timeInMillis = record.timestamp
                val hour = cal.get(Calendar.HOUR_OF_DAY)
                hour in 5..11
            }
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark)) {
                Text("Back")
            }
            Row {
                IconButton(onClick = { onAddRecord(selectedFilter) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Record", tint = PrimaryBlue)
                }
                Button(onClick = { 
                    val waterToExport = if (selectedFilter == "Hydration" || selectedFilter == "All") profileWater else emptyList()
                    val vitalsToExport = if (selectedFilter == "Hydration" || selectedFilter == "Daily Steps") emptyList() else filteredVitals
                    val title = if (selectedFilter == "All") "Complete_Report" else "${selectedFilter}_Report"
                    
                    val userName = profiles.find { it.id == activeProfileId }?.name ?: "Unknown"
                    val profileMeds = medications.filter { it.profileId == activeProfileId }
                    val profileMedLogs = medLogs.filter { it.profileId == activeProfileId }
                    
                    val file = PdfExporter.exportToPdf(
                        context, title, userName, vitalsToExport, waterToExport, profileMeds, profileMedLogs
                    )
                    if (file != null) {
                        Toast.makeText(context, "PDF saved to Downloads folder!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Error saving PDF", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Export")
                }
            }
        }

        Text(
            text = "History & Logs",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
        )
        
        // Filter Chips
        LazyRow(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = SurfaceDark,
                        selectedContainerColor = PrimaryBlue,
                        labelColor = TextSecondary,
                        selectedLabelColor = Color.White
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedFilter == filter,
                        borderColor = Color.Transparent
                    )
                )
            }
        }
        
        // Custom BP Line Chart (only shows if filter allows BP)
        val bpVitals = filteredVitals.filter { it.systolic > 0 }
        if ((bpVitals.isNotEmpty() || selectedFilter == "All" || selectedFilter == "Blood Pressure") && selectedFilter != "Hydration" && selectedFilter != "Daily Steps") {
            Card(
                modifier = Modifier.fillMaxWidth().height(220.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("BP Trend (Sys/Dia)", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (bpVitals.size < 2) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Add more logs to see trend", color = TextSecondary)
                        }
                    } else {
                        BpLineChart(vitals = bpVitals.take(10).reversed()) // Show last 10 entries chronologically
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        Text(
            text = "Logs",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
        )
        
        if (selectedFilter == "Hydration") {
            // Group water by day
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val waterByDay = profileWater.groupBy { sdf.format(Date(it.timestamp)) }
            
            if (waterByDay.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No water logs found.", color = TextSecondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(waterByDay.entries.toList().sortedByDescending { it.value.first().timestamp }) { entry ->
                        val dateStr = entry.key
                        val totalMl = entry.value.sumOf { it.amountMl }
                        
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Text(dateStr, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Daily Hydration", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.WaterDrop, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("$totalMl ml", color = PrimaryBlue, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        } else if (selectedFilter == "Daily Steps") {
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val stepsByDay = profileSteps.groupBy { sdf.format(Date(it.timestamp)) }
            
            if (stepsByDay.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No step logs found.", color = TextSecondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(stepsByDay.entries.toList().sortedByDescending { it.value.first().timestamp }) { entry ->
                        val dateStr = entry.key
                        val totalSteps = entry.value.sumOf { it.steps }
                        
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Text(dateStr, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Daily Steps", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.DirectionsWalk, contentDescription = null, tint = HighOrange, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("$totalSteps", color = HighOrange, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (filteredVitals.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No records found for this filter.", color = TextSecondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredVitals) { record ->
                        VitalHistoryCard(record)
                    }
                }
            }
        }
    }
}

@Composable
fun BpLineChart(vitals: List<VitalRecord>) {
    val systolicColor = HighOrange
    val diastolicColor = NormalGreen
    
    Canvas(modifier = Modifier.fillMaxSize().padding(top = 16.dp, bottom = 8.dp)) {
        val width = size.width
        val height = size.height
        
        val maxBp = 180f
        val minBp = 40f
        val range = maxBp - minBp
        
        val stepX = width / (if (vitals.size > 1) vitals.size - 1 else 1).toFloat()
        
        val sysPath = Path()
        val diaPath = Path()
        val sysFillPath = Path()
        val diaFillPath = Path()
        
        var prevSysX = 0f
        var prevSysY = 0f
        var prevDiaX = 0f
        var prevDiaY = 0f
        
        vitals.forEachIndexed { index, record ->
            val x = index * stepX
            val sysY = height - ((record.systolic.coerceIn(40, 180) - minBp) / range * height)
            val diaY = height - ((record.diastolic.coerceIn(40, 180) - minBp) / range * height)
            
            if (index == 0) {
                sysPath.moveTo(x, sysY)
                sysFillPath.moveTo(x, height)
                sysFillPath.lineTo(x, sysY)
                
                diaPath.moveTo(x, diaY)
                diaFillPath.moveTo(x, height)
                diaFillPath.lineTo(x, diaY)
            } else {
                val cx = (prevSysX + x) / 2
                
                sysPath.cubicTo(cx, prevSysY, cx, sysY, x, sysY)
                sysFillPath.cubicTo(cx, prevSysY, cx, sysY, x, sysY)
                
                diaPath.cubicTo(cx, prevDiaY, cx, diaY, x, diaY)
                diaFillPath.cubicTo(cx, prevDiaY, cx, diaY, x, diaY)
            }
            
            prevSysX = x
            prevSysY = sysY
            prevDiaX = x
            prevDiaY = diaY
        }
        
        if (vitals.isNotEmpty()) {
            sysFillPath.lineTo(width, height)
            sysFillPath.close()
            diaFillPath.lineTo(width, height)
            diaFillPath.close()
        }
        
        drawPath(
            path = sysFillPath,
            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                colors = listOf(systolicColor.copy(alpha = 0.5f), Color.Transparent),
                startY = 0f, endY = height
            )
        )
        drawPath(
            path = diaFillPath,
            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                colors = listOf(diastolicColor.copy(alpha = 0.5f), Color.Transparent),
                startY = 0f, endY = height
            )
        )
        
        drawPath(path = sysPath, color = systolicColor, style = Stroke(width = 6f))
        drawPath(path = diaPath, color = diastolicColor, style = Stroke(width = 6f))
        
        vitals.forEachIndexed { index, record ->
            val x = index * stepX
            val sysY = height - ((record.systolic.coerceIn(40, 180) - minBp) / range * height)
            val diaY = height - ((record.diastolic.coerceIn(40, 180) - minBp) / range * height)
            
            drawCircle(color = Color.White, radius = 10f, center = Offset(x, sysY))
            drawCircle(color = systolicColor, radius = 6f, center = Offset(x, sysY))
            
            drawCircle(color = Color.White, radius = 10f, center = Offset(x, diaY))
            drawCircle(color = diastolicColor, radius = 6f, center = Offset(x, diaY))
        }
    }
}

@Composable
fun VitalHistoryCard(record: VitalRecord) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
    val dateString = dateFormat.format(Date(record.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(dateString, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            
            if (record.systolic > 0 || record.diastolic > 0) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("BP: ${record.systolic}/${record.diastolic} mmHg", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(record.getBpStatus().first, color = Color(record.getBpStatus().second), style = MaterialTheme.typography.bodyMedium)
                }
                val bpContext = mutableListOf<String>()
                if (record.posture.isNotEmpty()) bpContext.add(record.posture)
                if (record.arm.isNotEmpty()) bpContext.add("${record.arm} Arm")
                if (record.tags.isNotEmpty()) bpContext.add(record.tags)
                if (bpContext.isNotEmpty()) {
                    Text(bpContext.joinToString(" • "), color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            if (record.heartRate > 0) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Heart Rate: ${record.heartRate} bpm", color = Color.White, fontWeight = FontWeight.Bold)
                    if (record.heartRateState.isNotEmpty()) {
                        Text(record.heartRateState, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (record.bloodSugar > 0 || record.bloodSugarPP > 0) {
                val sugarContextText = if (record.sugarContext.isNotEmpty()) record.sugarContext else if (record.bloodSugarPP > 0) "After Meal" else "General"
                val sugarValue = if (record.bloodSugar > 0) record.bloodSugar else record.bloodSugarPP
                val sugarStatus = if (sugarContextText == "After Meal") record.getBloodSugarPPStatus() else record.getBloodSugarStatus()
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Sugar: $sugarValue mg/dL", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(sugarStatus.first, color = Color(sugarStatus.second), style = MaterialTheme.typography.bodyMedium)
                }
                Text("Context: $sugarContextText", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                if (record.symptoms.isNotEmpty()) {
                    Text("Symptoms: ${record.symptoms}", color = HighOrange.copy(alpha=0.8f), style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (record.medications.isNotEmpty()) {
                androidx.compose.material3.Divider(color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(record.medications, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
