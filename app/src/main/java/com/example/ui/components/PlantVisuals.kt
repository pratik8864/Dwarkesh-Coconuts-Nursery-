package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentLight

/**
 * High quality visual component for Plant items.
 * If image is a remote URL or file path, loads with Coil AsyncImage.
 * Otherwise, renders custom illustrated variety art (Green Dwarf, Orange Dwarf, Hybrid, etc.).
 */
@Composable
fun PlantVisualCard(
    imageKey: String,
    modifier: Modifier = Modifier,
    varietyType: String = "green"
) {
    if (imageKey.startsWith("http://") || imageKey.startsWith("https://") || imageKey.startsWith("content://") || imageKey.startsWith("file://")) {
        AsyncImage(
            model = imageKey,
            contentDescription = "Plant image",
            modifier = modifier.clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    } else {
        // Render rich illustrated variety canvas
        IllustratedPlantArt(
            imageKey = imageKey,
            modifier = modifier
        )
    }
}

@Composable
fun IllustratedPlantArt(
    imageKey: String,
    modifier: Modifier = Modifier
) {
    // Variety color accents
    val (primaryNutColor, secondaryNutColor, foliageColor, skyTint) = when {
        imageKey.contains("orange") -> Quadruple(
            Color(0xFFFF9800),
            Color(0xFFFFB74D),
            Color(0xFF2E7D32),
            Color(0xFFFFF3E0)
        )
        imageKey.contains("yellow") || imageKey.contains("myd") -> Quadruple(
            Color(0xFFFFD600),
            Color(0xFFFFF176),
            Color(0xFF388E3C),
            Color(0xFFFFFDE7)
        )
        imageKey.contains("hybrid") || imageKey.contains("txd") -> Quadruple(
            Color(0xFF558B2F),
            Color(0xFF7CB342),
            Color(0xFF1B5E20),
            Color(0xFFE8F5E9)
        )
        imageKey.contains("tall") || imageKey.contains("wct") -> Quadruple(
            Color(0xFF4E342E),
            Color(0xFF6D4C41),
            Color(0xFF1B5E20),
            Color(0xFFECEFF1)
        )
        else -> Quadruple( // Green dwarf / standard
            Color(0xFF43A047),
            Color(0xFF66BB6A),
            Color(0xFF2E7D32),
            Color(0xFFE8F5E9)
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    listOf(skyTint, Color(0xFFF1F8E9), Color(0xFFC8E6C9))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Nursery Pot / Polybag at bottom
            val potPath = Path().apply {
                moveTo(w * 0.38f, h * 0.72f)
                lineTo(w * 0.62f, h * 0.72f)
                lineTo(w * 0.58f, h * 0.94f)
                lineTo(w * 0.42f, h * 0.94f)
                close()
            }
            drawPath(potPath, color = Color(0xFF212121))
            drawPath(potPath, color = Color(0xFF424242), style = Stroke(width = 2f))

            // Seed Coconut base inside polybag
            drawCircle(
                color = Color(0xFF5D4037),
                radius = w * 0.09f,
                center = Offset(w * 0.5f, h * 0.73f)
            )

            // Healthy Sprout Stem
            val stemPath = Path().apply {
                moveTo(w * 0.5f, h * 0.72f)
                quadraticTo(w * 0.48f, h * 0.52f, w * 0.5f, h * 0.38f)
            }
            drawPath(
                stemPath,
                color = foliageColor,
                style = Stroke(width = 8f, cap = StrokeCap.Round)
            )

            // Healthy Young Coconut Fronds (Sprouting leaves)
            val angles = listOf(-60f, -30f, 0f, 30f, 60f, -80f, 80f)
            val pivot = Offset(w * 0.5f, h * 0.38f)

            angles.forEachIndexed { i, ang ->
                rotate(ang, pivot = pivot) {
                    val leafLen = h * 0.32f * (1f - (i * 0.05f))
                    val leafP = Path().apply {
                        moveTo(pivot.x, pivot.y)
                        cubicTo(
                            pivot.x - (w * 0.04f), pivot.y - (leafLen * 0.5f),
                            pivot.x - (w * 0.02f), pivot.y - leafLen,
                            pivot.x, pivot.y - (leafLen * 1.05f)
                        )
                        cubicTo(
                            pivot.x + (w * 0.02f), pivot.y - leafLen,
                            pivot.x + (w * 0.04f), pivot.y - (leafLen * 0.5f),
                            pivot.x, pivot.y
                        )
                        close()
                    }
                    drawPath(
                        leafP,
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFF81C784), foliageColor, Color(0xFF1B5E20)),
                            startY = pivot.y - leafLen,
                            endY = pivot.y
                        )
                    )
                }
            }

            // Clustered Tender Coconuts on side
            drawCircle(color = primaryNutColor, radius = w * 0.065f, center = Offset(w * 0.72f, h * 0.62f))
            drawCircle(color = secondaryNutColor, radius = w * 0.055f, center = Offset(w * 0.78f, h * 0.56f))
            drawCircle(color = primaryNutColor, radius = w * 0.05f, center = Offset(w * 0.82f, h * 0.65f))

            // Sunlight highlight
            drawCircle(
                color = Color.White.copy(alpha = 0.5f),
                radius = w * 0.02f,
                center = Offset(w * 0.70f, h * 0.60f)
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
