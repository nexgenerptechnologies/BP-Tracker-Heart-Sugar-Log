package com.example.bptracker.data

import kotlinx.serialization.Serializable

/**
 * Represents a single health log entry.
 */
@Serializable
data class VitalRecord(
    val id: String = java.util.UUID.randomUUID().toString(),
    val profileId: String = "default",
    val timestamp: Long = System.currentTimeMillis(),
    val systolic: Int = 0,
    val diastolic: Int = 0,
    val heartRate: Int = 0,
    val bloodSugar: Int = 0, // General sugar level
    val bloodSugarPP: Int = 0, // Kept for backwards compatibility
    val medications: String = "", // Tracks pills taken during this log
    val posture: String = "", // Sitting, Standing, Lying Down
    val arm: String = "", // Left, Right
    val tags: String = "", // e.g. "Stressed", "After Exercise"
    val sugarContext: String = "", // Fasting, Before Meal, After Meal
    val symptoms: String = "", // Dizzy, Thirsty
    val heartRateState: String = "" // Resting, Active
) {
    fun getBpStatus(): Pair<String, Long> {
        if (systolic == 0 || diastolic == 0) return Pair("No Data", 0xFF888888)
        return when {
            systolic < 120 && diastolic < 80 -> Pair("Normal", 0xFF00D084)
            systolic in 120..129 && diastolic < 80 -> Pair("Elevated", 0xFFFFD700)
            systolic in 130..139 || diastolic in 80..89 -> Pair("High BP (Stage 1)", 0xFFFF8C00)
            systolic >= 140 || diastolic >= 90 -> Pair("High BP (Stage 2)", 0xFFFF4500)
            else -> Pair("Consult Doctor", 0xFFFF0000)
        }
    }

    fun getBloodSugarStatus(): Pair<String, Long> {
        if (bloodSugar == 0) return Pair("No Data", 0xFF888888)
        return when {
            bloodSugar < 70 -> Pair("Low", 0xFF2D72FF)
            bloodSugar in 70..99 -> Pair("Normal", 0xFF00D084)
            bloodSugar in 100..125 -> Pair("Prediabetes", 0xFFFFD700)
            else -> Pair("Diabetes", 0xFFFF4500)
        }
    }

    fun getBloodSugarPPStatus(): Pair<String, Long> {
        if (bloodSugarPP == 0) return Pair("No Data", 0xFF888888)
        return when {
            bloodSugarPP < 70 -> Pair("Low", 0xFF2D72FF)
            bloodSugarPP in 70..139 -> Pair("Normal", 0xFF00D084)
            bloodSugarPP in 140..199 -> Pair("Prediabetes", 0xFFFFD700)
            else -> Pair("Diabetes", 0xFFFF4500)
        }
    }
}
