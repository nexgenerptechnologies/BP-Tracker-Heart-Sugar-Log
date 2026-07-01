package com.example.bptracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun DonutChart(
    data: List<Float>,
    colors: List<Color>,
    totalText: String,
    modifier: Modifier = Modifier
) {
    val total = max(1f, data.sum())
    var animationPlayed by remember { mutableStateOf(false) }
    
    val animatedSweep = animateFloatAsState(
        targetValue = if (animationPlayed) 360f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "sweep_anim"
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 24.dp.toPx() // Thinner, elegant stroke
            var startAngle = -90f

            for (i in data.indices) {
                val sweepAngle = (data[i] / total) * animatedSweep.value
                drawArc(
                    color = colors[i],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                startAngle += sweepAngle
            }
            
            // Draw background track if no data
            if (data.sum() == 0f) {
                drawArc(
                    color = Color.DarkGray.copy(alpha = 0.3f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${data.sum().toInt()}",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = totalText,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun SimpleBarChart(
    data: List<Float>, // Values (e.g. 7 values for a week)
    color: Color,
    maxValue: Float,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }
    
    val animatedHeight = animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "height_anim"
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Canvas(modifier = modifier) {
        val barWidth = 12.dp.toPx()
        val cornerRadius = CornerRadius(6.dp.toPx())
        val spacing = (size.width - (data.size * barWidth)) / max(1, data.size - 1)
        val actualMax = max(1f, maxValue)

        for (i in data.indices) {
            val barHeight = (data[i] / actualMax) * size.height * animatedHeight.value
            val startX = i * (barWidth + spacing)
            val startY = size.height - barHeight

            // Draw track background
            drawRoundRect(
                color = Color.DarkGray.copy(alpha = 0.2f),
                topLeft = Offset(startX, 0f),
                size = Size(barWidth, size.height),
                cornerRadius = cornerRadius
            )

            // Draw filled bar
            drawRoundRect(
                color = color,
                topLeft = Offset(startX, startY),
                size = Size(barWidth, barHeight),
                cornerRadius = cornerRadius
            )
        }
        
        // Draw baseline
        drawLine(
            color = Color.DarkGray.copy(alpha = 0.5f),
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = 2.dp.toPx()
        )
    }
}

@Composable
fun CircularGauge(
    progress: Float, // 0f to 1f
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFE91E63), // Pink/Red by default
    content: @Composable () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(500),
        label = "gauge_progress"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 20.dp.toPx()
            val startAngle = 135f
            val maxSweepAngle = 270f
            
            // Background track
            drawArc(
                color = Color.DarkGray.copy(alpha = 0.3f),
                startAngle = startAngle,
                sweepAngle = maxSweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            
            // Progress track
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = maxSweepAngle * animatedProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            
            // Draw scale ticks along the outer edge
            val tickCount = 40
            val tickRadius = size.width / 2f + 16.dp.toPx() // Slightly outside the main arc
            val tickLength = 6.dp.toPx()
            
            for (i in 0..tickCount) {
                val tickAngle = startAngle + (i * maxSweepAngle / tickCount)
                val angleRad = Math.toRadians(tickAngle.toDouble())
                
                val startX = center.x + (tickRadius - tickLength) * Math.cos(angleRad).toFloat()
                val startY = center.y + (tickRadius - tickLength) * Math.sin(angleRad).toFloat()
                
                val endX = center.x + tickRadius * Math.cos(angleRad).toFloat()
                val endY = center.y + tickRadius * Math.sin(angleRad).toFloat()
                
                val isMajorTick = i % 5 == 0
                
                drawLine(
                    color = if (isMajorTick) Color.Gray else Color.DarkGray,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = if (isMajorTick) 3.dp.toPx() else 1.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        
        content()
    }
}
