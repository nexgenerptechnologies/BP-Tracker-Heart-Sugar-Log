package com.example.bptracker.ui.main

import android.app.TimePickerDialog
import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.launch
import com.example.bptracker.AppContainer
import com.example.bptracker.data.VitalRecord
import com.example.bptracker.theme.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVitalScreen(onSave: () -> Unit, onCancel: () -> Unit) {
    var systolic by remember { mutableIntStateOf(120) }
    var diastolic by remember { mutableIntStateOf(80) }
    var bloodSugar by remember { mutableIntStateOf(0) }
    var bloodSugarPP by remember { mutableIntStateOf(0) }
    
    val calendar = remember { Calendar.getInstance() }
    var selectedTimeMs by remember { mutableLongStateOf(calendar.timeInMillis) }
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val timeFormat = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            selectedTimeMs = calendar.timeInMillis
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = com.example.bptracker.R.drawable.premium_dark_bg),
                contentScale = ContentScale.Crop
            )
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "New Record",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)
        )
        
        // Time Picker Button
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).clickable { timePickerDialog.show() },
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Date & Time", color = TextSecondary)
                Text(timeFormat.format(selectedTimeMs), color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        // Live Status Bar
        val tempRecord = VitalRecord(systolic = systolic, diastolic = diastolic)
        val status = tempRecord.getBpStatus()
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(status.second).copy(alpha = 0.2f)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.Center) {
                Text("Status: ", color = Color.White)
                Text(status.first, color = Color(status.second), fontWeight = FontWeight.Bold)
            }
        }

        // Wheel Pickers for BP
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text("Systolic", color = TextSecondary)
                    Text("Diastolic", color = TextSecondary)
                }
                Row(modifier = Modifier.fillMaxWidth().height(150.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    NativeNumberPicker(value = systolic, range = 40..250) { systolic = it }
                    NativeNumberPicker(value = diastolic, range = 40..150) { diastolic = it }
                }
            }
        }

        // Wheel Pickers for Sugar
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text("Fasting Sugar", color = TextSecondary)
                    Text("PP Sugar", color = TextSecondary)
                }
                Row(modifier = Modifier.fillMaxWidth().height(150.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    NativeNumberPicker(value = bloodSugar, range = 0..300) { bloodSugar = it }
                    NativeNumberPicker(value = bloodSugarPP, range = 0..300) { bloodSugarPP = it }
                }
            }
        }

        // Medication Tracker
        var medications by remember { mutableStateOf("") }
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Medications Taken (Optional)", color = TextSecondary, modifier = Modifier.padding(bottom = 8.dp))
                OutlinedTextField(
                    value = medications,
                    onValueChange = { medications = it },
                    placeholder = { Text("e.g. Lisinopril 10mg, Metformin", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        }

        Button(
            onClick = {
                val record = VitalRecord(
                    timestamp = selectedTimeMs,
                    systolic = systolic,
                    diastolic = diastolic,
                    bloodSugar = if (bloodSugar > 0) bloodSugar else 0,
                    bloodSugarPP = if (bloodSugarPP > 0) bloodSugarPP else 0,
                    medications = medications.trim()
                )
                coroutineScope.launch {
                    AppContainer.repository.addRecord(record)
                    onSave()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text("Save Entry", style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = onCancel) {
            Text("Cancel", color = TextSecondary)
        }
    }
}

@Composable
fun NativeNumberPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                minValue = range.first
                maxValue = range.last
                this.value = value
                wrapSelectorWheel = true
                
                // Style for Dark Mode (Requires Reflection or Theme styling, but standard NumberPicker inherits context theme)
                setOnValueChangedListener { _, _, newVal ->
                    onValueChange(newVal)
                }
            }
        },
        update = { picker ->
            picker.value = value
        },
        modifier = Modifier.width(80.dp)
    )
}
