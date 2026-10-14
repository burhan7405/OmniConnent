package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BondedGreen
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.Sim1Magenta
import com.example.ui.theme.TechCardBorder
import com.example.ui.theme.TechSurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WifiCyan
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpeedometerGauge(
    speedMbps: Double,
    maxSpeedMbps: Double = 120.0,
    title: String = "Aggregated Bandwidth",
    latencyMs: Long = 18L,
    efficiencyPercent: Int = 94,
    modifier: Modifier = Modifier
) {
    val targetRatio = (speedMbps / maxSpeedMbps).coerceIn(0.0, 1.0).toFloat()
    val animatedRatio by animateFloatAsState(
        targetValue = targetRatio,
        animationSpec = tween(durationMillis = 400),
        label = "gaugeRatio"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(TechSurfaceDark)
            .border(1.dp, TechCardBorder, RoundedCornerShape(20.dp))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                color = TextSecondary
            )

            Box(
                modifier = Modifier
                    .size(220.dp, 130.dp)
                    .padding(top = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(220.dp, 130.dp)) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    val center = Offset(canvasWidth / 2f, canvasHeight * 0.95f)
                    val radius = canvasWidth * 0.42f
                    val strokeWidth = 24f

                    val startAngle = 180f
                    val sweepAngle = 180f

                    // Background Track Arc
                    drawArc(
                        color = Color(0xFF1E293B),
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Active Glowing Gradient Arc
                    val activeSweep = sweepAngle * animatedRatio
                    if (activeSweep > 0f) {
                        drawArc(
                            brush = Brush.sweepGradient(
                                0.0f to WifiCyan,
                                0.3f to Sim1Magenta,
                                0.6f to PrimaryNeon,
                                1.0f to BondedGreen,
                                center = center
                            ),
                            startAngle = startAngle,
                            sweepAngle = activeSweep,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    // Needle
                    val needleAngleRad = Math.toRadians((startAngle + activeSweep).toDouble())
                    val needleLength = radius * 0.85f
                    val needleEnd = Offset(
                        (center.x + needleLength * cos(needleAngleRad)).toFloat(),
                        (center.y + needleLength * sin(needleAngleRad)).toFloat()
                    )

                    drawLine(
                        color = Color.White,
                        start = center,
                        end = needleEnd,
                        strokeWidth = 5f,
                        cap = StrokeCap.Round
                    )

                    // Hub circle
                    drawCircle(color = BondedGreen, radius = 10f, center = center)
                    drawCircle(color = Color(0xFF0F172A), radius = 5f, center = center)
                }

                // Digital Readout
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 35.dp)
                ) {
                    Text(
                        text = "%.1f".format(speedMbps),
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Mbps",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = BondedGreen
                    )
                }
            }

            // Bottom metric chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MetricPill(label = "PING", value = "${latencyMs}ms", color = WifiCyan)
                MetricPill(label = "EFFICIENCY", value = "${efficiencyPercent}%", color = BondedGreen)
                MetricPill(label = "PEAK SPEED", value = "${"%.1f".format(speedMbps * 1.15)}M", color = PrimaryNeon)
            }
        }
    }
}

@Composable
private fun MetricPill(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 13.sp, color = color, fontWeight = FontWeight.ExtraBold)
    }
}
