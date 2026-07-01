package com.example.bptracker.utils

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

class StepCounterManager(private val context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    
    private val prefs: SharedPreferences = context.getSharedPreferences("StepPrefs", Context.MODE_PRIVATE)
    
    private val _currentSteps = MutableStateFlow(0)
    val currentSteps: StateFlow<Int> = _currentSteps

    private var initialStepsForDay: Float = -1f
    private var lastRecordedDay: Int = -1

    fun startListening() {
        if (stepSensor != null) {
            val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            lastRecordedDay = prefs.getInt("lastRecordedDay", -1)
            
            if (lastRecordedDay != currentDay) {
                // It's a new day, we need to reset our offset
                initialStepsForDay = -1f 
                prefs.edit().putInt("lastRecordedDay", currentDay).apply()
            } else {
                initialStepsForDay = prefs.getFloat("initialStepsForDay", -1f)
            }
            
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val totalStepsSinceBoot = event.values[0]
            
            if (initialStepsForDay < 0) {
                initialStepsForDay = totalStepsSinceBoot
                prefs.edit().putFloat("initialStepsForDay", initialStepsForDay).apply()
            }
            
            val todaySteps = (totalStepsSinceBoot - initialStepsForDay).toInt()
            if (todaySteps >= 0) {
                _currentSteps.value = todaySteps
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }
}
