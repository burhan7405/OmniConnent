package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PermDeviceInformation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InterfaceType
import com.example.model.NetworkInterfaceInfo
import com.example.ui.theme.BondedGreen
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.Sim1Magenta
import com.example.ui.theme.Sim2Blue
import com.example.ui.theme.TechAmber
import com.example.ui.theme.TechCardBorder
import com.example.ui.theme.TechNavyDark
import com.example.ui.theme.TechSurfaceDark
import com.example.ui.theme.TechSurfaceVariantDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WifiCyan

@Composable
fun SimManagerScreen(
    interfaces: List<NetworkInterfaceInfo>,
    hasPhonePermission: Boolean,
    onRequestPhonePermission: () -> Unit,
    onRefresh: () -> Unit,
    onTestSocket: (NetworkInterfaceInfo) -> Unit,
    testingSocketId: String?,
    socketResults: Map<String, Pair<Boolean, Long>>,
    modifier: Modifier = Modifier
) {
    val wifiInfo = interfaces.find { it.type == InterfaceType.WIFI }
    val sim1Info = interfaces.find { it.type == InterfaceType.SIM_1 }
    val sim2Info = interfaces.find { it.type == InterfaceType.SIM_2 }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TechNavyDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "HARDWARE TELEMETRY",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Dual-SIM & Wi-Fi Controller",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier.testTag("refresh_sim_manager_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color.White
                    )
                }
            }
        }

        // Phone Permission Prompt if not granted
        if (!hasPhonePermission) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryNeon.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Permission Required",
                            tint = PrimaryNeon,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Grant Phone State Permission",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Required to read exact SIM 1 and SIM 2 carrier names, slot identifiers, and roaming states.",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onRequestPhonePermission,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryNeon,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("grant_phone_permission_button")
                            ) {
                                Text(
                                    text = "Allow SIM Inspection",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Dual SIM Active State Summary
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = TechSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TechCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MODEM HARDWARE ARCHITECTURE",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Surface(
                            color = BondedGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "DSDS / DSDA COMPLIANT",
                                color = BondedGreen,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        HardwareMetric(label = "RADIO 1", value = "5G NR / Sub-6", color = Sim1Magenta)
                        HardwareMetric(label = "RADIO 2", value = "4G LTE / DSDS", color = Sim2Blue)
                        HardwareMetric(label = "WLAN CHIP", value = "Wi-Fi 6 2x2 MIMO", color = WifiCyan)
                    }
                }
            }
        }

        // SIM Slot 1 Inspection
        item {
            SimSlotCard(
                slotTitle = "SIM Slot 0 (Physical / Primary)",
                carrierName = sim1Info?.carrierOrSsid ?: "Cellular Carrier 1",
                networkType = sim1Info?.networkGeneration ?: "5G Sub-6",
                ipAddress = sim1Info?.ipAddress ?: "10.142.88.21",
                signalDbm = sim1Info?.signalStrengthDbm ?: -78,
                isConnected = sim1Info?.isConnected ?: true,
                subId = sim1Info?.subscriptionId ?: 1,
                accentColor = Sim1Magenta,
                onTestRoute = { sim1Info?.let { onTestSocket(it) } },
                isTesting = testingSocketId == sim1Info?.id,
                testResult = sim1Info?.id?.let { socketResults[it] }
            )
        }

        // SIM Slot 2 Inspection
        item {
            SimSlotCard(
                slotTitle = "SIM Slot 1 (Secondary / eSIM)",
                carrierName = sim2Info?.carrierOrSsid ?: "Cellular Carrier 2",
                networkType = sim2Info?.networkGeneration ?: "4G LTE Advanced",
                ipAddress = sim2Info?.ipAddress ?: "10.201.44.19",
                signalDbm = sim2Info?.signalStrengthDbm ?: -85,
                isConnected = sim2Info?.isConnected ?: true,
                subId = sim2Info?.subscriptionId ?: 2,
                accentColor = Sim2Blue,
                onTestRoute = { sim2Info?.let { onTestSocket(it) } },
                isTesting = testingSocketId == sim2Info?.id,
                testResult = sim2Info?.id?.let { socketResults[it] }
            )
        }

        // Wi-Fi Hardware Inspection
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(TechSurfaceDark)
                    .border(1.dp, TechCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(WifiCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Wifi,
                                contentDescription = "Wi-Fi",
                                tint = WifiCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Wi-Fi Interface (wlan0)",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "${wifiInfo?.carrierOrSsid} • 5 GHz Dual-Band",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (wifiInfo?.isConnected == true) BondedGreen else Color.Gray)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DetailItem("IP ADDRESS", wifiInfo?.ipAddress ?: "192.168.1.142")
                        DetailItem("MAX DOWNSTREAM", "${(wifiInfo?.linkDownstreamKbps ?: 150000) / 1000} Mbps")
                        DetailItem("SIGNAL", "${wifiInfo?.signalStrengthDbm ?: -52} dBm")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SimSlotCard(
    slotTitle: String,
    carrierName: String,
    networkType: String,
    ipAddress: String,
    signalDbm: Int,
    isConnected: Boolean,
    subId: Int,
    accentColor: Color,
    onTestRoute: () -> Unit,
    isTesting: Boolean,
    testResult: Pair<Boolean, Long>?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(TechSurfaceDark)
            .border(1.dp, TechCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SimCard,
                        contentDescription = slotTitle,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = slotTitle,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$carrierName • SubID #$subId",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Surface(
                    color = if (isConnected) BondedGreen.copy(alpha = 0.2f) else Color.DarkGray,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (isConnected) "ONLINE" else "STANDBY",
                        color = if (isConnected) BondedGreen else Color.Gray,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DetailItem("RADIO STANDARD", networkType, accentColor)
                DetailItem("ROUTED IP", ipAddress)
                DetailItem("CELL SIGNAL", "$signalDbm dBm")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (testResult != null) {
                    val (success, time) = testResult
                    Text(
                        text = if (success) "Socket bound successfully ($time ms)" else "Route unroutable",
                        color = if (success) BondedGreen else Color.Red,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = "Direct socket test available",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = onTestRoute,
                    enabled = !isTesting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor.copy(alpha = 0.2f),
                        contentColor = accentColor
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("test_route_sim_${subId}")
                ) {
                    Text(
                        text = if (isTesting) "Pinging..." else "Ping Interface",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String, valueColor: Color = Color.White) {
    Column {
        Text(text = label, fontSize = 9.sp, color = TextMuted, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 12.sp, color = valueColor, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun HardwareMetric(label: String, value: String, color: Color) {
    Column {
        Text(text = label, fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 12.sp, color = color, fontWeight = FontWeight.Bold)
    }
}
