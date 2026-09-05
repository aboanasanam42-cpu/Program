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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.MathResult
import com.example.ui.theme.ClayCardBorder
import com.example.ui.theme.ClayCardHighlight
import com.example.ui.theme.ClayCardSurface
import com.example.ui.theme.HeaderBlue
import com.example.ui.theme.PdfRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextNavyDark
import com.example.ui.theme.TextNavyMedium
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppGreenDark
import com.example.utils.SharingUtils

@Composable
fun SolutionAndShareSection(
    mathResult: MathResult?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var whatsappPhone by remember { mutableStateOf("") }

    if (mathResult == null) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Step-by-Step Solution Card
        ClayCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp,
            elevation = 6.dp,
            testTag = "solution_card"
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(HeaderBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = HeaderBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "خطوات الحل المفصلة",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyDark
                    )
                }

                HorizontalDivider(color = ClayCardBorder, thickness = 1.dp)

                // List of steps
                mathResult.steps.forEachIndexed { index, step ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(HeaderBlue.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = HeaderBlue
                            )
                        }
                        Text(
                            text = step,
                            fontSize = 14.5.sp,
                            color = TextNavyMedium,
                            lineHeight = 22.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Final Answer Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFE8F8F1), Color(0xFFD4F3E4))
                            )
                        )
                        .border(1.5.dp, Color(0xFF55C193), RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "النتيجة النهائية",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B6E48)
                        )
                        Text(
                            text = mathResult.finalAnswer,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F5434),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // 2. WhatsApp Sharing Card
        ClayCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp,
            elevation = 5.dp,
            testTag = "whatsapp_card"
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(WhatsAppGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                            tint = WhatsAppGreenDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "مشاركة المسألة والحل عبر واتساب",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyDark
                    )
                }

                ClayInputField(
                    value = whatsappPhone,
                    onValueChange = { whatsappPhone = it },
                    label = "رقم المستلم (اختياري)",
                    placeholder = "مثال: 771134103 أو +967...",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    testTag = "whatsapp_phone_input"
                )

                ClayButton(
                    text = "إرسال عبر واتساب",
                    icon = Icons.Default.Send,
                    backgroundColor = WhatsAppGreenDark,
                    onClick = {
                        SharingUtils.shareViaWhatsApp(context, whatsappPhone, mathResult)
                    },
                    testTag = "whatsapp_share_button"
                )
            }
        }

        // 3. PDF Export Card
        ClayCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp,
            elevation = 5.dp,
            testTag = "pdf_card"
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(PdfRed.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            tint = PdfRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "تصدير المستند كملف PDF",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )
                        Text(
                            text = "يتضمن ترويسة DR/MALIK وخاتمة الحقوق الرسمية",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                ClayButton(
                    text = "حفظ ومشاركة كملف PDF",
                    icon = Icons.Default.PictureAsPdf,
                    backgroundColor = PdfRed,
                    onClick = {
                        SharingUtils.exportAndSharePdf(context, mathResult)
                    },
                    testTag = "pdf_export_button"
                )
            }
        }
    }
}
