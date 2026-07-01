package com.example.bptracker.ui.main

import android.widget.Toast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bptracker.AppContainer
import com.example.bptracker.R
import com.example.bptracker.data.*
import com.example.bptracker.theme.*
import com.example.bptracker.utils.StepCounterManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import androidx.compose.ui.platform.LocalContext
import java.util.Date
import java.util.Locale
import java.util.Calendar
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    startDestination: String? = null,
    onNavigateToAddBp: () -> Unit,
    onNavigateToAddSugar: () -> Unit,
    onNavigateToAddHeart: () -> Unit,
    onNavigateToBmi: () -> Unit,
    onNavigateToSteps: () -> Unit = {},
    onNavigateToPrivacy: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {}
) {
    var currentTab by remember { mutableStateOf(if (startDestination == "add_meds") 2 else 0) }
    var showHistory by remember { mutableStateOf(false) } 
    
    LaunchedEffect(startDestination) {
        if (startDestination == "add_meds") {
            currentTab = 2
        }
    }
    
    val context = LocalContext.current
    val stepCounter = remember { StepCounterManager(context) }
    val realSteps by stepCounter.currentSteps.collectAsStateWithLifecycle()
    var isPedometerActive by remember { mutableStateOf(false) }
    
    val activityPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            isPedometerActive = true
            stepCounter.startListening()
        } else {
            Toast.makeText(context, "Permission needed for Step Counter", Toast.LENGTH_SHORT).show()
        }
    }
    
    DisposableEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {
            isPedometerActive = true
            stepCounter.startListening()
        }
        onDispose {
            stepCounter.stopListening()
        }
    }
    var homeSelectedArticle by remember { mutableStateOf<Article?>(null) }
    
    val vitals by AppContainer.repository.vitals.collectAsStateWithLifecycle()
    val profiles by AppContainer.repository.profiles.collectAsStateWithLifecycle()
    val activeProfileId by AppContainer.repository.activeProfileId.collectAsStateWithLifecycle()
    val waterLogs by AppContainer.repository.waterLogs.collectAsStateWithLifecycle()
    val medications by AppContainer.repository.medications.collectAsStateWithLifecycle()
    val medicationLogs by AppContainer.repository.medicationLogs.collectAsStateWithLifecycle()
    
    val activeVitals = vitals.filter { it.profileId == activeProfileId }
    val latestRecord = activeVitals.firstOrNull()
    val activeWater = waterLogs.filter { it.profileId == activeProfileId }
    
    if (showHistory) {
        HistoryScreen(onBack = { showHistory = false })
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1E213A))) {
        // Main Content
        Box(modifier = Modifier.fillMaxSize().padding(bottom = 80.dp)) {
            when (currentTab) {
                0 -> DashboardTab(
                    profiles = profiles,
                    activeProfileId = activeProfileId,
                    onProfileSwitch = { AppContainer.repository.setActiveProfile(it) },
                    activeVitals = activeVitals,
                    activeWater = activeWater,
                    latestRecord = latestRecord, 
                    onNavigateToAddBp = onNavigateToAddBp,
                    onNavigateToAddSugar = onNavigateToAddSugar,
                    onNavigateToAddHeart = onNavigateToAddHeart,
                    onNavigateToBmi = onNavigateToBmi,
                    onNavigateToSteps = onNavigateToSteps,
                    onNavigateToHistory = { showHistory = true }, 
                    onArticleClick = { article -> 
                        homeSelectedArticle = article
                        currentTab = 3
                    },
                    isPedometerActive = isPedometerActive,
                    onPedometerActivated = { isPedometerActive = true },
                    realSteps = realSteps,
                    activityPermissionLauncher = activityPermissionLauncher,
                    stepCounter = stepCounter
                )
                1 -> AnalyticsScreen(vitals = activeVitals)
                2 -> MedicationsScreen(
                    activeProfileId = activeProfileId, 
                    medications = medications, 
                    medicationLogs = medicationLogs
                )
                3 -> InfoScreen(
                    preSelectedArticle = homeSelectedArticle, 
                    onBackFromArticle = { homeSelectedArticle = null }
                )
                4 -> SettingsScreen(
                    onNavigateToPrivacy = onNavigateToPrivacy,
                    onNavigateToTerms = onNavigateToTerms
                )
            }
        }
        
        // Classic Bottom Navigation Bar
        NavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            containerColor = Color(0xFF2A2E4D),
            tonalElevation = 8.dp
        ) {
            NavigationBarItem(
                selected = currentTab == 0, onClick = { currentTab = 0 },
                icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.tab_home)) },
                label = { Text(stringResource(R.string.tab_home)) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryBlue, unselectedIconColor = TextSecondary, selectedTextColor = PrimaryBlue, unselectedTextColor = TextSecondary, indicatorColor = Color.Transparent)
            )
            NavigationBarItem(
                selected = currentTab == 1, onClick = { currentTab = 1 },
                icon = { Icon(Icons.Default.Timeline, contentDescription = stringResource(R.string.tab_analytics)) },
                label = { Text(stringResource(R.string.tab_analytics)) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryBlue, unselectedIconColor = TextSecondary, selectedTextColor = PrimaryBlue, unselectedTextColor = TextSecondary, indicatorColor = Color.Transparent)
            )
            NavigationBarItem(
                selected = currentTab == 2, onClick = { currentTab = 2 },
                icon = { Icon(Icons.Default.Medication, contentDescription = stringResource(R.string.tab_meds)) },
                label = { Text(stringResource(R.string.tab_meds)) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryBlue, unselectedIconColor = TextSecondary, selectedTextColor = PrimaryBlue, unselectedTextColor = TextSecondary, indicatorColor = Color.Transparent)
            )
            NavigationBarItem(
                selected = currentTab == 3, onClick = { currentTab = 3 },
                icon = { Icon(Icons.Default.MenuBook, contentDescription = stringResource(R.string.tab_info)) },
                label = { Text(stringResource(R.string.tab_info)) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryBlue, unselectedIconColor = TextSecondary, selectedTextColor = PrimaryBlue, unselectedTextColor = TextSecondary, indicatorColor = Color.Transparent)
            )
            NavigationBarItem(
                selected = currentTab == 4, onClick = { currentTab = 4 },
                icon = { Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.tab_settings)) },
                label = { Text(stringResource(R.string.tab_settings)) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryBlue, unselectedIconColor = TextSecondary, selectedTextColor = PrimaryBlue, unselectedTextColor = TextSecondary, indicatorColor = Color.Transparent)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTab(
    profiles: List<Profile>,
    activeProfileId: String,
    onProfileSwitch: (String) -> Unit,
    activeVitals: List<VitalRecord>,
    activeWater: List<WaterLog>,
    latestRecord: VitalRecord?,
    onNavigateToAddBp: () -> Unit,
    onNavigateToAddSugar: () -> Unit,
    onNavigateToAddHeart: () -> Unit,
    onNavigateToBmi: () -> Unit,
    onNavigateToSteps: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onArticleClick: (Article) -> Unit,
    isPedometerActive: Boolean,
    onPedometerActivated: () -> Unit,
    realSteps: Int,
    activityPermissionLauncher: androidx.activity.compose.ManagedActivityResultLauncher<String, Boolean>,
    stepCounter: com.example.bptracker.utils.StepCounterManager
) {
    val coroutineScope = rememberCoroutineScope()
    val streakDays = calculateStreak(activeVitals, activeWater)
    val insightMessage = generateSmartInsight(activeVitals)
    
    // Water
    val todayWaterMl = calculateTodayWater(activeWater)
    val dailyWaterTarget = 2000 
    val waterProgress = (todayWaterMl.toFloat() / dailyWaterTarget).coerceIn(0f, 1f)

    // Health Connect Mock
    var showHealthConnectDialog by remember { mutableStateOf(false) }
    var healthConnected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        
        // 1. Profile Selector Row
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            var expanded by remember { mutableStateOf(false) }
            var showAddProfileDialog by remember { mutableStateOf(false) }
            var showEditProfileDialog by remember { mutableStateOf(false) }
            val activeProfile = profiles.find { it.id == activeProfileId } ?: profiles.firstOrNull()
            
            // Add Profile Dialog
            if (showAddProfileDialog) {
                var newProfileName by remember { mutableStateOf("") }
                AlertDialog(
                    onDismissRequest = { showAddProfileDialog = false },
                    title = { Text("Add Family Member") },
                    text = {
                        OutlinedTextField(
                            value = newProfileName, onValueChange = { newProfileName = it }, label = { Text("Name") }, singleLine = true
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (newProfileName.isNotBlank()) {
                                coroutineScope.launch {
                                    val newProfile = Profile(name = newProfileName)
                                    AppContainer.repository.addProfile(newProfile)
                                    AppContainer.repository.setActiveProfile(newProfile.id)
                                }
                                showAddProfileDialog = false
                            }
                        }) { Text("Add") }
                    },
                    dismissButton = { TextButton(onClick = { showAddProfileDialog = false }) { Text("Cancel") } }
                )
            }

            // Edit Profile Dialog
            if (showEditProfileDialog) {
                var editProfileName by remember { mutableStateOf(activeProfile?.name ?: "") }
                AlertDialog(
                    onDismissRequest = { showEditProfileDialog = false },
                    title = { Text("Edit Profile Name") },
                    text = {
                        OutlinedTextField(
                            value = editProfileName, onValueChange = { editProfileName = it }, label = { Text("Name") }, singleLine = true
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (editProfileName.isNotBlank() && activeProfile != null) {
                                coroutineScope.launch {
                                    AppContainer.repository.updateProfileName(activeProfile.id, editProfileName)
                                }
                                showEditProfileDialog = false
                            }
                        }) { Text("Save") }
                    },
                    dismissButton = { TextButton(onClick = { showEditProfileDialog = false }) { Text("Cancel") } }
                )
            }
            
            Box {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { expanded = true }) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(activeProfile?.name ?: "Profile", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { showEditProfileDialog = true }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = TextSecondary, modifier = Modifier.size(16.dp))
                    }
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    profiles.forEach { profile ->
                        DropdownMenuItem(
                            text = { Text(profile.name) },
                            onClick = { onProfileSwitch(profile.id); expanded = false }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("+ Add Profile") },
                        onClick = { showAddProfileDialog = true; expanded = false }
                    )
                }
            }
            
            val context = LocalContext.current
            var showNotifications by remember { mutableStateOf(false) }
            val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
            
            // Re-read when dialog opens or just check live state
            var hasActiveReminders by remember { mutableStateOf(false) }
            
            DisposableEffect(Unit) {
                val listener = android.content.SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _ -> 
                    var active = false
                    for (i in 1..4) {
                        if (sharedPreferences.getBoolean("enabled_$i", false)) active = true
                    }
                    hasActiveReminders = active
                }
                prefs.registerOnSharedPreferenceChangeListener(listener)
                
                var active = false
                for (i in 1..4) {
                    if (prefs.getBoolean("enabled_$i", false)) active = true
                }
                hasActiveReminders = active
                
                onDispose {
                    prefs.unregisterOnSharedPreferenceChangeListener(listener)
                }
            }
            
            Box {
                IconButton(onClick = { 
                    showNotifications = true
                    // Re-check
                    hasActiveReminders = false
                    for (i in 1..4) {
                        if (prefs.getBoolean("enabled_$i", false)) hasActiveReminders = true
                    }
                }) {
                    BadgedBox(
                        badge = {
                            if (hasActiveReminders) {
                                Badge(containerColor = Color.Red)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                }
                
                DropdownMenu(expanded = showNotifications, onDismissRequest = { showNotifications = false }) {
                    val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                    var hasReminders = false
                    
                    val titles = listOf("Blood Pressure", "Blood Sugar", "Heart Rate", "Medications")
                    for (i in 1..4) {
                        if (prefs.getBoolean("enabled_$i", false)) {
                            hasReminders = true
                            val time = prefs.getString("time_$i", "")
                            DropdownMenuItem(
                                text = { Text("${titles[i-1]} Reminder: $time") },
                                onClick = { showNotifications = false }
                            )
                        }
                    }
                    
                    if (!hasReminders) {
                        DropdownMenuItem(
                            text = { Text("No active reminders") },
                            onClick = { showNotifications = false }
                        )
                    }
                }
            }
        }
        
        // 2. Beautiful Streak Banner
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2E4D)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🔥", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Active Streak", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Log daily to keep it going!", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Text("$streakDays Days", color = HighOrange, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            }
        }
        
        // 3. Smart Insights
        if (insightMessage.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(insightMessage, color = PrimaryBlue, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // --- GRID LAYOUT ---

        // ROW 1: Daily Steps | Daily Hydration
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Steps Card
            Card(
                modifier = Modifier.weight(1f).aspectRatio(1f).clickable { onNavigateToSteps() },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF25294A)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353A5F)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                    Icon(Icons.Default.DirectionsWalk, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(40.dp))
                    Text(stringResource(R.string.card_steps), color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text("$realSteps", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    
                    if (!isPedometerActive) {
                        Button(
                            onClick = { 
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                    activityPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                                } else {
                                    onPedometerActivated()
                                    stepCounter.startListening()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Connect", style = MaterialTheme.typography.bodySmall)
                        }
                    } else {
                        val progress = (realSteps / 10000f).coerceIn(0f, 1f)
                        LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth().height(4.dp), color = PrimaryBlue, trackColor = SurfaceDark)
                    }
                }
            }

            // Hydration Card
            Card(
                modifier = Modifier.weight(1f).aspectRatio(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF25294A)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353A5F)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                    Icon(Icons.Default.WaterDrop, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(40.dp))
                    Text(stringResource(R.string.card_water), color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text("$todayWaterMl ml", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { coroutineScope.launch { AppContainer.repository.removeLastWaterLog(activeProfileId) } }, 
                            modifier = Modifier.weight(1f).height(36.dp),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark)
                        ) {
                            Text("-", color = Color.White, style = MaterialTheme.typography.titleLarge)
                        }
                        Button(
                            onClick = { coroutineScope.launch { AppContainer.repository.addWaterLog(WaterLog(profileId = activeProfileId, amountMl = 250)) } }, 
                            modifier = Modifier.weight(1f).height(36.dp),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Text("+", color = Color.White, style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }
        }

        // ROW 2: BP | Sugar
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // BP Card
            Card(
                modifier = Modifier.weight(1f).aspectRatio(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF25294A)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353A5F)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                    Image(painter = painterResource(id = R.drawable.ic_bp), contentDescription = null, modifier = Modifier.size(40.dp))
                    Text(stringResource(R.string.card_bp), color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    
                    if (latestRecord != null && latestRecord.systolic > 0) {
                        Text("${latestRecord.systolic}/${latestRecord.diastolic}", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    } else {
                        Text("--/--", color = TextSecondary, style = MaterialTheme.typography.titleMedium)
                    }
                    
                    Button(
                        onClick = onNavigateToAddBp, modifier = Modifier.fillMaxWidth().height(32.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(0.dp)
                    ) { Text("Log", style = MaterialTheme.typography.bodySmall) }
                }
            }

            // Sugar Card
            Card(
                modifier = Modifier.weight(1f).aspectRatio(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF25294A)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353A5F)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                    Image(painter = painterResource(id = R.drawable.ic_sugar), contentDescription = null, modifier = Modifier.size(40.dp))
                    Text(stringResource(R.string.card_sugar), color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    
                    val sugarValue = if (latestRecord != null && latestRecord.bloodSugar > 0) latestRecord.bloodSugar else if (latestRecord != null && latestRecord.bloodSugarPP > 0) latestRecord.bloodSugarPP else 0
                    if (sugarValue > 0) {
                        Text("$sugarValue mg/dL", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    } else {
                        Text("--", color = TextSecondary, style = MaterialTheme.typography.titleMedium)
                    }
                    
                    Button(
                        onClick = onNavigateToAddSugar, modifier = Modifier.fillMaxWidth().height(32.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(0.dp)
                    ) { Text("Log", style = MaterialTheme.typography.bodySmall) }
                }
            }
        }

        // ROW 3: Heart Rate | BMI
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Heart Card
            Card(
                modifier = Modifier.weight(1f).aspectRatio(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF25294A)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353A5F)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                    Image(painter = painterResource(id = R.drawable.ic_heart), contentDescription = null, modifier = Modifier.size(40.dp))
                    Text(stringResource(R.string.card_heart), color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    
                    if (latestRecord != null && latestRecord.heartRate > 0) {
                        Text("${latestRecord.heartRate} bpm", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    } else {
                        Text("--", color = TextSecondary, style = MaterialTheme.typography.titleMedium)
                    }
                    
                    Button(
                        onClick = onNavigateToAddHeart, modifier = Modifier.fillMaxWidth().height(32.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(0.dp)
                    ) { Text("Measure", style = MaterialTheme.typography.bodySmall) }
                }
            }

            // BMI Card
            Card(
                modifier = Modifier.weight(1f).aspectRatio(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF25294A)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF353A5F)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                    Image(painter = painterResource(id = R.drawable.ic_bmi), contentDescription = null, modifier = Modifier.size(40.dp))
                    Text(stringResource(R.string.card_bmi), color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text("Check", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    
                    Button(
                        onClick = onNavigateToBmi, modifier = Modifier.fillMaxWidth().height(32.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(0.dp)
                    ) { Text("Check", style = MaterialTheme.typography.bodySmall) }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Info & Knowledge Section (Health Guide Blog)
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Health Guide", color = Color.White, style = MaterialTheme.typography.titleLarge)
            Text("View All", color = PrimaryBlue, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { 
                onArticleClick(
                    Article(
                        title = allBlogArticles[0].title, 
                        content = allBlogArticles[0].content, 
                        color = allBlogArticles[0].categoryColor, 
                        icon = Icons.Default.Info, 
                        category = ArticleCategory.BLOOD_PRESSURE
                    )
                ) // Just pass the first article to switch to Info tab, Info tab will show all. 
            })
        }
        
        Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
            // Show only the first 3 as Featured
            allBlogArticles.take(3).forEach { article ->
                Card(
                    modifier = Modifier.fillMaxWidth().height(100.dp).clickable { 
                        onArticleClick(
                            Article(
                                title = article.title, 
                                content = article.content, 
                                color = article.categoryColor, 
                                icon = Icons.Default.Info, 
                                category = ArticleCategory.BLOOD_PRESSURE
                            )
                        ) 
                    },
                    colors = CardDefaults.cardColors(containerColor = article.categoryColor.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(60.dp).background(article.categoryColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = article.categoryColor, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                            Text(article.title, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Read Article >", color = article.categoryColor, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        
        // Medical Disclaimer
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E213A)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Disclaimer: This app is for informational purposes only and is not a substitute for professional medical advice, diagnosis, or treatment. Always consult with a doctor.",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    // Health Connect Mock Dialog
    if (showHealthConnectDialog) {
        AlertDialog(
            onDismissRequest = { showHealthConnectDialog = false },
            title = { Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Health Connect API")
            }},
            text = { Text("Allow BP Tracker to read 'Steps' from Google Health Connect?\n\n(This is a UI mockup demonstrating the OAuth permissions flow for the real integration.)") },
            confirmButton = {
                TextButton(onClick = { healthConnected = true; showHealthConnectDialog = false }) { Text("Allow") }
            },
            dismissButton = {
                TextButton(onClick = { showHealthConnectDialog = false }) { Text("Deny") }
            }
        )
    }
}

@Composable
fun ArticleListCard(title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2E4D)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Gamification & Insights Helpers
fun calculateStreak(vitals: List<VitalRecord>, waterLogs: List<WaterLog>): Int {
    val allDates = mutableSetOf<String>()
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    vitals.forEach { allDates.add(sdf.format(Date(it.timestamp))) }
    waterLogs.forEach { allDates.add(sdf.format(Date(it.timestamp))) }
    
    if (allDates.isEmpty()) return 0
    val cal = Calendar.getInstance()
    val today = sdf.format(cal.time)
    cal.add(Calendar.DAY_OF_YEAR, -1)
    val yesterday = sdf.format(cal.time)
    
    if (!allDates.contains(today) && !allDates.contains(yesterday)) return 0
    
    var streak = 0
    cal.time = Date()
    while(true) {
        val dateStr = sdf.format(cal.time)
        if (allDates.contains(dateStr)) streak++
        else if (dateStr != today) break
        cal.add(Calendar.DAY_OF_YEAR, -1)
    }
    return streak
}

fun calculateTodayWater(waterLogs: List<WaterLog>): Int {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val todayStr = sdf.format(Date())
    return waterLogs.filter { sdf.format(Date(it.timestamp)) == todayStr }.sumOf { it.amountMl }
}

fun generateSmartInsight(vitals: List<VitalRecord>): String {
    if (vitals.size < 3) return "Log your vitals consistently for a few days to unlock personalized smart AI insights!"
    
    val recentBps = vitals.filter { it.systolic > 0 }.take(5)
    if (recentBps.size >= 3) {
        val allNormal = recentBps.all { it.systolic < 120 && it.diastolic < 80 }
        if (allNormal) return "Fantastic! Your last ${recentBps.size} blood pressure readings have been perfectly normal."
    }
    
    return "You are doing great staying consistent with your tracking. Consistency is key!"
}
