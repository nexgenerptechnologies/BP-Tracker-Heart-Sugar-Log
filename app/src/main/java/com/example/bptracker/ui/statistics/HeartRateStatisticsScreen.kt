package com.example.bptracker.ui.statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bptracker.AppContainer
import com.example.bptracker.R
import com.example.bptracker.data.VitalRecord
import com.example.bptracker.theme.HighOrange
import com.example.bptracker.theme.NormalGreen
import com.example.bptracker.theme.PrimaryBlue
import com.example.bptracker.theme.SurfaceDark
import com.example.bptracker.theme.TextSecondary
import com.example.bptracker.theme.ElevatedYellow
import androidx.compose.material.icons.filled.MonitorHeart
import com.example.bptracker.ui.components.DonutChart
import com.example.bptracker.ui.components.SimpleBarChart
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeartRateStatisticsScreen(
    onBack: () -> Unit,
    onMeasureNow: () -> Unit,
    onViewAll: () -> Unit = {}
) {
    val vitals by AppContainer.repository.vitals.collectAsStateWithLifecycle()
    val hrRecords = vitals.filter { it.heartRate > 0 }.sortedByDescending { it.timestamp }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Heart Rate Statistics", color = Color.White) },
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
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Measure Heart Rate", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
                HeartRateZonesSection(hrRecords)
            }
            
            item {
                HrWeeklyAnalyticsSection(hrRecords)
            }
            
            item {
                HrRecentReadingsSection(hrRecords, onViewAll)
            }
            
            item {
                HrSmartInsightsSection(hrRecords)
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HrSmartInsightsSection(records: List<VitalRecord>) {
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
                "Measure your heart rate at least 3 times to unlock personalized AI insights."
            } else {
                val avgHr = records.map { it.heartRate }.average().roundToInt()
                if (avgHr in 60..100) {
                    "Your resting heart rate ($avgHr bpm) is generally considered normal for adults. Excellent consistency!"
                } else if (avgHr < 60) {
                    "Your resting heart rate ($avgHr bpm) is on the lower side, which is often typical for athletes or those with high fitness levels."
                } else {
                    "Your heart rate has been averaging somewhat high ($avgHr bpm). Ensure you are resting, hydrating, and managing stress."
                }
            }
            Text(insight, color = Color.White.copy(alpha=0.9f), style = MaterialTheme.typography.bodyMedium, lineHeight = androidx.compose.ui.unit.TextUnit(20f, androidx.compose.ui.unit.TextUnitType.Sp))
        }
    }
}

@Composable
fun HeartRateZonesSection(records: List<VitalRecord>) {
    val total = records.size
    val rest = records.count { it.heartRateState == "Resting" }
    val anxious = records.count { it.heartRateState == "Anxious" || it.heartRateState == "Excited" }
    val high = records.count { it.heartRateState == "After Exercise" }
    val normal = records.count { it.heartRate in 60..100 }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                DonutChart(
                    data = listOf(rest.toFloat(), anxious.toFloat(), high.toFloat(), normal.toFloat()),
                    colors = listOf(PrimaryBlue, ElevatedYellow, HighOrange, NormalGreen),
                    totalText = "Measurements",
                    modifier = Modifier.size(200.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Activity Zones", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Distribution of measurements", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ZoneLegendRow("Resting", PrimaryBlue, rest, total)
            Spacer(modifier = Modifier.height(12.dp))
            ZoneLegendRow("Normal", NormalGreen, normal, total)
            Spacer(modifier = Modifier.height(12.dp))
            ZoneLegendRow("Excited/Anxious", ElevatedYellow, anxious, total)
            Spacer(modifier = Modifier.height(12.dp))
            ZoneLegendRow("After Exercise", HighOrange, high, total)
        }
    }
}

@Composable
fun ZoneLegendRow(label: String, color: Color, count: Int, total: Int) {
    val percentage = if (total > 0) ((count.toFloat() / total) * 100).roundToInt() else 0
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(12.dp).background(color, RoundedCornerShape(6.dp)))
            Spacer(modifier = Modifier.width(12.dp))
            Text(label, color = Color.White, style = MaterialTheme.typography.bodyMedium)
        }
        Text("$count ($percentage%)", color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun HrWeeklyAnalyticsSection(records: List<VitalRecord>) {
    val cal = Calendar.getInstance()
    val weeklyData = mutableListOf<Float>()
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    for (i in 6 downTo 0) {
        cal.time = Date()
        cal.add(Calendar.DAY_OF_YEAR, -i)
        val dateStr = sdf.format(cal.time)
        
        val dayRecords = records.filter { sdf.format(Date(it.timestamp)) == dateStr }
        if (dayRecords.isNotEmpty()) {
            weeklyData.add(dayRecords.map { it.heartRate }.average().toFloat())
        } else {
            weeklyData.add(0f)
        }
    }

    val maxVal = records.maxOfOrNull { it.heartRate } ?: 0
    val minVal = records.filter{ it.heartRate > 0}.minOfOrNull { it.heartRate } ?: 0
    val avgVal = if (records.isNotEmpty()) records.map { it.heartRate }.average().roundToInt() else 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column {
                    Text("ANALYTICS", color = HighOrange, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Text("Weekly Averages", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("$avgVal", color = HighOrange, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("AVG BPM", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SimpleBarChart(
                data = weeklyData,
                color = HighOrange,
                maxValue = max(100f, (maxVal + 20).toFloat()),
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                StatCard("MAX", "$maxVal", Modifier.weight(1f))
                Spacer(modifier = Modifier.width(12.dp))
                StatCard("MIN", "$minVal", Modifier.weight(1f))
                Spacer(modifier = Modifier.width(12.dp))
                StatCard("AVG", "$avgVal", Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color(0xFF2A2E4D), RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, color = TextSecondary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun HrRecentReadingsSection(records: List<VitalRecord>, onViewAll: () -> Unit) {
    val topRecords = records.take(3)
    val sdf = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Recent Readings", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("View All", color = HighOrange, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onViewAll() })
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
                            modifier = Modifier.size(48.dp).background(HighOrange.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.MonitorHeart, contentDescription = null, tint = HighOrange)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${record.heartRate} bpm", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                val statusStr = record.heartRateState.ifEmpty { "Normal" }
                                val statusColor = if (statusStr == "Resting" || statusStr == "Normal") NormalGreen else HighOrange
                                
                                Box(modifier = Modifier.background(statusColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                    Text(statusStr, color = statusColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(sdf.format(Date(record.timestamp)), color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                        } // Closes inner Column
                    } // Closes Row
                } // Closes Card
            } // Closes forEach
        } // Closes else
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Additional UI for Articles
        Column {
                Box(modifier = Modifier.fillMaxWidth().height(140.dp).background(PrimaryBlue.copy(alpha=0.3f))) {
                    // Placeholder for actual image
                    Text("Illustration", color = PrimaryBlue, modifier = Modifier.align(Alignment.Center))
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("LIFESTYLE", color = PrimaryBlue, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("The importance of sleep for heart health", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column {
                Box(modifier = Modifier.fillMaxWidth().height(140.dp).background(HighOrange.copy(alpha=0.3f))) {
                    // Placeholder for actual image
                    Text("Illustration", color = HighOrange, modifier = Modifier.align(Alignment.Center))
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("LIFESTYLE", color = PrimaryBlue, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Diet and heart health", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
