package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import com.example.ui.theme.Sim1Magenta
import com.example.ui.theme.Sim2Blue
import com.example.ui.theme.TechAmber
import com.example.ui.theme.TechCardBorder
import com.example.ui.theme.TechSurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WifiCyan

@Composable
fun InterfaceCard(
    info: NetworkInterfaceInfo,
    onTestSocket: () -> Unit,
    isTesting: Boolean,
    testResult: Pair<Boolean, Long>?,
    modifier: Modifier = Modifier
) {
    val accentColor = when (info.type) {
        InterfaceType.WIFI -> WifiCyan
        InterfaceType.SIM_1 -> Sim1Magenta
        InterfaceType.SIM_2 -> Sim2Blue
        else -> TechAmber
    }

    val icon = when (info.type) {
        InterfaceType.WIFI -> Icons.Default.Wifi
        InterfaceType.SIM_1 -> Icons.Default.SimCard
        InterfaceType.SIM_2 -> Icons.Default.CellTower
        else -> Icons.Default.Sensors
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(TechSurfaceDark)
            .border(1.dp, TechCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            // Header Row: Icon, Name & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = info.name,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = info.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = if (info.isBonded) BondedGreen.copy(alpha = 0.18f) else Color.DarkGray,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (info.isBonded) "BONDED" else "EXCLUDED",
                                color = if (info.isBonded) BondedGreen else Color.LightGray,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "${info.carrierOrSsid} • ${info.networkGeneration}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                // Status Indicator
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (info.isConnected) BondedGreen else Color.Gray)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(label = "IP ADDRESS", value = info.ipAddress)
                StatItem(label = "LATENCY", value = if (info.isConnected) "${info.latencyMs} ms" else "N/A")
                StatItem(
                    label = "LIVE SPEED",
                    value = "${"%.1f".format(info.currentSpeedMbps)} Mbps",
                    valueColor = accentColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action row: Socket Test / Route Verification
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (testResult != null) {
                    val (success, time) = testResult
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Test Status",
                            tint = if (success) BondedGreen else Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (success) "Bound socket verified ($time ms)" else "Direct route timeout",
                            fontSize = 11.sp,
                            color = if (success) BondedGreen else Color.Red,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Text(
                        text = "Direct interface socket available",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Button(
                    onClick = onTestSocket,
                    enabled = !isTesting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor.copy(alpha = 0.2f),
                        contentColor = accentColor
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("test_socket_${info.id}")
                ) {
                    if (isTesting) {
                        CircularProgressIndicator(
                            color = accentColor,
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.NetworkCheck,
                            contentDescription = "Test Route",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Test Route", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    valueColor: Color = Color.White
) {
    Column {
        Text(text = label, fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 12.sp, color = valueColor, fontWeight = FontWeight.SemiBold)
    }
}
