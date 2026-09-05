package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ExponentCoral
import com.example.ui.theme.ProportionWood
import com.example.ui.theme.RatioDotCoral
import com.example.ui.theme.RatioDotGreen
import com.example.ui.theme.RatioTeal
import com.example.ui.theme.TriangleBlue
import com.example.ui.theme.TriangleGreen

/**
 * 1. تشابه المثلثات: Similar Triangles (Blue & Green)
 */
@Composable
fun TriangleSimilarityIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(76.dp, 64.dp)) {
        val w = size.width
        val h = size.height

        // Left Triangle (Blue) - larger
        val p1 = Path().apply {
            moveTo(w * 0.28f, h * 0.08f)
            lineTo(w * 0.50f, h * 0.88f)
            lineTo(w * 0.06f, h * 0.88f)
            close()
        }

        // Drop shadow for 3D depth
        drawPath(
            path = p1,
            color = Color(0x333B82C4),
            style = Stroke(width = 12f, join = StrokeJoin.Round, cap = StrokeCap.Round)
        )
        drawPath(
            path = p1,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFE5F1FC), Color(0xFFCBE3F9))
            )
        )
        drawPath(
            path = p1,
            color = TriangleBlue,
            style = Stroke(width = 8f, join = StrokeJoin.Round, cap = StrokeCap.Round)
        )

        // Right Triangle (Green) - smaller similar triangle
        val p2 = Path().apply {
            moveTo(w * 0.74f, h * 0.22f)
            lineTo(w * 0.94f, h * 0.88f)
            lineTo(w * 0.54f, h * 0.88f)
            close()
        }

        drawPath(
            path = p2,
            color = Color(0x3343B68A),
            style = Stroke(width = 10f, join = StrokeJoin.Round, cap = StrokeCap.Round)
        )
        drawPath(
            path = p2,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFE9F8F1), Color(0xFFC7EFE0))
            )
        )
        drawPath(
            path = p2,
            color = TriangleGreen,
            style = Stroke(width = 7f, join = StrokeJoin.Round, cap = StrokeCap.Round)
        )
    }
}

/**
 * 2. التناسب: Proportions (Two wooden rulers with = sign)
 */
@Composable
fun ProportionsIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(76.dp, 64.dp)) {
        val w = size.width
        val h = size.height

        val rulerWidth = 14f
        val rulerHeight = h * 0.86f
        val topY = h * 0.07f

        // Draw Left Ruler
        drawRuler(left = w * 0.18f, top = topY, width = rulerWidth, height = rulerHeight)

        // Draw Equals Sign '=' in the middle
        val eqLeft = w * 0.44f
        val eqRight = w * 0.56f
        val eqY1 = h * 0.44f
        val eqY2 = h * 0.56f
        val eqColor = Color(0xFF2B4764)

        drawLine(
            color = eqColor,
            start = Offset(eqLeft, eqY1),
            end = Offset(eqRight, eqY1),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = eqColor,
            start = Offset(eqLeft, eqY2),
            end = Offset(eqRight, eqY2),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )

        // Draw Right Ruler
        drawRuler(left = w * 0.68f, top = topY, width = rulerWidth, height = rulerHeight)
    }
}

private fun DrawScope.drawRuler(left: Float, top: Float, width: Float, height: Float) {
    // 3D shadow
    drawRoundRect(
        color = Color(0x30B5643B),
        topLeft = Offset(left + 2f, top + 2f),
        size = Size(width, height),
        cornerRadius = CornerRadius(6f, 6f)
    )
    // Body
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFEEE5), Color(0xFFF9DCCE))
        ),
        topLeft = Offset(left, top),
        size = Size(width, height),
        cornerRadius = CornerRadius(5f, 5f)
    )
    // Outline
    drawRoundRect(
        color = ProportionWood,
        topLeft = Offset(left, top),
        size = Size(width, height),
        cornerRadius = CornerRadius(5f, 5f),
        style = Stroke(width = 4f)
    )

    // Ruler measurement ticks
    val tickSteps = 5
    val stepY = height / (tickSteps + 1)
    for (i in 1..tickSteps) {
        val y = top + i * stepY
        val tickLen = if (i % 2 == 0) width * 0.55f else width * 0.35f
        drawLine(
            color = Color(0xFF2A3D4E),
            start = Offset(left + width * 0.2f, y),
            end = Offset(left + width * 0.2f + tickLen, y),
            strokeWidth = 2.5f,
            cap = StrokeCap.Round
        )
    }
}

