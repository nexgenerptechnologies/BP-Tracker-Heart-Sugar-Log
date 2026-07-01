package com.example.bptracker.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.app.Activity
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.bptracker.R
import com.example.bptracker.theme.*
import com.example.bptracker.utils.ReminderManager
import java.util.Calendar

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = androidx.compose.ui.res.stringResource(R.string.settings_title),
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = androidx.compose.ui.res.stringResource(R.string.settings_reminders),
            color = PrimaryBlue,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2E4D)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column {
                ReminderItem(
                    id = 1,
                    title = androidx.compose.ui.res.stringResource(R.string.card_bp),
                    initialTime = "08:00 AM",
                    iconRes = R.drawable.ic_bp
                )
                Divider(color = Color(0xFF1E213A), thickness = 1.dp)
                ReminderItem(
                    id = 2,
                    title = androidx.compose.ui.res.stringResource(R.string.card_sugar),
                    initialTime = "07:30 AM",
                    iconRes = R.drawable.ic_sugar
                )
                Divider(color = Color(0xFF1E213A), thickness = 1.dp)
                ReminderItem(
                    id = 3,
                    title = androidx.compose.ui.res.stringResource(R.string.card_heart),
                    initialTime = "",
                    iconRes = R.drawable.ic_heart
                )
                Divider(color = Color(0xFF1E213A), thickness = 1.dp)
                ReminderItem(
                    id = 4,
                    title = androidx.compose.ui.res.stringResource(R.string.card_medications),
                    initialTime = "09:00 AM",
                    isIconVector = true
                )
            }
        }

        Text(
            text = androidx.compose.ui.res.stringResource(R.string.settings_general),
            color = PrimaryBlue,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2E4D)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column {
                LanguagePickerRow()
                Divider(color = Color(0xFF1E213A), thickness = 1.dp)
                
                var stickyEnabled by remember { mutableStateOf(context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE).getBoolean("sticky_notif", false)) }
                SettingsRow(title = "Quick Log Notification (Sticky)", value = if (stickyEnabled) "On" else "Off", onClick = {
                    stickyEnabled = !stickyEnabled
                    context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE).edit().putBoolean("sticky_notif", stickyEnabled).apply()
                    val intent = android.content.Intent(context, com.example.bptracker.ui.main.ReminderReceiver::class.java).apply {
                        putExtra("TITLE", "Quick Log Vitals")
                        putExtra("IS_STICKY", true)
                        putExtra("NOTIFICATION_ID", 999)
                    }
                    if (stickyEnabled) {
                        context.sendBroadcast(intent)
                    } else {
                        val nm = context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
                        nm.cancel(999)
                    }
                })
                Divider(color = Color(0xFF1E213A), thickness = 1.dp)
                
                // Export Data directly invokes PDF creation for everything
                val vitals by com.example.bptracker.AppContainer.repository.vitals.collectAsState(initial = emptyList())
                val waterLogs by com.example.bptracker.AppContainer.repository.waterLogs.collectAsState(initial = emptyList())
                val activeProfileId by com.example.bptracker.AppContainer.repository.activeProfileId.collectAsState(initial = 1)
                val profiles by com.example.bptracker.AppContainer.repository.profiles.collectAsState(initial = emptyList())
                val medications by com.example.bptracker.AppContainer.repository.medications.collectAsState(initial = emptyList())
                val medLogs by com.example.bptracker.AppContainer.repository.medicationLogs.collectAsState(initial = emptyList())
                
                SettingsRow(title = androidx.compose.ui.res.stringResource(R.string.settings_export_data), value = "", onClick = {
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
                })
            }
        }

        Text(
            text = androidx.compose.ui.res.stringResource(R.string.settings_about),
            color = PrimaryBlue,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2E4D)),
            shape = RoundedCornerShape(16.dp)
        ) {
                SettingsRow(title = androidx.compose.ui.res.stringResource(R.string.settings_rate), value = "", icon = androidx.compose.material.icons.Icons.Default.Star, onClick = {
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("market://details?id=${context.packageName}"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}"))
                        context.startActivity(intent)
                    }
                })
                Divider(color = Color(0xFF1E213A), thickness = 1.dp)
                SettingsRow(title = androidx.compose.ui.res.stringResource(R.string.settings_share), value = "", icon = androidx.compose.material.icons.Icons.Default.Share, onClick = {
                    try {
                        val shareIntent = android.content.Intent().apply {
                            action = android.content.Intent.ACTION_SEND
                            putExtra(android.content.Intent.EXTRA_TEXT, "Check out BP Tracker!")
                            type = "text/plain"
                        }
                        context.startActivity(android.content.Intent.createChooser(shareIntent, "Share via"))
                    } catch (e: Exception) { }
                })
                Divider(color = Color(0xFF1E213A), thickness = 1.dp)
                SettingsRow(title = androidx.compose.ui.res.stringResource(R.string.settings_feedback), value = "", icon = androidx.compose.material.icons.Icons.Default.Edit, onClick = {
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO, android.net.Uri.parse("mailto:support@example.com"))
                        context.startActivity(android.content.Intent.createChooser(intent, "Send Email"))
                    } catch (e: Exception) {
                        android.widget.Toast.makeText(context, "No email app found", android.widget.Toast.LENGTH_SHORT).show()
                    }
                })
                Divider(color = Color(0xFF1E213A), thickness = 1.dp)
                SettingsRow(title = androidx.compose.ui.res.stringResource(R.string.settings_privacy), value = "", icon = androidx.compose.material.icons.Icons.Default.Description, onClick = {
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://google.com"))
                        context.startActivity(intent)
                    } catch (e: Exception) { }
                })
                Divider(color = Color(0xFF1E213A), thickness = 1.dp)
                SettingsRow(title = androidx.compose.ui.res.stringResource(R.string.settings_terms), value = "", icon = androidx.compose.material.icons.Icons.Default.Bookmark, onClick = {
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://google.com"))
                        context.startActivity(intent)
                    } catch (e: Exception) { }
                })
        }
        
        Spacer(modifier = Modifier.height(200.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderItem(id: Int, title: String, initialTime: String, iconRes: Int = 0, isIconVector: Boolean = false) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("RemindersPrefs", android.content.Context.MODE_PRIVATE)
    
    var isEnabled by remember { mutableStateOf(prefs.getBoolean("enabled_$id", initialTime.isNotEmpty())) }
    var time by remember { mutableStateOf(prefs.getString("time_$id", if (initialTime.isNotEmpty()) initialTime else "08:00 AM")!!) }
    var hour by remember { mutableStateOf(prefs.getInt("hour_$id", if (initialTime.isNotEmpty()) initialTime.split(":")[0].toInt().let { if (initialTime.contains("PM") && it != 12) it + 12 else if (initialTime.contains("AM") && it == 12) 0 else it } else 8)) }
    var minute by remember { mutableStateOf(prefs.getInt("minute_$id", if (initialTime.isNotEmpty()) initialTime.split(":")[1].substring(0, 2).toInt() else 0)) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    // Day Selection State
    val days = listOf("M", "T", "W", "T", "F", "S", "S")
    // Map to Calendar days (Sunday = 1, Monday = 2, ...)
    val calDays = listOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY)
    
    val selectedDays = remember { 
        mutableStateListOf(
            prefs.getBoolean("day_${id}_0", true),
            prefs.getBoolean("day_${id}_1", true),
            prefs.getBoolean("day_${id}_2", true),
            prefs.getBoolean("day_${id}_3", true),
            prefs.getBoolean("day_${id}_4", true),
            prefs.getBoolean("day_${id}_5", false),
            prefs.getBoolean("day_${id}_6", false)
        ) 
    } 
    
    fun savePrefs() {
        prefs.edit().apply {
            putBoolean("enabled_$id", isEnabled)
            putString("time_$id", time)
            putInt("hour_$id", hour)
            putInt("minute_$id", minute)
            selectedDays.forEachIndexed { index, isSelected ->
                putBoolean("day_${id}_$index", isSelected)
            }
        }.apply()
    }
    
    fun updateAlarm() {
        if (isEnabled) {
            val activeDays = calDays.filterIndexed { index, _ -> selectedDays[index] }
            ReminderManager.scheduleReminder(
                context = context,
                notificationId = id,
                title = "$title Reminder",
                message = "It's time to log your $title!",
                hour = hour,
                minute = minute,
                daysOfWeek = activeDays
            )
        } else {
            ReminderManager.cancelReminder(context, id)
        }
    }

    // Schedule initial if enabled
    LaunchedEffect(isEnabled) {
        updateAlarm()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isEnabled) Color(0xFF353A5F) else Color(0xFF2A2E4D))
                .clickable { isEnabled = !isEnabled; savePrefs(); updateAlarm() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(if (isEnabled) PrimaryBlue.copy(alpha = 0.2f) else Color.DarkGray.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isIconVector) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = if (isEnabled) PrimaryBlue else Color.Gray, modifier = Modifier.size(24.dp))
                    } else if (iconRes != 0) {
                        Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(24.dp), alpha = if (isEnabled) 1f else 0.5f)
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(title, color = if (isEnabled) Color.White else Color.Gray, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    if (isEnabled) {
                        Text(
                            text = time, 
                            color = PrimaryBlue, 
                            style = MaterialTheme.typography.bodyMedium, 
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { showTimePicker = true }
                        )
                    } else {
                        Text("Off", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = { 
                    isEnabled = it 
                    savePrefs()
                    updateAlarm()
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = PrimaryBlue,
                    uncheckedThumbColor = Color.LightGray,
                    uncheckedTrackColor = SurfaceDark
                )
            )
        }
        
        if (isEnabled) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                days.forEachIndexed { index, day ->
                    val isSelected = selectedDays[index]
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) PrimaryBlue else Color(0xFF1E213A))
                            .clickable { 
                                selectedDays[index] = !isSelected 
                                savePrefs()
                                updateAlarm()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(day, color = if (isSelected) Color.White else TextSecondary, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
    
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = hour,
            initialMinute = minute,
            is24Hour = false
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Select Time") },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(onClick = {
                    val amPm = if (timePickerState.hour >= 12) "PM" else "AM"
                    val hour12 = if (timePickerState.hour % 12 == 0) 12 else timePickerState.hour % 12
                    val minuteStr = String.format("%02d", timePickerState.minute)
                    time = "$hour12:$minuteStr $amPm"
                    hour = timePickerState.hour
                    minute = timePickerState.minute
                    savePrefs()
                    updateAlarm()
                    showTimePicker = false
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun SettingsRow(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector? = null, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.padding(end = 12.dp).size(20.dp))
            }
            Text(title, color = Color.White, style = MaterialTheme.typography.bodyLarge)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextSecondary, modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
fun LanguagePickerRow() {
    var expanded by remember { mutableStateOf(false) }
    val currentLangCode by com.example.bptracker.AppContainer.preferences.currentLanguage.collectAsState()
    val context = LocalContext.current

    
    val languages = listOf(
        "default" to "English",
        "es" to "Español",
        "hi" to "हिन्दी",
        "pt" to "Português",
        "fr" to "Français"
    )
    
    val currentLangName = languages.find { it.first == currentLangCode }?.second ?: "English"

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(androidx.compose.ui.res.stringResource(R.string.settings_language), color = Color.White, style = MaterialTheme.typography.bodyLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(currentLangName, color = PrimaryBlue, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.padding(start = 8.dp))
            }
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(SurfaceDark)
        ) {
            languages.forEach { (code, name) ->
                DropdownMenuItem(
                    text = { Text(name, color = Color.White) },
                    onClick = {
                        if (currentLangCode != code) {
                            com.example.bptracker.AppContainer.preferences.setLanguage(code)
                            expanded = false
                            (context as? Activity)?.recreate()
                        } else {
                            expanded = false
                        }
                    }
                )
            }
        }
    }
}
