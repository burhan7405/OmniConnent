package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InterfaceType
import com.example.model.NetworkInterfaceInfo
import com.example.ui.theme.BondedGreen
import com.example.ui.theme.Sim1Magenta
import com.example.ui.theme.Sim2Blue
import com.example.ui.theme.TechCardBorder
import com.example.ui.theme.TechSurfaceDark
import com.example.ui.theme.WifiCyan

@Composable
fun MultiPipeVisualizer(
    interfaces: List<NetworkInterfaceInfo>,
    isBondingEnabled: Boolean,
    aggregatedSpeed: Double,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val wifiInfo = interfaces.find { it.type == InterfaceType.WIFI }
    val sim1Info = interfaces.find { it.type == InterfaceType.SIM_1 }
    val sim2Info = interfaces.find { it.type == InterfaceType.SIM_2 }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(TechSurfaceDark)
            .border(1.dp, TechCardBorder, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val leftX = width * 0.05f
            val coreX = width * 0.52f
            val rightX = width * 0.95f
            val coreY = height * 0.5f

            val yWifi = height * 0.22f
            val ySim1 = height * 0.50f
            val ySim2 = height * 0.78f

            // 1. Draw Stream 1: Wi-Fi (Top branch)
            val pathWifi = Path().apply {
                moveTo(leftX, yWifi)
                cubicTo(
                    leftX + (coreX - leftX) * 0.5f, yWifi,
                    coreX - 30f, coreY,
                    coreX, coreY
                )
            }
            val wifiActive = wifiInfo?.isConnected == true && isBondingEnabled
            val wifiColor = if (wifiActive) WifiCyan else WifiCyan.copy(alpha = 0.25f)

            drawPath(
                path = pathWifi,
                color = wifiColor.copy(alpha = 0.3f),
                style = Stroke(width = 10f, cap = StrokeCap.Round)
            )
            drawPath(
                path = pathWifi,
                color = wifiColor,
                style = Stroke(width = 4f, cap = StrokeCap.Round)
            )

            // 2. Draw Stream 2: SIM 1 (Middle branch)
            val pathSim1 = Path().apply {
                moveTo(leftX, ySim1)
                lineTo(coreX, coreY)
            }
            val sim1Active = sim1Info?.isConnected == true && isBondingEnabled
            val sim1Color = if (sim1Active) Sim1Magenta else Sim1Magenta.copy(alpha = 0.25f)

            drawPath(
                path = pathSim1,
                color = sim1Color.copy(alpha = 0.3f),
                style = Stroke(width = 10f, cap = StrokeCap.Round)
            )
            drawPath(
                path = pathSim1,
                color = sim1Color,
                style = Stroke(width = 4f, cap = StrokeCap.Round)
            )

            // 3. Draw Stream 3: SIM 2 (Bottom branch)
            val pathSim2 = Path().apply {
                moveTo(leftX, ySim2)
                cubicTo(
                    leftX + (coreX - leftX) * 0.5f, ySim2,
                    coreX - 30f, coreY,
                    coreX, coreY
                )
            }
            val sim2Active = sim2Info?.isConnected == true && isBondingEnabled
            val sim2Color = if (sim2Active) Sim2Blue else Sim2Blue.copy(alpha = 0.25f)

            drawPath(
                path = pathSim2,
                color = sim2Color.copy(alpha = 0.3f),
                style = Stroke(width = 10f, cap = StrokeCap.Round)
            )
            drawPath(
                path = pathSim2,
                color = sim2Color,
                style = Stroke(width = 4f, cap = StrokeCap.Round)
            )

            // 4. Draw Combined Converged Conduit (From core to right)
            val bondedPath = Path().apply {
                moveTo(coreX, coreY)
                lineTo(rightX, coreY)
            }
            val bondedActive = isBondingEnabled && (wifiActive || sim1Active || sim2Active)
            val bondedColor = if (bondedActive) BondedGreen else Color.Gray

            // Outer thick glow
            drawPath(
                path = bondedPath,
                brush = Brush.horizontalGradient(
                    colors = if (bondedActive) listOf(WifiCyan, BondedGreen) else listOf(Color.DarkGray, Color.Gray),
                    startX = coreX,
                    endX = rightX
                ),
                style = Stroke(width = 18f, cap = StrokeCap.Round)
            )
            // Core bright laser line
            drawPath(
                path = bondedPath,
                color = if (bondedActive) Color.White else Color.LightGray,
                style = Stroke(width = 6f, cap = StrokeCap.Round)
            )

            // 5. Draw Animated Particle Pulses along paths
            if (bondedActive) {
                // Incoming pulses
                val p1X = leftX + (coreX - leftX) * phase
                drawCircle(WifiCyan, radius = 5f, center = Offset(p1X, yWifi + (coreY - yWifi) * (phase * phase)))

                val p2X = leftX + (coreX - leftX) * ((phase + 0.33f) % 1f)
                drawCircle(Sim1Magenta, radius = 5f, center = Offset(p2X, coreY))

                val p3X = leftX + (coreX - leftX) * ((phase + 0.66f) % 1f)
                drawCircle(Sim2Blue, radius = 5f, center = Offset(p3X, ySim2 + (coreY - ySim2) * (phase * phase)))

                // Outgoing bonded speed pulses
                for (i in 0..2) {
                    val outPhase = (phase + (i * 0.33f)) % 1f
                    val outX = coreX + (rightX - coreX) * outPhase
                    drawCircle(Color.White, radius = 6f, center = Offset(outX, coreY))
                    drawCircle(BondedGreen, radius = 9f, center = Offset(outX, coreY), alpha = 0.5f)
                }
            }

            // 6. Draw Central Convergence Hub
            drawCircle(
                color = if (bondedActive) BondedGreen.copy(alpha = 0.3f) else Color.DarkGray,
                radius = 22f,
                center = Offset(coreX, coreY)
            )
            drawCircle(
                color = if (bondedActive) Color(0xFF0F172A) else Color.Black,
                radius = 16f,
                center = Offset(coreX, coreY)
            )
            drawCircle(
                color = if (bondedActive) BondedGreen else Color.Gray,
                radius = 8f,
                center = Offset(coreX, coreY)
            )
        }

        // Labels overlay
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 4.dp, top = 2.dp)
        ) {
            Text(
                text = "Wi-Fi 6: ${"%.1f".format(wifiInfo?.currentSpeedMbps ?: 0.0)}M",
                color = WifiCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 4.dp)
        ) {
            Text(
                text = "SIM 1: ${"%.1f".format(sim1Info?.currentSpeedMbps ?: 0.0)}M",
                color = Sim1Magenta,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 4.dp, bottom = 2.dp)
        ) {
            Text(
                text = "SIM 2: ${"%.1f".format(sim2Info?.currentSpeedMbps ?: 0.0)}M",
                color = Sim2Blue,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp)
        ) {
            Text(
                text = "BONDED: ${"%.1f".format(aggregatedSpeed)} Mbps",
                color = if (isBondingEnabled) BondedGreen else Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
