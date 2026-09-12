package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.LocalBrandingConfig
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentLight

/**
 * Authentic DWARKESH COCONUTS & NURSERY Logo Emblem
 * Rendered dynamically using the admin-configured logo (or official fallback asset)
 * with optional radiant glow and transparent background capability.
 */
@Composable
fun DwarkeshLogoView(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    showGlow: Boolean = false,
    transparentBackground: Boolean = true,
    animatedLeaves: Boolean = false,
    customLogoUrl: String? = null
) {
    val brandingConfig = LocalBrandingConfig.current
    val effectiveLogoUrl = customLogoUrl ?: brandingConfig.logoUrl

    val infiniteTransition = rememberInfiniteTransition(label = "logo_anim")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = modifier
            .size(size)
            .then(
                if (showGlow) {
                    Modifier.drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    GoldAccent.copy(alpha = glowAlpha * 0.5f),
                                    GoldAccentLight.copy(alpha = glowAlpha * 0.25f),
                                    Color.Transparent
                                ),
                                center = center,
                                radius = size.toPx() * 0.6f
                            )
                        )
                    }
                } else Modifier
            )
            .then(
                if (!transparentBackground) {
                    Modifier
                        .clip(CircleShape)
                        .background(ForestGreenDark)
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!effectiveLogoUrl.isNullOrEmpty()) {
            AsyncImage(
                model = effectiveLogoUrl,
                contentDescription = "Dwarkesh Coconuts & Nursery Logo",
                modifier = Modifier.size(size),
                contentScale = ContentScale.Fit
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.img_dwarkesh_logo),
                contentDescription = "Dwarkesh Coconuts & Nursery Logo",
                modifier = Modifier.size(size),
                contentScale = ContentScale.Fit
            )
        }
    }
}

/**
 * Compact horizontal header branding with official Logo + Business Name
 */
@Composable
fun DwarkeshHeaderBrand(
    modifier: Modifier = Modifier,
    logoSize: Dp = 44.dp,
    textColor: Color = Color.White,
    customLogoUrl: String? = null,
    businessTitle: String? = null
) {
    val brandingConfig = LocalBrandingConfig.current
    val effectiveTitle = businessTitle ?: brandingConfig.appName

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DwarkeshLogoView(
            size = logoSize,
            showGlow = false,
            transparentBackground = true,
            customLogoUrl = customLogoUrl
        )
        Column {
            Text(
                text = if (effectiveTitle.contains(" ")) effectiveTitle.substringBefore(" ") else effectiveTitle,
                color = GoldAccentLight,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Serif,
                fontSize = 17.sp,
                letterSpacing = 1.sp
            )
            Text(
                text = if (effectiveTitle.contains(" ")) effectiveTitle.substringAfter(" ") else "COCONUTS & NURSERY",
                color = textColor.copy(alpha = 0.9f),
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 1.2.sp
            )
        }
    }
}
