package com.example.bptracker.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bptracker.AppContainer
import com.example.bptracker.data.VitalRecord
import com.example.bptracker.theme.HighOrange
import com.example.bptracker.theme.NormalGreen
import com.example.bptracker.theme.PrimaryBlue
import com.example.bptracker.theme.SurfaceDark
import com.example.bptracker.theme.TextSecondary
import com.example.bptracker.ui.components.DonutChart
import com.example.bptracker.ui.components.SimpleBarChart
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BpStatisticsScreen(
    onBack: () -> Unit,
    onMeasureNow: () -> Unit,
    onViewAll: () -> Unit = {}
) {
    val vitals by AppContainer.repository.vitals.collectAsStateWithLifecycle()
    val bpRecords = vitals.filter { it.systolic > 0 }.sortedByDescending { it.timestamp }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Blood Pressure Statistics", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E2235))
            )
        },
        bottomBar = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E2235))
                .navigationBarsPadding()
                .padding(16.dp)
            ) {
                Button(
                    onClick = onMeasureNow,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Blood Pressure", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E2235))
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                BpHealthZonesSection(bpRecords)
            }
            
            item {
                BpWeeklyAnalyticsSection(bpRecords)
            }
            
            item {
                BpRecentReadingsSection(bpRecords, onViewAll)
            }
            
            item {
                BpSmartInsightsSection(bpRecords)
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun BpSmartInsightsSection(records: List<VitalRecord>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Insights For You", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            val insight = if (records.size < 3) {
                "Log at least 3 blood pressure readings to unlock personalized AI insights about your cardiovascular health trends."
            } else {
                val avgSys = records.map { it.systolic }.average().roundToInt()
                val avgDia = records.map { it.diastolic }.average().roundToInt()
                if (avgSys < 120 && avgDia < 80) {
                    "Great job! Your average BP ($avgSys/$avgDia) is firmly in the normal range. Keep up your healthy lifestyle."
                } else if (avgSys in 120..129 && avgDia < 80) {
                    "Your blood pressure is slightly elevated ($avgSys/$avgDia on average). Consider reducing sodium intake and managing stress."
                } else {
                    "Your average BP ($avgSys/$avgDia) is considered high. Make sure to monitor it closely and consult with your healthcare provider."
                }
            }
            Text(insight, color = Color.White.copy(alpha=0.9f), style = MaterialTheme.typography.bodyMedium, lineHeight = androidx.compose.ui.unit.TextUnit(20f, androidx.compose.ui.unit.TextUnitType.Sp))
        }
    }
}

@Composable
fun BpHealthZonesSection(records: List<VitalRecord>) {
    val total = records.size
    val normal = records.count { it.systolic < 120 && it.diastolic < 80 }
    val elevated = records.count { (it.systolic in 120..129) && it.diastolic < 80 }
    val high = records.count { it.systolic >= 130 || it.diastolic >= 80 }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                DonutChart(
                    data = listOf(normal.toFloat(), elevated.toFloat(), high.toFloat()),
                    colors = listOf(NormalGreen, PrimaryBlue, HighOrange),
                    totalText = "In Total",
                    modifier = Modifier.size(200.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Health Zones", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Measurement distribution", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ZoneLegendRow("Normal", NormalGreen, normal, total)
            Spacer(modifier = Modifier.height(12.dp))
            ZoneLegendRow("Elevated", PrimaryBlue, elevated, total)
            Spacer(modifier = Modifier.height(12.dp))
            ZoneLegendRow("High BP (Hypertension)", HighOrange, high, total)
        }
    }
}

@Composable
fun BpWeeklyAnalyticsSection(records: List<VitalRecord>) {
    val cal = Calendar.getInstance()
    val weeklyData = mutableListOf<Float>()
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    for (i in 6 downTo 0) {
        cal.time = Date()
        cal.add(Calendar.DAY_OF_YEAR, -i)
        val dateStr = sdf.format(cal.time)
        val dayRecords = records.filter { sdf.format(Date(it.timestamp)) == dateStr }
        if (dayRecords.isNotEmpty()) {
            weeklyData.add(dayRecords.map { it.systolic }.average().toFloat())
        } else {
            weeklyData.add(0f)
        }
    }

    val maxVal = records.maxOfOrNull { it.systolic } ?: 0
    val minVal = records.filter{ it.systolic > 0}.minOfOrNull { it.systolic } ?: 0
    val avgVal = if (records.isNotEmpty()) records.map { it.systolic }.average().roundToInt() else 0
    val avgDias = if (records.isNotEmpty()) records.map { it.diastolic }.average().roundToInt() else 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column {
                    Text("ANALYTICS", color = PrimaryBlue, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Text("Weekly Systolic", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("$avgVal/$avgDias", color = PrimaryBlue, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("AVG BP", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SimpleBarChart(
                data = weeklyData,
                color = PrimaryBlue,
                maxValue = max(120f, (maxVal + 20).toFloat()),
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                StatCard("MAX SYS", "$maxVal", Modifier.weight(1f))
                Spacer(modifier = Modifier.width(12.dp))
                StatCard("MIN SYS", "$minVal", Modifier.weight(1f))
                Spacer(modifier = Modifier.width(12.dp))
                StatCard("AVG", "$avgVal", Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun BpRecentReadingsSection(records: List<VitalRecord>, onViewAll: () -> Unit) {
    val topRecords = records.take(3)
    val sdf = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Recent Readings", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("View All", color = PrimaryBlue, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onViewAll() })
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (topRecords.isEmpty()) {
            Text("No recent readings.", color = TextSecondary, modifier = Modifier.padding(16.dp))
        } else {
            topRecords.forEach { record ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(48.dp).background(PrimaryBlue.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = PrimaryBlue)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${record.systolic}/${record.diastolic} mmHg", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                val statusStr = when {
                                    record.systolic < 120 -> "NORMAL"
                                    record.systolic in 120..129 -> "ELEVATED"
                                    else -> "HIGH"
                                }
                                val statusColor = when {
                                    record.systolic < 120 -> NormalGreen
                                    record.systolic in 120..129 -> PrimaryBlue
                                    else -> HighOrange
                                }
                                
                                Box(modifier = Modifier.background(statusColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                    Text(statusStr, color = statusColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(sdf.format(Date(record.timestamp)), color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
