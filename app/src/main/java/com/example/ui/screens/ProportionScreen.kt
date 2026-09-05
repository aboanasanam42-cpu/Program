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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.MathResult
import com.example.math.MathSolvers
import com.example.ui.components.ClayButton
import com.example.ui.components.ClayCard
import com.example.ui.components.ClayInputField
import com.example.ui.components.ProportionsIcon
import com.example.ui.components.SolutionAndShareSection
import com.example.ui.components.SubScreenBrandingFooter
import com.example.ui.components.SubScreenTopBar
import com.example.ui.theme.ClayBackground
import com.example.ui.theme.ProportionWood
import com.example.ui.theme.TextNavyDark

@Composable
fun ProportionScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var mathResult by remember { mutableStateOf<MathResult?>(null) }

    // Mode 0: A / B = C / D (one unknown)
    var a by remember { mutableStateOf("3") }
    var b by remember { mutableStateOf("5") }
    var c by remember { mutableStateOf("12") }
    var d by remember { mutableStateOf("") } // Unknown x

    // Mode 1: Direct or Inverse (x1, y1 -> x2 -> y2)
    var x1 by remember { mutableStateOf("4") }
    var y1 by remember { mutableStateOf("20") }
    var x2 by remember { mutableStateOf("10") }
    var isDirect by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ClayBackground,
        bottomBar = {
            Box(modifier = Modifier.padding(16.dp)) {
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
                title = "التناسب",
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
                            text = "التناسب (Proportions)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "حل معادلات التناسب A/B = C/D والتناسب الطردي والعكسي",
                            fontSize = 12.5.sp,
                            color = Color(0xFF4A657D)
                        )
                    }
                    ProportionsIcon(modifier = Modifier.padding(start = 12.dp))
                }
            }

            // Mode Selection Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ClayCard(
                    modifier = Modifier.weight(1f),
                    cornerRadius = 14.dp,
                    elevation = if (selectedTab == 0) 5.dp else 2.dp,
                    backgroundColor = if (selectedTab == 0) Color(0xFFFDEEE8) else Color(0xFFF7FAFD),
                    borderColor = if (selectedTab == 0) ProportionWood else Color(0xFFD6E4F0),
                    onClick = {
                        selectedTab = 0
                        mathResult = null
                    },
                    testTag = "tab_proportion_eq"
                ) {
                    Text(
                        text = "معادلة التناسب (A/B = C/D)",
                        fontSize = 13.sp,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) ProportionWood else TextNavyDark,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }

                ClayCard(
                    modifier = Modifier.weight(1f),
                    cornerRadius = 14.dp,
                    elevation = if (selectedTab == 1) 5.dp else 2.dp,
                    backgroundColor = if (selectedTab == 1) Color(0xFFFDEEE8) else Color(0xFFF7FAFD),
                    borderColor = if (selectedTab == 1) ProportionWood else Color(0xFFD6E4F0),
                    onClick = {
                        selectedTab = 1
                        mathResult = null
                    },
                    testTag = "tab_proportion_direct_inverse"
                ) {
                    Text(
                        text = "طردي وعكسي",
                        fontSize = 13.sp,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) ProportionWood else TextNavyDark,
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
                            text = "أدخل 3 قيم واترك الخانة الرابعة فارغة لحساب المجهول (x):",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )

                        // Visual Proportional Equation layout
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Left Fraction A / B
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ClayInputField(
                                    value = a,
                                    onValueChange = { a = it },
                                    label = "الحد الأول (A)",
                                    placeholder = "x",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    testTag = "input_prop_a"
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    thickness = 2.dp,
                                    color = ProportionWood
                                )
                                ClayInputField(
                                    value = b,
                                    onValueChange = { b = it },
                                    label = "الحد الثاني (B)",
                                    placeholder = "x",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    testTag = "input_prop_b"
                                )
                            }

                            Text(
                                text = "=",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = ProportionWood,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )

                            // Right Fraction C / D
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ClayInputField(
                                    value = c,
                                    onValueChange = { c = it },
                                    label = "الحد الثالث (C)",
                                    placeholder = "x",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    testTag = "input_prop_c"
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    thickness = 2.dp,
                                    color = ProportionWood
                                )
                                ClayInputField(
                                    value = d,
                                    onValueChange = { d = it },
                                    label = "الحد الرابع (D)",
                                    placeholder = "x",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    testTag = "input_prop_d"
                                )
                            }
                        }
                    } else {
                        // Direct / Inverse switch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ClayCard(
                                modifier = Modifier.weight(1f),
                                cornerRadius = 12.dp,
                                elevation = if (isDirect) 4.dp else 1.dp,
                                backgroundColor = if (isDirect) Color(0xFFFDEEE8) else Color(0xFFF9FBFE),
                                borderColor = if (isDirect) ProportionWood else Color(0xFFD6E4F0),
                                onClick = { isDirect = true }
                            ) {
                                Text(
                                    text = "تناسب طردي (y/x = k)",
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isDirect) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isDirect) ProportionWood else TextNavyDark,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            ClayCard(
                                modifier = Modifier.weight(1f),
                                cornerRadius = 12.dp,
                                elevation = if (!isDirect) 4.dp else 1.dp,
                                backgroundColor = if (!isDirect) Color(0xFFFDEEE8) else Color(0xFFF9FBFE),
                                borderColor = if (!isDirect) ProportionWood else Color(0xFFD6E4F0),
                                onClick = { isDirect = false }
                            ) {
                                Text(
                                    text = "تناسب عكسي (y·x = k)",
                                    fontSize = 12.5.sp,
                                    fontWeight = if (!isDirect) FontWeight.Bold else FontWeight.Normal,
                                    color = if (!isDirect) ProportionWood else TextNavyDark,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ClayInputField(
                                value = x1,
                                onValueChange = { x1 = it },
                                label = "القيمة الأولى x₁",
                                placeholder = "مثال: 4",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                            ClayInputField(
                                value = y1,
                                onValueChange = { y1 = it },
                                label = "يقابلها y₁",
                                placeholder = "مثال: 20",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        ClayInputField(
                            value = x2,
                            onValueChange = { x2 = it },
                            label = "القيمة الجديدة x₂ (المطلوب حساب y₂ عندها):",
                            placeholder = "مثال: 10",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }

                    ClayButton(
                        text = "حل المسألة",
                        icon = Icons.Default.Calculate,
                        backgroundColor = ProportionWood,
                        onClick = {
                            if (selectedTab == 0) {
                                val va = a.toDoubleOrNull()
                                val vb = b.toDoubleOrNull()
                                val vc = c.toDoubleOrNull()
                                val vd = d.toDoubleOrNull()
                                mathResult = MathSolvers.solveProportion(va, vb, vc, vd)
                            } else {
                                val vx1 = x1.toDoubleOrNull() ?: 1.0
                                val vy1 = y1.toDoubleOrNull() ?: 1.0
                                val vx2 = x2.toDoubleOrNull() ?: 1.0
                                mathResult = MathSolvers.solveDirectOrInverseProportion(vx1, vy1, vx2, isDirect)
                            }
                        },
                        testTag = "solve_proportion_button"
                    )
                }
            }

            // Results and Sharing
            SolutionAndShareSection(mathResult = mathResult)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
