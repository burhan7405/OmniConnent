package com.example.ui

import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.BondedDownloadState
import com.example.model.BondingMode
import com.example.model.NetworkInterfaceInfo
import com.example.model.SpeedTestMetrics
import com.example.network.MultiNetworkBondingManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val bondingManager = MultiNetworkBondingManager(application)

    val interfaces: StateFlow<List<NetworkInterfaceInfo>> = bondingManager.interfaces
    val bondingMode: StateFlow<BondingMode> = bondingManager.bondingMode
    val isBondingEnabled: StateFlow<Boolean> = bondingManager.isBondingEnabled
    val isVirtualSimEmulationEnabled: StateFlow<Boolean> = bondingManager.isVirtualSimEmulationEnabled
    val aggregatedSpeedMbps: StateFlow<Double> = bondingManager.aggregatedSpeedMbps
    val totalBytesAggregated: StateFlow<Long> = bondingManager.totalBytesAggregated
    val speedTestMetrics: StateFlow<SpeedTestMetrics> = bondingManager.speedTestMetrics
    val downloadState: StateFlow<BondedDownloadState> = bondingManager.downloadState
    val isMobileDataAlwaysActive: StateFlow<Boolean> = bondingManager.isMobileDataAlwaysActive

    private val _hasPhonePermission = MutableStateFlow(
        ContextCompat.checkSelfPermission(
            application,
            android.Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    )
    val hasPhonePermission: StateFlow<Boolean> = _hasPhonePermission.asStateFlow()

    private val _socketTestResults = MutableStateFlow<Map<String, Pair<Boolean, Long>>>(emptyMap())
    val socketTestResults: StateFlow<Map<String, Pair<Boolean, Long>>> = _socketTestResults.asStateFlow()

    private val _isTestingSocket = MutableStateFlow<String?>(null)
    val isTestingSocket: StateFlow<String?> = _isTestingSocket.asStateFlow()

    fun updatePermissionStatus(granted: Boolean) {
        _hasPhonePermission.value = granted
        if (granted) {
            bondingManager.refreshInterfaces()
        }
    }

    fun setBondingMode(mode: BondingMode) {
        bondingManager.setBondingMode(mode)
    }

    fun toggleBonding(enabled: Boolean) {
        bondingManager.toggleBonding(enabled)
    }

    fun toggleInterfaceBonding(interfaceId: String) {
        bondingManager.toggleInterfaceBonding(interfaceId)
    }

    fun toggleVirtualSimEmulation(enabled: Boolean) {
        bondingManager.toggleVirtualSimEmulation(enabled)
    }

    fun startSpeedTest() {
        bondingManager.startSpeedTest()
    }

    fun startBondedDownload(url: String = "https://speed.cloudflare.com/__down?bytes=25000000") {
        bondingManager.startBondedDownload(url)
    }

    fun cancelDownload() {
        bondingManager.cancelDownload()
    }

    fun openDeveloperSettings() {
        bondingManager.openDeveloperSettings()
    }

    fun refreshNetworks() {
        bondingManager.checkSystemSettings()
        bondingManager.refreshInterfaces()
    }

    fun testSocket(interfaceInfo: NetworkInterfaceInfo) {
        viewModelScope.launch {
            _isTestingSocket.value = interfaceInfo.id
            val result = bondingManager.testSocketOnInterface(interfaceInfo)
            val currentMap = _socketTestResults.value.toMutableMap()
            currentMap[interfaceInfo.id] = result
            _socketTestResults.value = currentMap
            _isTestingSocket.value = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        bondingManager.onDestroy()
    }
}
