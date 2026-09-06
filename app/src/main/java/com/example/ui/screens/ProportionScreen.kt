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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
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
    // 0: One unknown (أ / ب = جـ / د)
    // 1: Two unknowns (س ، ص)
    // 2: Direct and Inverse (طردي وعكسي)
    var selectedTab by remember { mutableIntStateOf(0) }
    var mathResult by remember { mutableStateOf<MathResult?>(null) }

    // Mode 0: أ / ب = جـ / د (مجهول واحد)
    var a by remember { mutableStateOf("3") }
    var b by remember { mutableStateOf("5") }
    var c by remember { mutableStateOf("12") }
    var d by remember { mutableStateOf("") } // مجهول س

    // Mode 1: إيجاد مجهولين (س ، ص)
    // Sub-mode 0: خواص التناسب مع المجموع أو الفرق (س/أ = ص/ب)
    // Sub-mode 1: تناسب متسلسل (س/أ = جـ/د = ص/هـ)
    var twoUnknownsSubMode by remember { mutableIntStateOf(0) }
    // Sub-mode 0 state:
    var propDenomA by remember { mutableStateOf("3") }
    var propDenomB by remember { mutableStateOf("5") }
    var twoUnknownsOpType by remember { mutableIntStateOf(0) } // 0: مجموع (س + ص), 1: فرق (س - ص), 2: معادلة عامة
    var twoUnknownsResultVal by remember { mutableStateOf("40") }
    var coeffS by remember { mutableStateOf("1") }
    var coeffY by remember { mutableStateOf("1") }

    // Sub-mode 1 state (تناسب متسلسل):
    var chainedDenomS by remember { mutableStateOf("4") }
    var chainedNumKnown by remember { mutableStateOf("15") }
    var chainedDenomKnown by remember { mutableStateOf("5") }
    var chainedDenomY by remember { mutableStateOf("7") }

    // Mode 2: طردي وعكسي (س₁ ، ص₁ -> س₂ -> ص₂)
    var s1 by remember { mutableStateOf("4") }
    var y1 by remember { mutableStateOf("20") }
    var s2 by remember { mutableStateOf("10") }
    var isDirect by remember { mutableStateOf(true) }

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
                            text = "التناسب",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "حل معادلات التناسب (أ / ب = جـ / د)، إيجاد مجهولين (س ، ص)، والتناسب الطردي والعكسي",
                            fontSize = 12.5.sp,
                            color = Color(0xFF4A657D),
                            lineHeight = 17.sp
                        )
                    }
                    ProportionsIcon(modifier = Modifier.padding(start = 12.dp))
                }
            }

            // 3 Mode Selection Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
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
                        text = "مجهول واحد\n(أ / ب = جـ / د)",
                        fontSize = 11.5.sp,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) ProportionWood else TextNavyDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
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
                    testTag = "tab_proportion_two_unknowns"
                ) {
                    Text(
                        text = "إيجاد مجهولين\n(س ، ص)",
                        fontSize = 11.5.sp,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) ProportionWood else TextNavyDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                ClayCard(
                    modifier = Modifier.weight(1f),
                    cornerRadius = 14.dp,
                    elevation = if (selectedTab == 2) 5.dp else 2.dp,
                    backgroundColor = if (selectedTab == 2) Color(0xFFFDEEE8) else Color(0xFFF7FAFD),
                    borderColor = if (selectedTab == 2) ProportionWood else Color(0xFFD6E4F0),
                    onClick = {
                        selectedTab = 2
                        mathResult = null
                    },
                    testTag = "tab_proportion_direct_inverse"
                ) {
                    Text(
                        text = "طردي وعكسي\n(س ، ص)",
                        fontSize = 11.5.sp,
                        fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 2) ProportionWood else TextNavyDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
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
                    // TAB 0: ONE UNKNOWN
                    if (selectedTab == 0) {
                        Text(
                            text = "أدخل 3 قيم واترك الخانة الرابعة فارغة لحساب المجهول (س):",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )

                        // Visual Proportional Equation layout: أ / ب = جـ / د
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Left Fraction: أ / ب
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ClayInputField(
                                    value = a,
                                    onValueChange = { a = it },
                                    label = "الحد الأول (أ)",
                                    placeholder = "س",
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
                                    label = "الحد الثاني (ب)",
                                    placeholder = "س",
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

                            // Right Fraction: جـ / د
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ClayInputField(
                                    value = c,
                                    onValueChange = { c = it },
                                    label = "الحد الثالث (جـ)",
                                    placeholder = "س",
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
                                    label = "الحد الرابع (د)",
                                    placeholder = "س",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    testTag = "input_prop_d"
                                )
                            }
                        }

                        ClayButton(
                            text = "حل التناسب وإيجاد المجهول (س)",
                            icon = Icons.Default.Calculate,
                            backgroundColor = ProportionWood,
                            onClick = {
                                val va = a.toDoubleOrNull()
                                val vb = b.toDoubleOrNull()
                                val vc = c.toDoubleOrNull()
                                val vd = d.toDoubleOrNull()
                                mathResult = MathSolvers.solveProportion(va, vb, vc, vd)
                            },
                            testTag = "solve_proportion_button"
                        )
                    }
                    // TAB 1: TWO UNKNOWNS (س ، ص)
                    else if (selectedTab == 1) {
                        Text(
                            text = "إيجاد مجهولين في التناسب (س ، ص):",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )

                        // Sub-mode selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ClayCard(
                                modifier = Modifier.weight(1f),
                                cornerRadius = 12.dp,
                                elevation = if (twoUnknownsSubMode == 0) 4.dp else 1.dp,
                                backgroundColor = if (twoUnknownsSubMode == 0) Color(0xFFFDEEE8) else Color(0xFFF9FBFE),
                                borderColor = if (twoUnknownsSubMode == 0) ProportionWood else Color(0xFFD6E4F0),
                                onClick = {
                                    twoUnknownsSubMode = 0
                                    mathResult = null
                                }
                            ) {
                                Text(
                                    text = "خواص التناسب\n(المجموع / الفرق)",
                                    fontSize = 12.sp,
                                    fontWeight = if (twoUnknownsSubMode == 0) FontWeight.Bold else FontWeight.Normal,
                                    color = if (twoUnknownsSubMode == 0) ProportionWood else TextNavyDark,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }

                            ClayCard(
                                modifier = Modifier.weight(1f),
                                cornerRadius = 12.dp,
                                elevation = if (twoUnknownsSubMode == 1) 4.dp else 1.dp,
                                backgroundColor = if (twoUnknownsSubMode == 1) Color(0xFFFDEEE8) else Color(0xFFF9FBFE),
                                borderColor = if (twoUnknownsSubMode == 1) ProportionWood else Color(0xFFD6E4F0),
                                onClick = {
                                    twoUnknownsSubMode = 1
                                    mathResult = null
                                }
                            ) {
                                Text(
                                    text = "تناسب متسلسل\n(سلسلة نسب متساوية)",
                                    fontSize = 12.sp,
                                    fontWeight = if (twoUnknownsSubMode == 1) FontWeight.Bold else FontWeight.Normal,
                                    color = if (twoUnknownsSubMode == 1) ProportionWood else TextNavyDark,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                        }

                        if (twoUnknownsSubMode == 0) {
                            // Sub-mode 0: س / أ = ص / ب مع معادلة
                            Text(
                                text = "التناسب: (س ÷ أ = ص ÷ ب = ك)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ProportionWood
                            )

                            // Visual Fractions: س / أ = ص / ب
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                // Fraction 1: س / أ
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFFDF4E7))
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "المجهول الأول (س)",
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ProportionWood
                                        )
                                    }
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 6.dp),
                                        thickness = 2.dp,
                                        color = ProportionWood
                                    )
                                    ClayInputField(
                                        value = propDenomA,
                                        onValueChange = { propDenomA = it },
                                        label = "مقام س (أ)",
                                        placeholder = "3",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        testTag = "input_prop_denom_a"
                                    )
                                }

                                Text(
                                    text = "=",
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ProportionWood,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )

                                // Fraction 2: ص / ب
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFFDF4E7))
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "المجهول الثاني (ص)",
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ProportionWood
                                        )
                                    }
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 6.dp),
                                        thickness = 2.dp,
                                        color = ProportionWood
                                    )
                                    ClayInputField(
                                        value = propDenomB,
                                        onValueChange = { propDenomB = it },
                                        label = "مقام ص (ب)",
                                        placeholder = "5",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        testTag = "input_prop_denom_b"
                                    )
                                }
                            }

                            // Relationship selector between س and ص
                            Text(
                                text = "العلاقة المعطاة بين المجهولين:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextNavyDark
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                ClayCard(
                                    modifier = Modifier.weight(1f),
                                    cornerRadius = 10.dp,
                                    elevation = if (twoUnknownsOpType == 0) 3.dp else 1.dp,
                                    backgroundColor = if (twoUnknownsOpType == 0) Color(0xFFFDEEE8) else Color(0xFFF9FBFE),
                                    borderColor = if (twoUnknownsOpType == 0) ProportionWood else Color(0xFFD6E4F0),
                                    onClick = { twoUnknownsOpType = 0 }
                                ) {
                                    Text(
                                        text = "المجموع (س + ص)",
                                        fontSize = 11.5.sp,
                                        fontWeight = if (twoUnknownsOpType == 0) FontWeight.Bold else FontWeight.Normal,
                                        color = if (twoUnknownsOpType == 0) ProportionWood else TextNavyDark,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 6.dp)
                                    )
                                }

                                ClayCard(
                                    modifier = Modifier.weight(1f),
                                    cornerRadius = 10.dp,
                                    elevation = if (twoUnknownsOpType == 1) 3.dp else 1.dp,
                                    backgroundColor = if (twoUnknownsOpType == 1) Color(0xFFFDEEE8) else Color(0xFFF9FBFE),
                                    borderColor = if (twoUnknownsOpType == 1) ProportionWood else Color(0xFFD6E4F0),
                                    onClick = { twoUnknownsOpType = 1 }
                                ) {
                                    Text(
                                        text = "الفرق (س - ص)",
                                        fontSize = 11.5.sp,
                                        fontWeight = if (twoUnknownsOpType == 1) FontWeight.Bold else FontWeight.Normal,
                                        color = if (twoUnknownsOpType == 1) ProportionWood else TextNavyDark,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 6.dp)
                                    )
                                }

                                ClayCard(
                                    modifier = Modifier.weight(1f),
                                    cornerRadius = 10.dp,
                                    elevation = if (twoUnknownsOpType == 2) 3.dp else 1.dp,
                                    backgroundColor = if (twoUnknownsOpType == 2) Color(0xFFFDEEE8) else Color(0xFFF9FBFE),
                                    borderColor = if (twoUnknownsOpType == 2) ProportionWood else Color(0xFFD6E4F0),
                                    onClick = { twoUnknownsOpType = 2 }
                                ) {
                                    Text(
                                        text = "معادلة عامة",
                                        fontSize = 11.5.sp,
                                        fontWeight = if (twoUnknownsOpType == 2) FontWeight.Bold else FontWeight.Normal,
                                        color = if (twoUnknownsOpType == 2) ProportionWood else TextNavyDark,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 6.dp)
                                    )
                                }
                            }

                            if (twoUnknownsOpType == 0) {
                                ClayInputField(
                                    value = twoUnknownsResultVal,
                                    onValueChange = { twoUnknownsResultVal = it },
                                    label = "قيمة مجموع المجهولين: س + ص =",
                                    placeholder = "مثال: 40",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    testTag = "input_two_unknowns_sum"
                                )
                            } else if (twoUnknownsOpType == 1) {
                                ClayInputField(
                                    value = twoUnknownsResultVal,
                                    onValueChange = { twoUnknownsResultVal = it },
                                    label = "قيمة الفرق بين المجهولين: س - ص =",
                                    placeholder = "مثال: 10",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    testTag = "input_two_unknowns_diff"
                                )
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ClayInputField(
                                        value = coeffS,
                                        onValueChange = { coeffS = it },
                                        label = "معامل س",
                                        placeholder = "1",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ProportionWood)
                                    ClayInputField(
                                        value = coeffY,
                                        onValueChange = { coeffY = it },
                                        label = "معامل ص",
                                        placeholder = "1",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text("=", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ProportionWood)
                                    ClayInputField(
                                        value = twoUnknownsResultVal,
                                        onValueChange = { twoUnknownsResultVal = it },
                                        label = "الناتج",
                                        placeholder = "40",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            ClayButton(
                                text = "إيجاد المجهولين (س ، ص)",
                                icon = Icons.Default.Calculate,
                                backgroundColor = ProportionWood,
                                onClick = {
                                    val da = propDenomA.toDoubleOrNull() ?: 1.0
                                    val db = propDenomB.toDoubleOrNull() ?: 1.0
                                    val resVal = twoUnknownsResultVal.toDoubleOrNull() ?: 0.0
                                    val cs = if (twoUnknownsOpType == 2) (coeffS.toDoubleOrNull() ?: 1.0) else 1.0
                                    val cy = if (twoUnknownsOpType == 2) (coeffY.toDoubleOrNull() ?: 1.0) else 1.0
                                    val isAdd = twoUnknownsOpType != 1
                                    mathResult = MathSolvers.solveProportionTwoUnknownsWithSumDiff(
                                        denomA = da,
                                        denomB = db,
                                        coeffS = cs,
                                        coeffY = cy,
                                        isAddition = isAdd,
                                        resultValue = resVal
                                    )
                                },
                                testTag = "solve_two_unknowns_button"
                            )
                        } else {
                            // Sub-mode 1: تناسب متسلسل (س / أ = جـ / د = ص / هـ)
                            Text(
                                text = "سلسلة النسب المتساوية: (س / أ = جـ / د = ص / هـ)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ProportionWood
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Fraction 1: س / أ
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFFDF4E7))
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "س", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ProportionWood)
                                    }
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        thickness = 2.dp,
                                        color = ProportionWood
                                    )
                                    ClayInputField(
                                        value = chainedDenomS,
                                        onValueChange = { chainedDenomS = it },
                                        label = "مقام س (أ)",
                                        placeholder = "4",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                    )
                                }

                                Text(text = "=", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ProportionWood, modifier = Modifier.padding(horizontal = 4.dp))

                                // Fraction 2: جـ / د (معلوم)
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    ClayInputField(
                                        value = chainedNumKnown,
                                        onValueChange = { chainedNumKnown = it },
                                        label = "بسط (جـ)",
                                        placeholder = "15",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        thickness = 2.dp,
                                        color = ProportionWood
                                    )
                                    ClayInputField(
                                        value = chainedDenomKnown,
                                        onValueChange = { chainedDenomKnown = it },
                                        label = "مقام (د)",
                                        placeholder = "5",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                    )
                                }

                                Text(text = "=", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ProportionWood, modifier = Modifier.padding(horizontal = 4.dp))

                                // Fraction 3: ص / هـ
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFFDF4E7))
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "ص", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ProportionWood)
                                    }
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        thickness = 2.dp,
                                        color = ProportionWood
                                    )
                                    ClayInputField(
                                        value = chainedDenomY,
                                        onValueChange = { chainedDenomY = it },
                                        label = "مقام ص (هـ)",
                                        placeholder = "7",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                    )
                                }
                            }

                            ClayButton(
                                text = "إيجاد المجهولين (س ، ص) من السلسلة",
                                icon = Icons.Default.Calculate,
                                backgroundColor = ProportionWood,
                                onClick = {
                                    val ds = chainedDenomS.toDoubleOrNull() ?: 1.0
                                    val nk = chainedNumKnown.toDoubleOrNull() ?: 1.0
                                    val dk = chainedDenomKnown.toDoubleOrNull() ?: 1.0
                                    val dy = chainedDenomY.toDoubleOrNull() ?: 1.0
                                    mathResult = MathSolvers.solveChainedProportionTwoUnknowns(
                                        denomS = ds,
                                        numKnown = nk,
                                        denomKnown = dk,
                                        denomY = dy
                                    )
                                }
                            )
                        }
                    }
                    // TAB 2: DIRECT AND INVERSE (س ، ص)
                    else {
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
                                    text = "تناسب طردي (ص ÷ س = ك)",
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isDirect) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isDirect) ProportionWood else TextNavyDark,
                                    textAlign = TextAlign.Center,
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
                                    text = "تناسب عكسي (ص × س = ك)",
                                    fontSize = 12.5.sp,
                                    fontWeight = if (!isDirect) FontWeight.Bold else FontWeight.Normal,
                                    color = if (!isDirect) ProportionWood else TextNavyDark,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ClayInputField(
                                value = s1,
                                onValueChange = { s1 = it },
                                label = "القيمة الأولى (س₁)",
                                placeholder = "مثال: 4",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                            ClayInputField(
                                value = y1,
                                onValueChange = { y1 = it },
                                label = "يقابلها (ص₁)",
                                placeholder = "مثال: 20",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        ClayInputField(
                            value = s2,
                            onValueChange = { s2 = it },
                            label = "القيمة الجديدة (س₂) [المطلوب حساب ص₂ عندها]:",
                            placeholder = "مثال: 10",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )

                        ClayButton(
                            text = "حل المسألة وحساب (ص₂)",
                            icon = Icons.Default.Calculate,
                            backgroundColor = ProportionWood,
                            onClick = {
                                val vs1 = s1.toDoubleOrNull() ?: 1.0
                                val vy1 = y1.toDoubleOrNull() ?: 1.0
                                val vs2 = s2.toDoubleOrNull() ?: 1.0
                                mathResult = MathSolvers.solveDirectOrInverseProportion(vs1, vy1, vs2, isDirect)
                            },
                            testTag = "solve_proportion_direct_inverse_button"
                        )
                    }
                }
            }

            // Results and Sharing
            SolutionAndShareSection(mathResult = mathResult)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

