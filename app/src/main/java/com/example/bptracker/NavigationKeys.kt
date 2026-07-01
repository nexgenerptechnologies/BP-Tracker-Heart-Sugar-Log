package com.example.bptracker

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Main : NavKey
@Serializable data object AddBp : NavKey
@Serializable data object AddSugar : NavKey
@Serializable data object AddHeart : NavKey
@Serializable data class History(val initialFilter: String = "All") : NavKey
@Serializable data class AddHistoricalRecord(val type: String) : NavKey
@Serializable data object PrivacyPolicy : NavKey
@Serializable data object TermsOfUse : NavKey
@Serializable data object Bmi : NavKey
@Serializable data object Welcome : NavKey
@Serializable data object HeartStats : NavKey
@Serializable data object BpStats : NavKey
@Serializable data object StepsStats : NavKey
