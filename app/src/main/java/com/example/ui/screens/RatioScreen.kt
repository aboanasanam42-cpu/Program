package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.MathResult
import com.example.math.MathSolvers
import com.example.ui.components.ClayButton
import com.example.ui.components.ClayCard
import com.example.ui.components.ClayInputField
import com.example.ui.components.RatioIcon
import com.example.ui.components.SolutionAndShareSection
import com.example.ui.components.SubScreenBrandingFooter
import com.example.ui.components.SubScreenTopBar
import com.example.ui.theme.ClayBackground
import com.example.ui.theme.RatioTeal
import com.example.ui.theme.TextNavyDark

@Composable
fun RatioScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var mathResult by remember { mutableStateOf<MathResult?>(null) }

    // Mode 0: Simplify ratio
    var ratioA by remember { mutableStateOf("24") }
    var ratioB by remember { mutableStateOf("36") }

    // Mode 1: Proportional distribution
    var totalAmount by remember { mutableStateOf("150000") }
    var part1 by remember { mutableStateOf("2") }
    var part2 by remember { mutableStateOf("3") }
    var part3 by remember { mutableStateOf("5") }

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ClayBackground,
        bottomBar = {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 14.dp)
            ) {
                SubScreenBrandingFooter()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SubScreenTopBar(
                title = "النسبة",
                onBack = onBack
            )

            // Header Banner
            ClayCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "النسبة (Ratios)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "تبسيط النسب لأبسط صورة، والتقسيم التناسبي للكميات والأموال",
                            fontSize = 12.5.sp,
                            color = Color(0xFF4A657D)
                        )
                    }
                    RatioIcon(modifier = Modifier.padding(start = 12.dp))
                }
            }

            // Mode Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ClayCard(
                    modifier = Modifier.weight(1f),
                    cornerRadius = 14.dp,
                    elevation = if (selectedTab == 0) 5.dp else 2.dp,
                    backgroundColor = if (selectedTab == 0) Color(0xFFE3F3F8) else Color(0xFFF7FAFD),
                    borderColor = if (selectedTab == 0) RatioTeal else Color(0xFFD6E4F0),
                    onClick = {
                        selectedTab = 0
                        mathResult = null
                    },
                    testTag = "tab_ratio_simplify"
                ) {
                    Text(
                        text = "تبسيط النسبة",
                        fontSize = 13.sp,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) RatioTeal else TextNavyDark,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }

                ClayCard(
                    modifier = Modifier.weight(1f),
                    cornerRadius = 14.dp,
                    elevation = if (selectedTab == 1) 5.dp else 2.dp,
                    backgroundColor = if (selectedTab == 1) Color(0xFFE3F3F8) else Color(0xFFF7FAFD),
                    borderColor = if (selectedTab == 1) RatioTeal else Color(0xFFD6E4F0),
                    onClick = {
                        selectedTab = 1
                        mathResult = null
                    },
                    testTag = "tab_ratio_distribute"
                ) {
                    Text(
                        text = "التقسيم التناسبي",
                        fontSize = 13.sp,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) RatioTeal else TextNavyDark,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }

            // Form Inputs Card
            ClayCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 22.dp,
                elevation = 5.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (selectedTab == 0) {
                        Text(
                            text = "أدخل حدي النسبة (أ : ب) لتبسيطها لأبسط صورة وحساب النسبة المئوية:",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ClayInputField(
                                value = ratioA,
                                onValueChange = { ratioA = it },
                                label = "الحد الأول (أ)",
                                placeholder = "مثال: 24",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                testTag = "input_ratio_a"
                            )
                            Text(
                                text = ":",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = RatioTeal
                            )
                            ClayInputField(
                                value = ratioB,
                                onValueChange = { ratioB = it },
                                label = "الحد الثاني (ب)",
                                placeholder = "مثال: 36",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                testTag = "input_ratio_b"
                            )
                        }
                    } else {
                        ClayInputField(
                            value = totalAmount,
                            onValueChange = { totalAmount = it },
                            label = "المبلغ أو الكمية الإجمالية المراد تقسيمها:",
                            placeholder = "مثال: 150000",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            testTag = "input_total_amount"
                        )

                        Text(
                            text = "أجزاء النسب لكل طرف (حصص التوزيع):",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ClayInputField(
                                value = part1,
                                onValueChange = { part1 = it },
                                label = "الطرف 1",
                                placeholder = "مثال: 2",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                testTag = "input_part_1"
                            )
                            ClayInputField(
                                value = part2,
                                onValueChange = { part2 = it },
                                label = "الطرف 2",
                                placeholder = "مثال: 3",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                testTag = "input_part_2"
                            )
                            ClayInputField(
                                value = part3,
                                onValueChange = { part3 = it },
                                label = "الطرف 3 (اختياري)",
                                placeholder = "مثال: 5",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                testTag = "input_part_3"
                            )
                        }
                    }

                    ClayButton(
                        text = "حل المسألة",
                        icon = Icons.Default.Calculate,
                        backgroundColor = RatioTeal,
                        onClick = {
                            if (selectedTab == 0) {
                                val va = ratioA.toDoubleOrNull() ?: 1.0
                                val vb = ratioB.toDoubleOrNull() ?: 1.0
                                mathResult = MathSolvers.simplifyRatio(va, vb)
                            } else {
                                val vt = totalAmount.toDoubleOrNull() ?: 1000.0
                                val vp1 = part1.toDoubleOrNull() ?: 1.0
                                val vp2 = part2.toDoubleOrNull() ?: 1.0
                                val vp3 = part3.toDoubleOrNull()
                                mathResult = MathSolvers.proportionalDistribution(vt, vp1, vp2, vp3)
                            }
                        },
                        testTag = "solve_ratio_button"
                    )
                }
            }

            // Results and Sharing
            SolutionAndShareSection(mathResult = mathResult)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
