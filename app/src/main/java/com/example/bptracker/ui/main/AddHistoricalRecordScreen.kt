package com.example.bptracker.ui.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bptracker.AppContainer
import com.example.bptracker.data.StepLog
import com.example.bptracker.data.WaterLog
import com.example.bptracker.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHistoricalRecordScreen(recordType: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val activeProfileId by AppContainer.repository.activeProfileId.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    var valueInput by remember { mutableStateOf("") }
    
    // Simple Date selection logic
    val calendar = Calendar.getInstance()
    var selectedDate by remember { mutableStateOf(calendar.time) }
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E213A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark)) {
                Text("Cancel", color = Color.White)
            }
            Text(
                text = "Add $recordType",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = {
                    val value = valueInput.toIntOrNull() ?: 0
                    if (value <= 0) {
                        Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    scope.launch {
                        if (recordType == "Hydration") {
                            AppContainer.repository.addWaterLog(
                                WaterLog(
                                    profileId = activeProfileId,
                                    timestamp = selectedDate.time,
                                    amountMl = value
                                )
                            )
                        } else if (recordType == "Daily Steps") {
                            AppContainer.repository.addStepLog(
                                StepLog(
                                    profileId = activeProfileId,
                                    timestamp = selectedDate.time,
                                    steps = value
                                )
                            )
                        }
                        Toast.makeText(context, "Record added", Toast.LENGTH_SHORT).show()
                        onBack()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Save", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Date", color = TextSecondary, style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(8.dp))
                
                // Simple Date adjusters for demo purposes (A real app might use a DatePickerDialog)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        val cal = Calendar.getInstance()
                        cal.time = selectedDate
                        cal.add(Calendar.DAY_OF_YEAR, -1)
                        selectedDate = cal.time
                    }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2E4D))) {
                        Text("< Prev Day", color = Color.White)
                    }
                    
                    Text(sdf.format(selectedDate), color = Color.White, fontWeight = FontWeight.Bold)
                    
                    Button(onClick = {
                        val cal = Calendar.getInstance()
                        cal.time = selectedDate
                        cal.add(Calendar.DAY_OF_YEAR, 1)
                        if (cal.timeInMillis <= System.currentTimeMillis()) {
                            selectedDate = cal.time
                        } else {
                            Toast.makeText(context, "Cannot select future date", Toast.LENGTH_SHORT).show()
                        }
                    }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2E4D))) {
                        Text("Next Day >", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = if (recordType == "Hydration") "Amount (ml)" else "Total Steps",
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = valueInput,
                    onValueChange = { if (it.length <= 6) valueInput = it.filter { char -> char.isDigit() } },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Color(0xFF3B4168),
                        focusedContainerColor = Color(0xFF2A2E4D),
                        unfocusedContainerColor = Color(0xFF2A2E4D)
                    ),
                    placeholder = { 
                        Text(if (recordType == "Hydration") "e.g., 250" else "e.g., 8500", color = Color.Gray) 
                    }
                )
            }
        }
    }
}
