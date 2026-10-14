package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.BondingMode
import com.example.model.NetworkInterfaceInfo
import com.example.ui.components.InterfaceCard
import com.example.ui.components.MultiPipeVisualizer
import com.example.ui.components.SpeedometerGauge
import com.example.ui.theme.BondedGreen
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.TechAmber
import com.example.ui.theme.TechCardBorder
import com.example.ui.theme.TechNavyDark
import com.example.ui.theme.TechSurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun DashboardScreen(
    interfaces: List<NetworkInterfaceInfo>,
    isBondingEnabled: Boolean,
    bondingMode: BondingMode,
    aggregatedSpeed: Double,
    totalBytes: Long,
    isMobileDataAlwaysActive: Boolean,
    socketResults: Map<String, Pair<Boolean, Long>>,
    testingSocketId: String?,
    onToggleBonding: (Boolean) -> Unit,
    onSelectBondingMode: (BondingMode) -> Unit,
    onTestSocket: (NetworkInterfaceInfo) -> Unit,
    onRefresh: () -> Unit,
    onOpenDeveloperSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TechNavyDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Banner with Title & Quick Status
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, TechCardBorder, RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.omnibond_hero),
                    contentDescription = "OmniBond Hero Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xDD0B111E)),
                                startY = 40f
                            )
                        )
                )

                // Text overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = if (isBondingEnabled) BondedGreen else Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (isBondingEnabled) "MULTI-LINK ACTIVE" else "STANDBY",
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${interfaces.count { it.isConnected }} Channels Online",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Concurrent Wi-Fi + Dual SIM Bonding",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                // Refresh Button
                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .testTag("refresh_dashboard_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Networks",
                        tint = Color.White
                    )
                }
            }
        }

        // System Developer Setting Alert (if Mobile Data Always Active is off)
        if (!isMobileDataAlwaysActive) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF26190B)),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TechAmber.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenDeveloperSettings() }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Notice",
                            tint = TechAmber,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Keep Mobile Data Active on Wi-Fi",
                                color = TechAmber,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tap to open Developer Options & enable 'Mobile data always active' for full concurrent speed.",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Live Multi-Pipe Visualizer
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CONCURRENT PIPES",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isBondingEnabled) "Bonding On" else "Bonding Off",
                            color = if (isBondingEnabled) BondedGreen else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = isBondingEnabled,
                            onCheckedChange = onToggleBonding,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = BondedGreen,
                                uncheckedThumbColor = Color.LightGray,
                                uncheckedTrackColor = Color.DarkGray
                            ),
                            modifier = Modifier.testTag("toggle_bonding_switch")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                MultiPipeVisualizer(
                    interfaces = interfaces,
                    isBondingEnabled = isBondingEnabled,
                    aggregatedSpeed = aggregatedSpeed
                )
            }
        }

        // Speedometer Gauge
        item {
            SpeedometerGauge(
                speedMbps = aggregatedSpeed,
                maxSpeedMbps = 120.0,
                title = "Aggregated Real-Time Throughput",
                latencyMs = interfaces.minOfOrNull { if (it.isConnected) it.latencyMs else 999 } ?: 18L,
                efficiencyPercent = if (isBondingEnabled) 94 else 0
            )
        }

        // Bonding Mode Selection
        item {
            Column {
                Text(
                    text = "BONDING ALGORITHM",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BondingMode.entries.forEach { mode ->
                        val isSelected = mode == bondingMode
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) PrimaryNeon.copy(alpha = 0.2f) else TechSurfaceDark)
                                .border(
                                    1.dp,
                                    if (isSelected) PrimaryNeon else TechCardBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onSelectBondingMode(mode) }
                                .padding(10.dp)
                                .testTag("bonding_mode_${mode.name}")
                        ) {
                            Column {
                                Icon(
                                    imageVector = when (mode) {
                                        BondingMode.SPEED_AGGREGATION -> Icons.Default.Bolt
                                        BondingMode.REDUNDANT_RACE -> Icons.Default.Layers
                                        BondingMode.DYNAMIC_BALANCED -> Icons.Default.CheckCircle
                                    },
                                    contentDescription = mode.title,
                                    tint = if (isSelected) PrimaryNeon else TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = mode.title.split(" ").first(),
                                    color = if (isSelected) Color.White else TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (mode == BondingMode.SPEED_AGGREGATION) "Max Speed" else if (mode == BondingMode.REDUNDANT_RACE) "0ms Loss" else "Balanced",
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Connected Physical Interfaces List
        item {
            Text(
                text = "LINK INTERFACES (${interfaces.size})",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        items(interfaces.size) { index ->
            val info = interfaces[index]
            InterfaceCard(
                info = info,
                onTestSocket = { onTestSocket(info) },
                isTesting = testingSocketId == info.id,
                testResult = socketResults[info.id]
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
