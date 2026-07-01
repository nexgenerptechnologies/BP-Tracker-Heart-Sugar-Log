package com.example.bptracker.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bptracker.theme.*
import kotlin.math.pow

@Composable
fun BmiCalculatorScreen(onBack: () -> Unit) {
    var weightStr by remember { mutableStateOf("") }
    var heightStr by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf<Float?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E2235))
            .navigationBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) {
                Text("< Back", color = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("BMI Calculator", color = Color.White, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.weight(1.5f))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Input Area
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                OutlinedTextField(
                    value = weightStr,
                    onValueChange = { weightStr = it },
                    label = { Text("Weight (kg)", color = TextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Color(0xFF333856),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = heightStr,
                    onValueChange = { heightStr = it },
                    label = { Text("Height (cm)", color = TextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Color(0xFF333856),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val weight = weightStr.toFloatOrNull()
                        val heightCm = heightStr.toFloatOrNull()
                        if (weight != null && heightCm != null && heightCm > 0) {
                            val heightM = heightCm / 100
                            bmiResult = weight / heightM.pow(2)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Calculate", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Result Gauge
        bmiResult?.let { bmi ->
            val status = getBmiStatus(bmi)
            val color = getBmiColor(bmi)
            // Normalize BMI for gauge (0 to 1). Let's say 15 is 0 and 40 is 1.
            val progress = ((bmi - 15f) / 25f).coerceIn(0f, 1f)

            com.example.bptracker.ui.components.CircularGauge(
                progress = progress,
                color = color,
                modifier = Modifier.size(240.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = String.format("%.1f", bmi),
                        color = Color.White,
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.background(color.copy(alpha = 0.2f), RoundedCornerShape(8.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                        Text(
                            text = status.uppercase(),
                            color = color,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun getBmiStatus(bmi: Float): String {
    return when {
        bmi < 18.5 -> "Underweight"
        bmi < 25.0 -> "Normal"
        bmi < 30.0 -> "Overweight"
        else -> "Obese"
    }
}

private fun getBmiColor(bmi: Float): Color {
    return when {
        bmi < 18.5 -> Color(0xFF3498DB) // Blue
        bmi < 25.0 -> com.example.bptracker.theme.NormalGreen // Green
        bmi < 30.0 -> com.example.bptracker.theme.ElevatedYellow // Yellow
        else -> com.example.bptracker.theme.HighOrange // Red/Orange
    }
}
