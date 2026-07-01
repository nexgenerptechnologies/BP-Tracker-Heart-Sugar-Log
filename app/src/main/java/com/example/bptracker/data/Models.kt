package com.example.bptracker.data

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "My Vitals",
    val avatarId: Int = 0,
    val isPrimary: Boolean = false
)

@Serializable
data class WaterLog(
    val id: String = java.util.UUID.randomUUID().toString(),
    val profileId: String = "default",
    val timestamp: Long = System.currentTimeMillis(),
    val amountMl: Int = 250 // Amount consumed in ml
)

// The following are for Phase 2, but created now to structure the data layer
@Serializable
data class Medication(
    val id: String = java.util.UUID.randomUUID().toString(),
    val profileId: String = "default",
    val name: String,
    val dosage: String,
    val timeHour: Int,
    val timeMinute: Int
)

@Serializable
data class MedicationLog(
    val id: String = java.util.UUID.randomUUID().toString(),
    val medicationId: String,
    val profileId: String = "default",
    val timestamp: Long = System.currentTimeMillis(),
    val taken: Boolean = false
)

@Serializable
data class StepLog(
    val id: String = java.util.UUID.randomUUID().toString(),
    val profileId: String = "default",
    val timestamp: Long = System.currentTimeMillis(),
    val steps: Int = 0
)
