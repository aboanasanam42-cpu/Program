package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ClayBackground
import com.example.ui.theme.ClayBackgroundEnd
import com.example.ui.theme.ClayCardBorder
import com.example.ui.theme.ClayCardHighlight
import com.example.ui.theme.ClayCardShadow
import com.example.ui.theme.ClayCardSurface
import com.example.ui.theme.HeaderBlue
import com.example.ui.theme.HeaderBlueDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextNavyDark
import com.example.ui.theme.TextNavyMedium

/**
 * 3D Soft Claymorphism Card Container
 */
@Composable
fun ClayCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 26.dp,
    elevation: Dp = 6.dp,
    backgroundColor: Color = ClayCardSurface,
    borderColor: Color = ClayCardBorder,
    highlightBorder: Boolean = true,
    onClick: (() -> Unit)? = null,
    testTag: String = "clay_card",
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.96f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "clay_press"
    )

    val shape: Shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .testTag(testTag)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (isPressed) 2.dp else elevation,
                shape = shape,
                ambientColor = ClayCardShadow,
                spotColor = ClayCardShadow
            )
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(ClayCardHighlight, backgroundColor)
                )
            )
            .border(
                width = 1.5.dp,
                color = if (highlightBorder) borderColor else Color.Transparent,
                shape = shape
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * 3D Soft Extruded Button with Tactile Feedback
 */
@Composable
fun ClayButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    backgroundColor: Color = HeaderBlue,
    contentColor: Color = Color.White,
    height: Dp = 52.dp,
    testTag: String = "clay_button"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "btn_press"
    )

    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .testTag(testTag)
            .fillMaxWidth()
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (isPressed) 2.dp else 6.dp,
                shape = shape,
                ambientColor = backgroundColor.copy(alpha = 0.4f),
                spotColor = backgroundColor.copy(alpha = 0.4f)
            )
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor.copy(alpha = 0.92f),
                        backgroundColor
                    )
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.35f), shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = contentColor,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 3D Soft Pill Header Bar "DR/MALIK" with Math Icons
 */
@Composable
fun MainHeaderBar(
    modifier: Modifier = Modifier
) {
    ClayCard(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp),
        cornerRadius = 24.dp,
        elevation = 7.dp,
        borderColor = ClayCardBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Math Icon: Drafting Divider / Compass
            DraftingCompassIcon(modifier = Modifier.size(38.dp))

            // Center: 3D Soft Text "DR/MALIK"
            Box(contentAlignment = Alignment.Center) {
                // Soft shadow text
                Text(
                    text = "DR/MALIK",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    color = Color(0x3519446D),
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.offset(x = 1.5.dp, y = 2.dp)
                )
                // Main embossed text
                Text(
                    text = "DR/MALIK",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    color = HeaderBlue,
                    letterSpacing = 1.2.sp
                )
            }

            // Right Math Icon: Pastel 3D Calculator
            PastelCalculatorIcon(modifier = Modifier.size(34.dp, 38.dp))
        }
    }
}

/**
 * Main Screen Footer: 3-line credits matching the reference design
 */
@Composable
fun MainFooterCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ClayCard(
        modifier = modifier
            .fillMaxWidth(),
        cornerRadius = 24.dp,
        elevation = 5.dp,
        borderColor = ClayCardBorder
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "فكرة أ / محمد الرميمة",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextNavyDark,
                textAlign = TextAlign.Center
            )
            Text(
                text = "تصميم وبرمجة / مالك الرميمة",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextNavyDark,
                textAlign = TextAlign.Center
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        try {
                            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:771134103"))
                            context.startActivity(dialIntent)
                        } catch (e: Exception) {
                            // ignore
                        }
                    }
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "اتصال",
                    tint = HeaderBlueDark,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "هاتف / 771134103",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeaderBlueDark,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * On-Screen Sub-Screen Permanent Branding Footer
 */
@Composable
fun SubScreenBrandingFooter(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ClayCard(
        modifier = modifier.fillMaxWidth(),
        cornerRadius = 16.dp,
        elevation = 3.dp,
        backgroundColor = Color(0xFFF3F8FC)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    try {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:771134103"))
                        context.startActivity(dialIntent)
                    } catch (e: Exception) {
                        // ignore
                    }
                }
                .padding(vertical = 10.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
                tint = HeaderBlueDark,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "تصميم وبرمجة الدكتور / مالك الرميمة - هاتف / 771134103",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextNavyDark,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Sub-Screen Top Bar with Back Button and Title
 */
@Composable
fun SubScreenTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back Button
        ClayCard(
            modifier = Modifier.size(46.dp),
            cornerRadius = 16.dp,
            elevation = 4.dp,
            onClick = onBack,
            testTag = "back_button"
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "الرجوع",
                tint = HeaderBlueDark,
                modifier = Modifier.size(22.dp)
            )
        }

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextNavyDark,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
        )

        // Subtle balance spacer
        Spacer(modifier = Modifier.size(46.dp))
    }
}

/**
 * Soft Input Field with Inset Look
 */
@Composable
fun ClayInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingText: String? = null,
    testTag: String = "input_field"
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextNavyMedium,
            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = TextMuted,
                    fontSize = 14.sp
                )
            },
            trailingIcon = {
                if (trailingText != null) {
                    Text(
                        text = trailingText,
                        fontSize = 13.sp,
                        color = TextNavyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                } else if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "مسح",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ClayCardHighlight,
                unfocusedContainerColor = Color(0xFFF9FBFE),
                focusedBorderColor = HeaderBlue,
                unfocusedBorderColor = ClayCardBorder,
                focusedTextColor = TextNavyDark,
                unfocusedTextColor = TextNavyDark
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag)
        )
    }
}

/**
 * Subtle Background Watermark Math Formulas
 * Replicating y=mx+b, \Sigma, x², \sqrt{x} from reference image
 */
@Composable
fun BackgroundMathWatermark(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(ClayBackground, ClayBackgroundEnd)
                )
            )
    ) {
        // Top Left: y = mx + b
        Text(
            text = "y = mx + b",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0x28638AA8),
            fontFamily = FontFamily.Serif,
            modifier = Modifier.offset(x = 24.dp, y = 84.dp)
        )

        // Top Right: y = mx + b
        Text(
            text = "y = mx + b",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0x28638AA8),
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-30).dp, y = 84.dp)
        )

        // Middle Center: \Sigma
        Text(
            text = "Σ",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0x28638AA8),
            fontFamily = FontFamily.Serif,
            modifier = Modifier.align(Alignment.Center)
        )

        // Lower Left: y = mx + b
        Text(
            text = "y = mx + b",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0x28638AA8),
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 24.dp, y = 140.dp)
        )

        // Lower Right: y = mx + b
        Text(
            text = "y = mx + b",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0x28638AA8),
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-24).dp, y = 140.dp)
        )

        // Right side: x²
        Text(
            text = "x²",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0x22638AA8),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-10).dp, y = 110.dp)
        )

        // Center bottom: \Sigma
        Text(
            text = "Σ",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0x22638AA8),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-150).dp)
        )
    }
}
