package com.example.bptracker.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class FileStorageRepository(private val context: Context) {
    private val vitalsFile = File(context.filesDir, "vitals_data.json")
    private val profilesFile = File(context.filesDir, "profiles.json")
    private val waterFile = File(context.filesDir, "water_logs.json")
    private val medsFile = File(context.filesDir, "meds.json")
    private val medLogsFile = File(context.filesDir, "med_logs.json")
    private val stepsFile = File(context.filesDir, "step_logs.json")
    
    private val _vitals = MutableStateFlow<List<VitalRecord>>(emptyList())
    val vitals: StateFlow<List<VitalRecord>> = _vitals.asStateFlow()
    
    private val _profiles = MutableStateFlow<List<Profile>>(emptyList())
    val profiles: StateFlow<List<Profile>> = _profiles.asStateFlow()
    
    private val _waterLogs = MutableStateFlow<List<WaterLog>>(emptyList())
    val waterLogs: StateFlow<List<WaterLog>> = _waterLogs.asStateFlow()
    
    private val _medications = MutableStateFlow<List<Medication>>(emptyList())
    val medications: StateFlow<List<Medication>> = _medications.asStateFlow()
    
    private val _medicationLogs = MutableStateFlow<List<MedicationLog>>(emptyList())
    val medicationLogs: StateFlow<List<MedicationLog>> = _medicationLogs.asStateFlow()
    
    private val _stepLogs = MutableStateFlow<List<StepLog>>(emptyList())
    val stepLogs: StateFlow<List<StepLog>> = _stepLogs.asStateFlow()
    
    private val _activeProfileId = MutableStateFlow("default")
    val activeProfileId: StateFlow<String> = _activeProfileId.asStateFlow()

    init {
        loadData()
    }
    
    fun setActiveProfile(profileId: String) {
        _activeProfileId.value = profileId
    }

    private fun loadData() {
        if (vitalsFile.exists()) {
            try { _vitals.value = Json.decodeFromString<List<VitalRecord>>(vitalsFile.readText()) } catch (e: Exception) { e.printStackTrace() }
        }
        
        if (profilesFile.exists()) {
            try {
                val list = Json.decodeFromString<List<Profile>>(profilesFile.readText())
                _profiles.value = list
                if (list.isNotEmpty()) {
                    _activeProfileId.value = list.firstOrNull { it.isPrimary }?.id ?: list.first().id
                }
            } catch (e: Exception) { e.printStackTrace() }
        } else {
            val defaultProfile = Profile(id = "default", name = "My Vitals", isPrimary = true)
            _profiles.value = listOf(defaultProfile)
            _activeProfileId.value = "default"
            saveProfiles(_profiles.value)
        }
        
        if (waterFile.exists()) {
            try { _waterLogs.value = Json.decodeFromString<List<WaterLog>>(waterFile.readText()) } catch (e: Exception) { e.printStackTrace() }
        }
        
        if (medsFile.exists()) {
            try { _medications.value = Json.decodeFromString<List<Medication>>(medsFile.readText()) } catch (e: Exception) { e.printStackTrace() }
        }
        
        if (medLogsFile.exists()) {
            try { _medicationLogs.value = Json.decodeFromString<List<MedicationLog>>(medLogsFile.readText()) } catch (e: Exception) { e.printStackTrace() }
        }
        
        if (stepsFile.exists()) {
            try { _stepLogs.value = Json.decodeFromString<List<StepLog>>(stepsFile.readText()) } catch (e: Exception) { e.printStackTrace() }
        }
    }

    suspend fun addRecord(record: VitalRecord) {
        withContext(Dispatchers.IO) {
            val currentList = _vitals.value.toMutableList()
            currentList.add(0, record)
            _vitals.value = currentList
            saveVitals(currentList)
        }
    }
    
    suspend fun addProfile(profile: Profile) {
        withContext(Dispatchers.IO) {
            val currentList = _profiles.value.toMutableList()
            currentList.add(profile)
            _profiles.value = currentList
            saveProfiles(currentList)
        }
    }
    
    suspend fun updateProfileName(profileId: String, newName: String) {
        withContext(Dispatchers.IO) {
            val currentList = _profiles.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == profileId }
            if (index != -1) {
                currentList[index] = currentList[index].copy(name = newName)
                _profiles.value = currentList
                saveProfiles(currentList)
            }
        }
    }
    
    suspend fun addWaterLog(log: WaterLog) {
        withContext(Dispatchers.IO) {
            val currentList = _waterLogs.value.toMutableList()
            currentList.add(0, log)
            _waterLogs.value = currentList
            saveWaterLogs(currentList)
        }
    }
    
    suspend fun removeLastWaterLog(profileId: String) {
        withContext(Dispatchers.IO) {
            val currentList = _waterLogs.value.toMutableList()
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val todayStr = sdf.format(java.util.Date())
            
            val logToRemove = currentList.firstOrNull { 
                it.profileId == profileId && sdf.format(java.util.Date(it.timestamp)) == todayStr
            }
            
            if (logToRemove != null) {
                currentList.remove(logToRemove)
                _waterLogs.value = currentList
                saveWaterLogs(currentList)
            }
        }
    }
    
    suspend fun addMedication(med: Medication) {
        withContext(Dispatchers.IO) {
            val currentList = _medications.value.toMutableList()
            currentList.add(med)
            _medications.value = currentList
            saveMeds(currentList)
        }
    }
    
    suspend fun updateMedication(med: Medication) {
        withContext(Dispatchers.IO) {
            val currentList = _medications.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == med.id }
            if (index != -1) {
                currentList[index] = med
                _medications.value = currentList
                saveMeds(currentList)
            }
        }
    }
    
    suspend fun deleteMedication(medId: String) {
        withContext(Dispatchers.IO) {
            val currentList = _medications.value.toMutableList()
            currentList.removeAll { it.id == medId }
            _medications.value = currentList
            saveMeds(currentList)
            
            // Also clean up logs
            val currentLogs = _medicationLogs.value.toMutableList()
            currentLogs.removeAll { it.medicationId == medId }
            _medicationLogs.value = currentLogs
            saveMedLogs(currentLogs)
        }
    }
    
    suspend fun toggleMedicationLog(medId: String, profileId: String) {
        withContext(Dispatchers.IO) {
            val currentList = _medicationLogs.value.toMutableList()
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val todayStr = sdf.format(java.util.Date())
            
            val existingLog = currentList.find { 
                it.medicationId == medId && sdf.format(java.util.Date(it.timestamp)) == todayStr
            }
            
            if (existingLog != null) {
                // Untoggle
                currentList.remove(existingLog)
            } else {
                // Toggle
                currentList.add(MedicationLog(medicationId = medId, profileId = profileId, taken = true))
            }
            
            _medicationLogs.value = currentList
            saveMedLogs(currentList)
        }
    }

    suspend fun addStepLog(log: StepLog) {
        withContext(Dispatchers.IO) {
            val currentList = _stepLogs.value.toMutableList()
            currentList.add(0, log)
            _stepLogs.value = currentList
            saveStepLogs(currentList)
        }
    }

    private fun saveVitals(list: List<VitalRecord>) { try { vitalsFile.writeText(Json.encodeToString(list)) } catch (e: Exception) {} }
    private fun saveProfiles(list: List<Profile>) { try { profilesFile.writeText(Json.encodeToString(list)) } catch (e: Exception) {} }
    private fun saveWaterLogs(list: List<WaterLog>) { try { waterFile.writeText(Json.encodeToString(list)) } catch (e: Exception) {} }
    private fun saveMeds(list: List<Medication>) { try { medsFile.writeText(Json.encodeToString(list)) } catch (e: Exception) {} }
    private fun saveMedLogs(list: List<MedicationLog>) { try { medLogsFile.writeText(Json.encodeToString(list)) } catch (e: Exception) {} }
    private fun saveStepLogs(list: List<StepLog>) { try { stepsFile.writeText(Json.encodeToString(list)) } catch (e: Exception) {} }
}
