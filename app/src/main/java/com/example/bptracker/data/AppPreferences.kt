package com.example.bptracker.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _isFirstLaunch = MutableStateFlow(prefs.getBoolean("is_first_launch", true))
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    private val _currentLanguage = MutableStateFlow(prefs.getString("app_language", "default") ?: "default")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    fun setFirstLaunchCompleted() {
        prefs.edit().putBoolean("is_first_launch", false).apply()
        _isFirstLaunch.value = false
    }

    fun setLanguage(languageCode: String) {
        prefs.edit().putString("app_language", languageCode).apply()
        _currentLanguage.value = languageCode
    }
}
