package com.example.bptracker.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bptracker.AppContainer
import com.example.bptracker.data.VitalRecord
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainScreenViewModel : ViewModel() {
  val uiState: StateFlow<MainScreenUiState> =
    AppContainer.repository.vitals
      .map<List<VitalRecord>, MainScreenUiState> { MainScreenUiState.Success(it) }
      .catch { emit(MainScreenUiState.Error(it)) }
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainScreenUiState.Loading)
}

sealed interface MainScreenUiState {
  object Loading : MainScreenUiState
  data class Error(val throwable: Throwable) : MainScreenUiState
  data class Success(val data: List<VitalRecord>) : MainScreenUiState
}
