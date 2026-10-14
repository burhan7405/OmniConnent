package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SpeedTestMetrics
import com.example.ui.theme.BondedGreen
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.Sim1Magenta
import com.example.ui.theme.Sim2Blue
import com.example.ui.theme.TechCardBorder
import com.example.ui.theme.TechNavyDark
import com.example.ui.theme.TechSurfaceDark
import com.example.ui.theme.TechSurfaceVariantDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WifiCyan

@Composable
fun SpeedAggregatorScreen(
    metrics: SpeedTestMetrics,
    onStartSpeedTest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = metrics.progress,
        label = "testProgress"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TechNavyDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "MULTI-LINK BENCHMARK",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Simultaneous Channel Speed Test",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Tests raw throughput across Wi-Fi, SIM 1, and SIM 2 independently, then measures bonded aggregation.",
                color = TextMuted,
                fontSize = 12.sp
            )
        }

        // Test Status & Progress Header
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (metrics.isRunning) PrimaryNeon else BondedGreen)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (metrics.isRunning) "BENCHMARK IN PROGRESS" else "READY TO TEST",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (metrics.isRunning) PrimaryNeon else BondedGreen
                            )
                        }

                        Text(
                            text = "${(animatedProgress * 100).toInt()}%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = BondedGreen,
                        trackColor = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = metrics.stage,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onStartSpeedTest,
                        enabled = !metrics.isRunning,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BondedGreen,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("start_speed_test_button")
                    ) {
                        Icon(
                            imageVector = if (metrics.isRunning) Icons.Default.Speed else Icons.Default.PlayArrow,
                            contentDescription = "Start Test",
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (metrics.isRunning) "Measuring Channels..." else "START MULTI-LINK BENCHMARK",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Bonded Result Spotlight Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            listOf(Color(0xFF0F2B1D), Color(0xFF061A2B))
                        )
                    )
                    .border(1.5.dp, BondedGreen, RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.RocketLaunch,
                                contentDescription = "Bonded Speed",
                                tint = BondedGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "BONDED AGGREGATION SPEED",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Surface(
                            color = BondedGreen,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "3 PATHS CONCURRENT",
                                color = Color.Black,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "${"%.1f".format(metrics.bondedSpeedMbps)}",
                                fontSize = 44.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "Combined Megabits per second",
                                fontSize = 12.sp,
                                color = BondedGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            val maxSingle = maxOf(metrics.wifiSpeedMbps, metrics.sim1SpeedMbps, metrics.sim2SpeedMbps)
                            val multiplier = if (maxSingle > 0) metrics.bondedSpeedMbps / maxSingle else 1.0

                            Text(
                                text = "${"%.1f".format(multiplier)}x",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = PrimaryNeon
                            )
                            Text(
                                text = "Speedup vs Single Link",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Per-Interface Individual Breakdown Cards
        item {
            Text(
                text = "INDIVIDUAL CHANNELS COMPARISON",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        item {
            ChannelSpeedCard(
                icon = Icons.Default.Wifi,
                title = "Wi-Fi (wlan0)",
                subtitle = "5 GHz 802.11ax",
                speedMbps = metrics.wifiSpeedMbps,
                latencyMs = metrics.wifiLatencyMs,
                color = WifiCyan
            )
        }

        item {
            ChannelSpeedCard(
                icon = Icons.Default.SimCard,
                title = "Cellular SIM 1",
                subtitle = "5G Sub-6 / LTE",
                speedMbps = metrics.sim1SpeedMbps,
                latencyMs = metrics.sim1LatencyMs,
                color = Sim1Magenta
            )
        }

        item {
            ChannelSpeedCard(
                icon = Icons.Default.CellTower,
                title = "Cellular SIM 2",
                subtitle = "DSDS Mobile Data Slot",
                speedMbps = metrics.sim2SpeedMbps,
                latencyMs = metrics.sim2LatencyMs,
                color = Sim2Blue
            )
        }

        // Explanation Info Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = TechSurfaceVariantDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                        contentDescription = "Architecture",
                        tint = PrimaryNeon,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "How Simultaneous Bonding Works",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Android allows binding individual TCP/UDP sockets to specific Network objects via Network.bindSocket(). By splitting network payloads across Wi-Fi and Cellular SIM modems simultaneously, bandwidth is aggregated without requiring specialized carrier support.",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
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
private fun ChannelSpeedCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    speedMbps: Double,
    latencyMs: Long,
    color: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(TechSurfaceDark)
            .border(1.dp, TechCardBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "$subtitle • ${latencyMs}ms ping",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${"%.1f".format(speedMbps)}",
                    color = color,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
                Text(
                    text = "Mbps",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
