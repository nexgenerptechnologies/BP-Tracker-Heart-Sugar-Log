package com.example.bptracker

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.bptracker.ui.main.MainScreen

@Composable
fun MainNavigation(isFirstLaunch: Boolean, startDestination: String? = null) {
  val backStack = rememberNavBackStack(if (isFirstLaunch) Welcome else Main)

  androidx.compose.runtime.LaunchedEffect(startDestination) {
      if (startDestination == "add_bp") backStack.add(AddBp)
      if (startDestination == "add_hr") backStack.add(AddHeart)
      if (startDestination == "add_sugar") backStack.add(AddSugar)
  }

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<Welcome> {
          com.example.bptracker.ui.main.WelcomeScreen(onFinish = {
              backStack.add(Main)
              backStack.remove(Welcome)
          })
        }
        entry<Main> {
          MainScreen(
            startDestination = startDestination,
            onNavigateToAddBp = { backStack.add(BpStats) },
            onNavigateToAddSugar = { backStack.add(AddSugar) },
            onNavigateToAddHeart = { backStack.add(HeartStats) },
            onNavigateToBmi = { backStack.add(Bmi) },
            onNavigateToPrivacy = { backStack.add(PrivacyPolicy) },
            onNavigateToTerms = { backStack.add(TermsOfUse) }
          )
        }
        entry<AddBp> {
          com.example.bptracker.ui.main.AddBpScreen(onSave = { backStack.removeLastOrNull() }, onCancel = { backStack.removeLastOrNull() })
        }
        entry<BpStats> {
          com.example.bptracker.ui.statistics.BpStatisticsScreen(
              onBack = { backStack.removeLastOrNull() },
              onMeasureNow = { backStack.add(AddBp) },
              onViewAll = { backStack.add(History(initialFilter = "Blood Pressure")) }
          )
        }
        entry<AddSugar> {
          com.example.bptracker.ui.main.AddSugarScreen(onSave = { backStack.removeLastOrNull() }, onCancel = { backStack.removeLastOrNull() })
        }
        entry<AddHeart> {
          com.example.bptracker.ui.main.MeasureHeartRateScreen(onBack = { backStack.removeLastOrNull() })
        }
        entry<HeartStats> {
          com.example.bptracker.ui.statistics.HeartRateStatisticsScreen(
              onBack = { backStack.removeLastOrNull() },
              onMeasureNow = { backStack.add(AddHeart) },
              onViewAll = { backStack.add(History(initialFilter = "Heart Rate")) }
          )
        }
        entry<StepsStats> {
          com.example.bptracker.ui.statistics.StepsStatisticsScreen(
              onBack = { backStack.removeLastOrNull() },
              onViewAll = { backStack.add(History(initialFilter = "All")) }
          )
        }
        entry<History> {
          val filter = it.initialFilter
          com.example.bptracker.ui.main.HistoryScreen(
              onBack = { backStack.removeLastOrNull() },
              onAddRecord = { type -> backStack.add(AddHistoricalRecord(type)) },
              initialFilter = filter
          )
        }
        entry<Bmi> {
          com.example.bptracker.ui.main.BmiCalculatorScreen(onBack = { backStack.removeLastOrNull() })
        }
        entry<AddHistoricalRecord> {
          val type = it.type
          com.example.bptracker.ui.main.AddHistoricalRecordScreen(recordType = type, onBack = { backStack.removeLastOrNull() })
        }
        entry<PrivacyPolicy> {
          com.example.bptracker.ui.main.PrivacyPolicyScreen(onNavigateBack = { backStack.removeLastOrNull() })
        }
        entry<TermsOfUse> {
          com.example.bptracker.ui.main.TermsOfUseScreen(onNavigateBack = { backStack.removeLastOrNull() })
        }
      },
  )
}
