package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppText
import com.example.ui.components.DwarkeshLogoView
import com.example.ui.theme.DarkGreenBackground
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentLight
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_splash")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LaunchedEffect(Unit) {
        // Animate entrance
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = LinearEasing)
        )
        // 2.5 seconds splash display as requested
        delay(1600)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ForestGreenDark,
                        Color(0xFF0A2B18),
                        DarkGreenBackground
                    )
                )
            )
            .clickable { onSplashFinished() }
            .testTag("splash_screen_container"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(32.dp)
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            // Original Logo with custom vector graphics & golden glow
            DwarkeshLogoView(
                size = 220.dp,
                showGlow = true,
                transparentBackground = true,
                animatedLeaves = true,
                modifier = Modifier.scale(pulseGlow)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "DWARKESH",
                color = GoldAccentLight,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Serif,
                fontSize = 28.sp,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "COCONUTS & NURSERY",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "દ્વારકેશ કોકોનટ્સ & નર્સરી",
                color = GoldAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Quality Plants • Healthy Growth • Trusted Service",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp),
                color = GoldAccent,
                trackColor = ForestGreen
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Loading nursery varieties...",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp
            )
        }
    }
}
