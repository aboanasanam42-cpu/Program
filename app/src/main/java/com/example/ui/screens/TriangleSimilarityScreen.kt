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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
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
import com.example.ui.components.SolutionAndShareSection
import com.example.ui.components.SubScreenBrandingFooter
import com.example.ui.components.SubScreenTopBar
import com.example.ui.components.TriangleSimilarityIcon
import com.example.ui.theme.ClayBackground
import com.example.ui.theme.HeaderBlue
import com.example.ui.theme.TextNavyDark
import com.example.ui.theme.TriangleBlue

@Composable
fun TriangleSimilarityScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var mathResult by remember { mutableStateOf<MathResult?>(null) }

    // Tab 0: Find missing side & scale factor
    var a1 by remember { mutableStateOf("6") }
    var b1 by remember { mutableStateOf("8") }
    var c1 by remember { mutableStateOf("10") }
    var a2 by remember { mutableStateOf("12") }

    // Tab 1: Find Triangle Area (أوجد مساحة المثلث)
    var areaMethod by remember { mutableIntStateOf(0) } // 0: Base & Height, 1: 3 sides (Heron)
    var triangleBase by remember { mutableStateOf("8") }
    var triangleHeight by remember { mutableStateOf("6") }
    var areaSideA by remember { mutableStateOf("6") }
    var areaSideB by remember { mutableStateOf("8") }
    var areaSideC by remember { mutableStateOf("10") }
    var similarityKForArea by remember { mutableStateOf("") }

    // Tab 2: Verify SSS similarity
    var s1a by remember { mutableStateOf("3") }
    var s1b by remember { mutableStateOf("4") }
    var s1c by remember { mutableStateOf("5") }
    var s2a by remember { mutableStateOf("6") }
    var s2b by remember { mutableStateOf("8") }
    var s2c by remember { mutableStateOf("10") }

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
                title = "تشابه المثلثات",
                onBack = onBack
            )

            // Header Icon and Info
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
                            text = "تشابه المثلثات (Triangle Similarity)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "حساب تناسب الأضلاع، إيجاد مساحة المثلث، ونسب المساحات والمحيطات",
                            fontSize = 12.5.sp,
                            color = Color(0xFF4A657D)
                        )
                    }
                    TriangleSimilarityIcon(modifier = Modifier.padding(start = 12.dp))
                }
            }

            // Mode Selector Tabs (3 Options)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ClayCard(
                        modifier = Modifier.weight(1f),
                        cornerRadius = 14.dp,
                        elevation = if (selectedTab == 0) 5.dp else 2.dp,
                        backgroundColor = if (selectedTab == 0) Color(0xFFE5F1FC) else Color(0xFFF7FAFD),
                        borderColor = if (selectedTab == 0) TriangleBlue else Color(0xFFD6E4F0),
                        onClick = {
                            selectedTab = 0
                            mathResult = null
                        },
                        testTag = "tab_similarity_calc"
                    ) {
                        Text(
                            text = "حساب الأضلاع و k",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) TriangleBlue else TextNavyDark,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }

                    ClayCard(
                        modifier = Modifier.weight(1.15f),
                        cornerRadius = 14.dp,
                        elevation = if (selectedTab == 1) 5.dp else 2.dp,
                        backgroundColor = if (selectedTab == 1) Color(0xFFE5F1FC) else Color(0xFFF7FAFD),
                        borderColor = if (selectedTab == 1) TriangleBlue else Color(0xFFD6E4F0),
                        onClick = {
                            selectedTab = 1
                            mathResult = null
                        },
                        testTag = "tab_triangle_area"
                    ) {
                        Text(
                            text = "أوجد مساحة المثلث 📐",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) TriangleBlue else TextNavyDark,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }

                ClayCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 14.dp,
                    elevation = if (selectedTab == 2) 5.dp else 2.dp,
                    backgroundColor = if (selectedTab == 2) Color(0xFFE5F1FC) else Color(0xFFF7FAFD),
                    borderColor = if (selectedTab == 2) TriangleBlue else Color(0xFFD6E4F0),
                    onClick = {
                        selectedTab = 2
                        mathResult = null
                    },
                    testTag = "tab_similarity_verify"
                ) {
                    Text(
                        text = "التحقق من التشابه بموجب الأضلاع (SSS)",
                        fontSize = 13.sp,
                        fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 2) TriangleBlue else TextNavyDark,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }

            // Input Form Card
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
                            text = "أضلاع المثلث الأول:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ClayInputField(
                                value = a1,
                                onValueChange = { a1 = it },
                                label = "الضلع أ₁",
                                placeholder = "مثال: 6",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                testTag = "input_a1"
                            )
                            ClayInputField(
                                value = b1,
                                onValueChange = { b1 = it },
                                label = "الضلع ب₁",
                                placeholder = "مثال: 8",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                testTag = "input_b1"
                            )
                            ClayInputField(
                                value = c1,
                                onValueChange = { c1 = it },
                                label = "الضلع ج₁ (اختياري)",
                                placeholder = "مثال: 10",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                testTag = "input_c1"
                            )
                        }

                        Text(
                            text = "الضلع المناظر في المثلث الثاني المشابه:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )
                        ClayInputField(
                            value = a2,
                            onValueChange = { a2 = it },
                            label = "الضلع أ₂ (المناظر لـ أ₁)",
                            placeholder = "مثال: 12",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            testTag = "input_a2"
                        )
                    } else if (selectedTab == 1) {
                        // "أوجد مساحة المثلث"
                        Text(
                            text = "طريقة حساب مساحة المثلث:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ClayCard(
                                modifier = Modifier.weight(1f),
                                cornerRadius = 12.dp,
                                elevation = if (areaMethod == 0) 4.dp else 1.dp,
                                backgroundColor = if (areaMethod == 0) Color(0xFFE5F1FC) else Color(0xFFF7FAFD),
                                borderColor = if (areaMethod == 0) TriangleBlue else Color(0xFFD6E4F0),
                                onClick = { areaMethod = 0 }
                            ) {
                                Text(
                                    text = "القاعدة والارتفاع (½×ق×ع)",
                                    fontSize = 12.sp,
                                    fontWeight = if (areaMethod == 0) FontWeight.Bold else FontWeight.Normal,
                                    color = if (areaMethod == 0) TriangleBlue else TextNavyDark,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                                )
                            }

                            ClayCard(
                                modifier = Modifier.weight(1f),
                                cornerRadius = 12.dp,
                                elevation = if (areaMethod == 1) 4.dp else 1.dp,
                                backgroundColor = if (areaMethod == 1) Color(0xFFE5F1FC) else Color(0xFFF7FAFD),
                                borderColor = if (areaMethod == 1) TriangleBlue else Color(0xFFD6E4F0),
                                onClick = { areaMethod = 1 }
                            ) {
                                Text(
                                    text = "الأضلاع الثلاثة (هيرون)",
                                    fontSize = 12.sp,
                                    fontWeight = if (areaMethod == 1) FontWeight.Bold else FontWeight.Normal,
                                    color = if (areaMethod == 1) TriangleBlue else TextNavyDark,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                                )
                            }
                        }

                        if (areaMethod == 0) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ClayInputField(
                                    value = triangleBase,
                                    onValueChange = { triangleBase = it },
                                    label = "طول القاعدة (ق)",
                                    placeholder = "مثال: 8",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    testTag = "input_area_base"
                                )
                                ClayInputField(
                                    value = triangleHeight,
                                    onValueChange = { triangleHeight = it },
                                    label = "الارتفاع (ع)",
                                    placeholder = "مثال: 6",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    testTag = "input_area_height"
                                )
                            }
                        } else {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ClayInputField(
                                    value = areaSideA,
                                    onValueChange = { areaSideA = it },
                                    label = "الضلع أ",
                                    placeholder = "مثال: 6",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f)
                                )
                                ClayInputField(
                                    value = areaSideB,
                                    onValueChange = { areaSideB = it },
                                    label = "الضلع ب",
                                    placeholder = "مثال: 8",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f)
                                )
                                ClayInputField(
                                    value = areaSideC,
                                    onValueChange = { areaSideC = it },
                                    label = "الضلع ج",
                                    placeholder = "مثال: 10",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        ClayInputField(
                            value = similarityKForArea,
                            onValueChange = { similarityKForArea = it },
                            label = "معامل التشابه k (اختياري لحساب مساحة المشابه)",
                            placeholder = "مثال: 2 (لحساب مساحة المشابه بنسبة k²)",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            testTag = "input_area_k"
                        )
                    } else {
                        Text(
                            text = "أضلاع المثلث الأول (أ₁ ، ب₁ ، ج₁):",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ClayInputField(
                                value = s1a,
                                onValueChange = { s1a = it },
                                label = "أ₁",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                            ClayInputField(
                                value = s1b,
                                onValueChange = { s1b = it },
                                label = "ب₁",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                            ClayInputField(
                                value = s1c,
                                onValueChange = { s1c = it },
                                label = "ج₁",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Text(
                            text = "أضلاع المثلث الثاني (أ₂ ، ب₂ ، ج₂):",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ClayInputField(
                                value = s2a,
                                onValueChange = { s2a = it },
                                label = "أ₂",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                            ClayInputField(
                                value = s2b,
                                onValueChange = { s2b = it },
                                label = "ب₂",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                            ClayInputField(
                                value = s2c,
                                onValueChange = { s2c = it },
                                label = "ج₂",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    ClayButton(
                        text = if (selectedTab == 1) "أوجد مساحة المثلث" else "حل المسألة",
                        icon = Icons.Default.Calculate,
                        backgroundColor = TriangleBlue,
                        onClick = {
                            if (selectedTab == 0) {
                                val va1 = a1.toDoubleOrNull() ?: 1.0
                                val vb1 = b1.toDoubleOrNull() ?: 0.0
                                val vc1 = c1.toDoubleOrNull()
                                val va2 = a2.toDoubleOrNull() ?: 1.0
                                mathResult = MathSolvers.solveTriangleSides(va1, vb1, vc1, va2, null, null)
                            } else if (selectedTab == 1) {
                                val kVal = similarityKForArea.toDoubleOrNull()
                                if (areaMethod == 0) {
                                    val baseVal = triangleBase.toDoubleOrNull() ?: 0.0
                                    val heightVal = triangleHeight.toDoubleOrNull() ?: 0.0
                                    mathResult = MathSolvers.calculateTriangleAreaBaseHeight(baseVal, heightVal, kVal)
                                } else {
                                    val sa = areaSideA.toDoubleOrNull() ?: 0.0
                                    val sb = areaSideB.toDoubleOrNull() ?: 0.0
                                    val sc = areaSideC.toDoubleOrNull() ?: 0.0
                                    mathResult = MathSolvers.calculateTriangleAreaHeron(sa, sb, sc, kVal)
                                }
                            } else {
                                val v1a = s1a.toDoubleOrNull() ?: 1.0
                                val v1b = s1b.toDoubleOrNull() ?: 1.0
                                val v1c = s1c.toDoubleOrNull() ?: 1.0
                                val v2a = s2a.toDoubleOrNull() ?: 1.0
                                val v2b = s2b.toDoubleOrNull() ?: 1.0
                                val v2c = s2c.toDoubleOrNull() ?: 1.0
                                mathResult = MathSolvers.verifyTriangleSimilaritySSS(v1a, v1b, v1c, v2a, v2b, v2c)
                            }
                        },
                        testTag = "solve_triangle_button"
                    )
                }
            }

            // Results, WhatsApp and PDF Section
            SolutionAndShareSection(mathResult = mathResult)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
