package com.example.bptracker.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bptracker.AppContainer
import com.example.bptracker.data.Medication
import com.example.bptracker.data.MedicationLog
import com.example.bptracker.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationsScreen(
    activeProfileId: String,
    medications: List<Medication>,
    medicationLogs: List<MedicationLog>
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var medToEdit by remember { mutableStateOf<Medication?>(null) }
    val coroutineScope = rememberCoroutineScope()
    
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val todayStr = sdf.format(Date())
    
    val activeMeds = medications.filter { it.profileId == activeProfileId }
    
    // Calendar Week Generator
    val cal = Calendar.getInstance()
    val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
    cal.add(Calendar.DAY_OF_YEAR, -(dayOfWeek - 1))
    val daysOfWeek = (0..6).map { 
        val d = cal.time
        cal.add(Calendar.DAY_OF_YEAR, 1)
        d
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PrimaryBlue,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Medication", tint = Color.White)
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Pill Tracker", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 24.dp))

            // Premium Horizontal Calendar
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val todayDateStr = sdf.format(Date())
                daysOfWeek.forEach { date ->
                    val dateStr = sdf.format(date)
                    val isToday = dateStr == todayDateStr
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = SimpleDateFormat("EEE", Locale.getDefault()).format(date).uppercase(),
                            color = if (isToday) PrimaryBlue else TextSecondary,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isToday) PrimaryBlue else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = SimpleDateFormat("d", Locale.getDefault()).format(date),
                                color = if (isToday) Color.White else TextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            if (activeMeds.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(bottom = 64.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Medication, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No medications added yet.", color = TextSecondary)
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(bottom = 64.dp)) {
                    items(activeMeds) { med ->
                        val isTakenToday = medicationLogs.any { 
                            it.medicationId == med.id && sdf.format(Date(it.timestamp)) == todayStr 
                        }
                        
                        // Animated Color Background for Checkbox
                        val cardBgColor by animateColorAsState(
                            targetValue = if (isTakenToday) PrimaryBlue.copy(alpha = 0.1f) else Color(0xFF2A2E4D),
                            animationSpec = tween(300)
                        )
                        val iconBgColor by animateColorAsState(
                            targetValue = if (isTakenToday) PrimaryBlue else SurfaceDark,
                            animationSpec = tween(300)
                        )

                            var showMenu by remember { mutableStateOf(false) }
                            
                            Card(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    coroutineScope.launch { AppContainer.repository.toggleMedicationLog(med.id, activeProfileId) }
                                },
                                colors = CardDefaults.cardColors(containerColor = cardBgColor),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Box(
                                            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(iconBgColor),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Medication, contentDescription = null, tint = if (isTakenToday) Color.White else PrimaryBlue, modifier = Modifier.size(24.dp))
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column {
                                            Text(med.name, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                            Text("${med.dosage} • ${String.format("%02d:%02d", med.timeHour, med.timeMinute)}", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                                        }
                                    }
                                    
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier.size(28.dp).clip(CircleShape).background(if (isTakenToday) PrimaryBlue else Color.Transparent)
                                                .background(Color.Transparent, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isTakenToday) {
                                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                            } else {
                                                Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(Color.Transparent))
                                            }
                                        }
                                        
                                        Spacer(modifier = Modifier.width(8.dp))
                                        
                                        Box {
                                            IconButton(onClick = { showMenu = true }, modifier = Modifier.size(32.dp)) {
                                                Icon(Icons.Default.MoreVert, contentDescription = "More options", tint = TextSecondary)
                                            }
                                            DropdownMenu(
                                                expanded = showMenu,
                                                onDismissRequest = { showMenu = false },
                                                modifier = Modifier.background(SurfaceDark)
                                            ) {
                                                DropdownMenuItem(
                                                    text = { Text("Edit", color = Color.White) },
                                                    onClick = {
                                                        showMenu = false
                                                        medToEdit = med
                                                    }
                                                )
                                                DropdownMenuItem(
                                                    text = { Text("Delete", color = Color.Red) },
                                                    onClick = {
                                                        showMenu = false
                                                        coroutineScope.launch { AppContainer.repository.deleteMedication(med.id) }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                    }
                }
            }
        }
    }
    
    if (showAddDialog || medToEdit != null) {
        val isEditing = medToEdit != null
        var name by remember(medToEdit) { mutableStateOf(medToEdit?.name ?: "") }
        var dosage by remember(medToEdit) { mutableStateOf(medToEdit?.dosage ?: "") }
        var hour by remember(medToEdit) { mutableStateOf(medToEdit?.timeHour?.toString()?.padStart(2, '0') ?: "08") }
        var minute by remember(medToEdit) { mutableStateOf(medToEdit?.timeMinute?.toString()?.padStart(2, '0') ?: "00") }
        
        androidx.compose.ui.window.Dialog(onDismissRequest = { showAddDialog = false; medToEdit = null }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(if (isEditing) "Edit Medication" else "Add Medication", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    OutlinedTextField(
                        value = name, onValueChange = { name = it },
                        label = { Text("Name (e.g. Lisinopril)", color = TextSecondary) },
                        singleLine = true, modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = Color(0xFF333856), focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = dosage, onValueChange = { dosage = it },
                        label = { Text("Dosage (e.g. 10mg)", color = TextSecondary) },
                        singleLine = true, modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = Color(0xFF333856), focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = hour, onValueChange = { hour = it },
                            label = { Text("Hour (0-23)", color = TextSecondary) },
                            singleLine = true, modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = Color(0xFF333856), focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        OutlinedTextField(
                            value = minute, onValueChange = { minute = it },
                            label = { Text("Minute", color = TextSecondary) },
                            singleLine = true, modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = Color(0xFF333856), focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(
                            onClick = { showAddDialog = false; medToEdit = null },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333856))
                        ) { Text("Cancel", color = Color.White) }
                        
                        Button(
                            onClick = {
                                if (name.isNotBlank()) {
                                    coroutineScope.launch {
                                        if (isEditing && medToEdit != null) {
                                            AppContainer.repository.updateMedication(
                                                medToEdit!!.copy(
                                                    name = name,
                                                    dosage = dosage,
                                                    timeHour = hour.toIntOrNull() ?: 8,
                                                    timeMinute = minute.toIntOrNull() ?: 0
                                                )
                                            )
                                        } else {
                                            AppContainer.repository.addMedication(
                                                Medication(
                                                    profileId = activeProfileId,
                                                    name = name,
                                                    dosage = dosage,
                                                    timeHour = hour.toIntOrNull() ?: 8,
                                                    timeMinute = minute.toIntOrNull() ?: 0
                                                )
                                            )
                                        }
                                    }
                                    showAddDialog = false
                                    medToEdit = null
                                }
                            },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) { Text("Save", color = Color.White) }
                    }
                }
            }
        }
    }
}
