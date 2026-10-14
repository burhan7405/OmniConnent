package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import com.example.model.BondedDownloadState
import com.example.model.DownloadChunk
import com.example.model.InterfaceType
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
fun BondedDownloaderScreen(
    downloadState: BondedDownloadState,
    onStartDownload: (String) -> Unit,
    onCancelDownload: () -> Unit,
    onOpenDeveloperSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalProgress = if (downloadState.totalBytes > 0) {
        (downloadState.downloadedBytes.toFloat() / downloadState.totalBytes).coerceIn(0f, 1f)
    } else 0f

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
                text = "PARALLEL MULTI-PATH TRANSFER",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Bonded Downloader & Setup Guide",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Stripes HTTP range requests concurrently across Wi-Fi and Mobile Data to maximize throughput.",
                color = TextMuted,
                fontSize = 12.sp
            )
        }

        // Downloader Box
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
                            Icon(
                                imageVector = Icons.Default.CloudDownload,
                                contentDescription = "Downloader",
                                tint = BondedGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "BONDED PAYLOAD FETCH",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (downloadState.isDownloading) {
                            Surface(
                                color = BondedGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "${"%.1f".format(downloadState.currentSpeedMbps)} Mbps",
                                    color = BondedGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Benchmark Target: 25 MB Cloudflare Edge Test",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Splits byte range into 3 simultaneous sub-streams over Wi-Fi, SIM 1, and SIM 2",
                        color = TextMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { totalProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = BondedGreen,
                        trackColor = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${(downloadState.downloadedBytes / (1024 * 1024))} MB / ${(downloadState.totalBytes / (1024 * 1024))} MB",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "${(totalProgress * 100).toInt()}% Complete",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (downloadState.completedSuccessfully) BondedGreen else Color.White
                        )
                    }

                    // Download Action Buttons
                    Spacer(modifier = Modifier.height(14.dp))
                    if (!downloadState.isDownloading) {
                        Button(
                            onClick = { onStartDownload("https://speed.cloudflare.com/__down?bytes=25000000") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryNeon,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("start_download_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Start Download",
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (downloadState.completedSuccessfully) "RE-RUN BONDED DOWNLOAD" else "START BONDED 3-WAY DOWNLOAD",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    } else {
                        OutlinedButton(
                            onClick = onCancelDownload,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("cancel_download_button")
                        ) {
                            Icon(imageVector = Icons.Default.Cancel, contentDescription = "Cancel")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Cancel Multi-Stream", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Parallel Chunks Stream Visualization
        if (downloadState.chunks.isNotEmpty()) {
            item {
                Text(
                    text = "PARALLEL STREAM PROGRESS BY INTERFACE",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            items(downloadState.chunks.size) { index ->
                val chunk = downloadState.chunks[index]
                ChunkProgressCard(chunk = chunk)
            }
        }

        // Setup Guide Section
        item {
            Text(
                text = "SYSTEM CONFIGURATION GUIDE",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        // Step 1: Mobile Data Always Active
        item {
            SetupStepCard(
                stepNumber = "1",
                title = "Mobile Data Always Active (Crucial)",
                description = "By default, Android turns off cellular data standby when Wi-Fi connects to save battery. Enabling this keeps both cellular modems powered and ready for multi-path bonding.",
                actionLabel = "Open Developer Settings",
                onAction = onOpenDeveloperSettings,
                buttonTag = "open_dev_settings_guide"
            )
        }

        // Step 2: DSDS Hardware Configuration
        item {
            SetupStepCard(
                stepNumber = "2",
                title = "Dual SIM Dual Standby (DSDS) Setup",
                description = "In Android Settings > Network & Internet > SIMs, ensure both SIM 1 and SIM 2 are toggled ON. Modern chipsets support concurrent packet transmission across both SIM basebands.",
                actionLabel = null,
                onAction = null,
                buttonTag = null
            )
        }

        // Step 3: Android Direct Socket Binding API
        item {
            SetupStepCard(
                stepNumber = "3",
                title = "Zero-Carrier Bypass via Sockets",
                description = "OmniBond bypasses system default routing by invoking Network.bindSocket() and Network.openConnection(url) on each interface. This enables simultaneous multi-network transmission on standard carrier contracts.",
                actionLabel = null,
                onAction = null,
                buttonTag = null
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ChunkProgressCard(chunk: DownloadChunk) {
    val (color, name) = when (chunk.interfaceType) {
        InterfaceType.WIFI -> Pair(WifiCyan, "Stream 1: Wi-Fi (wlan0)")
        InterfaceType.SIM_1 -> Pair(Sim1Magenta, "Stream 2: Cellular SIM 1")
        InterfaceType.SIM_2 -> Pair(Sim2Blue, "Stream 3: Cellular SIM 2")
        else -> Pair(BondedGreen, "Stream")
    }

    val chunkTotal = chunk.endByte - chunk.startByte + 1
    val progress = if (chunkTotal > 0) (chunk.downloadedBytes.toFloat() / chunkTotal).coerceIn(0f, 1f) else 0f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(TechSurfaceDark)
            .border(1.dp, TechCardBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Text(
                    text = "${(chunk.downloadedBytes / (1024 * 1024))} / ${(chunkTotal / (1024 * 1024))} MB",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = color,
                trackColor = Color(0xFF1E293B)
            )
        }
    }
}

@Composable
private fun SetupStepCard(
    stepNumber: String,
    title: String,
    description: String,
    actionLabel: String?,
    onAction: (() -> Unit)?,
    buttonTag: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(TechSurfaceDark)
            .border(1.dp, TechCardBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(PrimaryNeon.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber,
                    color = PrimaryNeon,
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )

                if (actionLabel != null && onAction != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onAction,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TechSurfaceVariantDark,
                            contentColor = PrimaryNeon
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .testTag(buttonTag ?: "step_action_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = actionLabel,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = actionLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
