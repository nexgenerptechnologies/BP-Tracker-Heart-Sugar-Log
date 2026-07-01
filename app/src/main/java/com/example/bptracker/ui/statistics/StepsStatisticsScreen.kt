package com.example.bptracker.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bptracker.theme.NormalGreen
import com.example.bptracker.theme.PrimaryBlue
import com.example.bptracker.theme.SurfaceDark
import com.example.bptracker.theme.TextSecondary
import com.example.bptracker.ui.components.DonutChart
import com.example.bptracker.ui.components.SimpleBarChart
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepsStatisticsScreen(
    onBack: () -> Unit,
    onViewAll: () -> Unit = {}
) {
    // We don't have historical steps in Room DB since it's mock Health Connect data, 
    // but we can show a UI for what it would look like.
    
    val currentSteps = 4230 // Example mock data for today
    val dailyGoal = 10000

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Steps Statistics", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E2235))
            )
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
                StepsTodaySection(currentSteps, dailyGoal)
            }
            
            item {
                StepsWeeklyAnalyticsSection()
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun StepsTodaySection(currentSteps: Int, dailyGoal: Int) {
    val progress = (currentSteps.toFloat() / dailyGoal).coerceIn(0f, 1f)
    val remaining = max(0, dailyGoal - currentSteps)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                DonutChart(
                    data = listOf(currentSteps.toFloat(), remaining.toFloat()),
                    colors = listOf(NormalGreen, Color.DarkGray),
                    totalText = "Daily Goal",
                    modifier = Modifier.size(200.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Today's Progress", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Goal: $dailyGoal steps", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ZoneLegendRow("Completed", NormalGreen, currentSteps, dailyGoal)
            Spacer(modifier = Modifier.height(12.dp))
            ZoneLegendRow("Remaining", Color.DarkGray, remaining, dailyGoal)
        }
    }
}

@Composable
fun StepsWeeklyAnalyticsSection() {
    // Mock weekly data
    val weeklyData = listOf(3500f, 8200f, 10500f, 7400f, 9800f, 11200f, 4230f)
    
    val maxVal = weeklyData.maxOrNull()?.toInt() ?: 0
    val avgVal = weeklyData.average().toInt()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column {
                    Text("ANALYTICS", color = NormalGreen, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Text("Weekly Steps", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("$avgVal", color = NormalGreen, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("AVG STEPS", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SimpleBarChart(
                data = weeklyData,
                color = NormalGreen,
                maxValue = max(10000f, (maxVal + 2000).toFloat()),
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                StatCard("MAX", "$maxVal", Modifier.weight(1f))
                Spacer(modifier = Modifier.width(12.dp))
                StatCard("AVG", "$avgVal", Modifier.weight(1f))
            }
        }
    }
}
