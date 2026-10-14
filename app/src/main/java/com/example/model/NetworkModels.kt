package com.example.model

import android.net.Network

enum class InterfaceType(val displayName: String) {
    WIFI("Wi-Fi"),
    SIM_1("Cellular SIM 1"),
    SIM_2("Cellular SIM 2"),
    ETHERNET("Ethernet"),
    UNKNOWN("Network")
}

data class NetworkInterfaceInfo(
    val id: String,
    val type: InterfaceType,
    val name: String,
    val network: Network? = null,
    val isConnected: Boolean = false,
    val isMetered: Boolean = false,
    val ipAddress: String = "Not Assigned",
    val carrierOrSsid: String = "Unknown",
    val signalStrengthDbm: Int = -65,
    val linkDownstreamKbps: Int = 0,
    val linkUpstreamKbps: Int = 0,
    val currentSpeedMbps: Double = 0.0,
    val latencyMs: Long = 0L,
    val slotIndex: Int? = null,
    val subscriptionId: Int? = null,
    val networkGeneration: String = "4G/5G/Wi-Fi",
    val isBonded: Boolean = true
)

enum class BondingMode(val title: String, val subtitle: String) {
    SPEED_AGGREGATION(
        "Bandwidth Aggregation",
        "Stripes download chunks across Wi-Fi & SIM data concurrently for max speed"
    ),
    REDUNDANT_RACE(
        "Zero-Latency Redundancy",
        "Duplicates packets over all paths; first arrival wins for gaming & calls"
    ),
    DYNAMIC_BALANCED(
        "Smart Load Balancing",
        "Routes requests dynamically based on real-time link latency and stability"
    )
}

data class SpeedTestMetrics(
    val isRunning: Boolean = false,
    val progress: Float = 0f, // 0..1
    val wifiSpeedMbps: Double = 0.0,
    val sim1SpeedMbps: Double = 0.0,
    val sim2SpeedMbps: Double = 0.0,
    val bondedSpeedMbps: Double = 0.0,
    val wifiLatencyMs: Long = 0L,
    val sim1LatencyMs: Long = 0L,
    val sim2LatencyMs: Long = 0L,
    val bondedLatencyMs: Long = 0L,
    val stage: String = "Idle"
)

data class DownloadChunk(
    val chunkIndex: Int,
    val interfaceType: InterfaceType,
    val startByte: Long,
    val endByte: Long,
    val downloadedBytes: Long,
    val isCompleted: Boolean = false
)

data class BondedDownloadState(
    val isDownloading: Boolean = false,
    val url: String = "",
    val fileName: String = "",
    val totalBytes: Long = 0L,
    val downloadedBytes: Long = 0L,
    val currentSpeedMbps: Double = 0.0,
    val chunks: List<DownloadChunk> = emptyList(),
    val completedSuccessfully: Boolean = false,
    val errorMessage: String? = null
)