/**
 * 3. النسبة: Ratios (Layered division symbol)
 */
@Composable
fun RatioIcon(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.size(76.dp, 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Top Dot (Mint green)
        Box(
            modifier = Modifier
                .size(12.dp)
                .shadow(3.dp, CircleShape, spotColor = RatioDotGreen)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFA5E3CE), RatioDotGreen)
                    )
                )
        )

        Spacer(modifier = Modifier.height(5.dp))

        // Middle Division Bar (Teal / Blue rounded pill)
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(10.dp)
                .shadow(4.dp, RoundedCornerShape(6.dp), spotColor = RatioTeal)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF5394B5), RatioTeal)
                    )
                )
        )

        Spacer(modifier = Modifier.height(5.dp))

        // Bottom Dot (Coral / Orange)
        Box(
            modifier = Modifier
                .size(12.dp)
                .shadow(3.dp, CircleShape, spotColor = RatioDotCoral)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFFFB39E), RatioDotCoral)
                    )
                )
        )
    }
}

/**
 * 4. الأسس والجذور: Exponents & Radicals (x² √x)
 */
@Composable
fun ExponentsRadicalsIcon(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.size(76.dp, 64.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // x² with 3D soft color
        Box(contentAlignment = Alignment.Center) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "x",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif,
                    color = ExponentCoral,
                    modifier = Modifier.offset(y = 4.dp)
                )
                Text(
                    text = "2",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ExponentCoral
                )
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // √x
        Text(
            text = "√x",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Serif,
            color = ExponentCoral
        )
    }
}

/**
 * Top Header Math Drafting Compass Icon
 */
@Composable
fun DraftingCompassIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(36.dp, 36.dp)) {
        val w = size.width
        val h = size.height

        // Top hinge circle
        drawCircle(
            color = Color(0xFF426582),
            radius = 5f,
            center = Offset(w * 0.5f, h * 0.22f)
        )
        // Top handle
        drawLine(
            color = Color(0xFF33536D),
            start = Offset(w * 0.5f, h * 0.08f),
            end = Offset(w * 0.5f, h * 0.22f),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )

        // Left Leg
        drawLine(
            color = Color(0xFF3B678A),
            start = Offset(w * 0.5f, h * 0.22f),
            end = Offset(w * 0.24f, h * 0.88f),
            strokeWidth = 4.5f,
            cap = StrokeCap.Round
        )

        // Right Leg
        drawLine(
            color = Color(0xFF3B678A),
            start = Offset(w * 0.5f, h * 0.22f),
            end = Offset(w * 0.76f, h * 0.88f),
            strokeWidth = 4.5f,
            cap = StrokeCap.Round
        )

        // Arc guide
        drawArc(
            color = Color(0xFF8BAEC9),
            startAngle = 30f,
            sweepAngle = 120f,
            useCenter = false,
            topLeft = Offset(w * 0.25f, h * 0.40f),
            size = Size(w * 0.5f, h * 0.35f),
            style = Stroke(width = 2.5f)
        )
    }
}

/**
 * Top Header Pastel Calculator Icon
 */
@Composable
fun PastelCalculatorIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(34.dp, 38.dp)
            .shadow(3.dp, RoundedCornerShape(7.dp), spotColor = Color(0x40337AB7))
            .clip(RoundedCornerShape(7.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF75A7D4), Color(0xFF568EBF))
                )
            )
            .padding(3.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.35f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFDCEAF6))
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Buttons grid (2 rows of 3 buttons)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.65f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MiniBtn(Color(0xFFF3F7FA))
                    MiniBtn(Color(0xFFF3F7FA))
                    MiniBtn(Color(0xFFF9A825))
                }
                Row(
                    modifier = Modifier.fillMaxSize().weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MiniBtn(Color(0xFFF3F7FA))
                    MiniBtn(Color(0xFFF3F7FA))
                    MiniBtn(Color(0xFFE57373))
                }
            }
        }
    }
}

@Composable
private fun MiniBtn(color: Color) {
    Box(
        modifier = Modifier
            .size(6.5.dp)
            .clip(RoundedCornerShape(1.5.dp))
            .background(color)
    )
}
