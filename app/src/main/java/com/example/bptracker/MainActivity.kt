package com.example.bptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.bptracker.theme.BPTrackerTheme
import com.example.bptracker.data.FileStorageRepository

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.bptracker.data.AppPreferences
import java.util.Locale

object AppContainer {
    lateinit var repository: FileStorageRepository
    lateinit var preferences: AppPreferences
}

class MainActivity : ComponentActivity() {
  override fun attachBaseContext(newBase: Context) {
      val prefs = newBase.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
      val lang = prefs.getString("app_language", "default") ?: "default"
      val locale = if (lang == "default") Locale.getDefault() else Locale(lang)
      Locale.setDefault(locale)
      val config = Configuration(newBase.resources.configuration)
      config.setLocale(locale)
      super.attachBaseContext(newBase.createConfigurationContext(config))
  }

  private val currentDestination = androidx.compose.runtime.mutableStateOf<String?>(null)

  override fun onNewIntent(intent: android.content.Intent) {
      super.onNewIntent(intent)
      intent.getStringExtra("START_DESTINATION")?.let {
          currentDestination.value = it
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AppContainer.repository = FileStorageRepository(this)
    AppContainer.preferences = AppPreferences(this)

    intent.getStringExtra("START_DESTINATION")?.let {
        currentDestination.value = it
    }

    enableEdgeToEdge()
    setContent {
        val isFirstLaunch by AppContainer.preferences.isFirstLaunch.collectAsState()

        BPTrackerTheme { 
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { 
                MainNavigation(isFirstLaunch = isFirstLaunch, startDestination = currentDestination.value) 
                
                // Reset state after consuming
                androidx.compose.runtime.LaunchedEffect(currentDestination.value) {
                    if (currentDestination.value != null) {
                        kotlinx.coroutines.delay(500)
                        currentDestination.value = null
                    }
                }
            } 
        }
    }
  }
}
