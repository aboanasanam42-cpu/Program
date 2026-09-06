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
import com.example.ui.components.ExponentsRadicalsIcon
import com.example.ui.components.SolutionAndShareSection
import com.example.ui.components.SubScreenBrandingFooter
import com.example.ui.components.SubScreenTopBar
import com.example.ui.theme.ClayBackground
import com.example.ui.theme.ExponentCoral
import com.example.ui.theme.TextNavyDark

@Composable
fun ExponentsRadicalsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var mathResult by remember { mutableStateOf<MathResult?>(null) }

    // Mode 0: Power calculation a^n
    var baseVal by remember { mutableStateOf("2") }
    var expVal by remember { mutableStateOf("5") }

    // Mode 1: Radical simplification
    var rootNumber by remember { mutableStateOf("72") }

    // Mode 2: Exponent laws
    var lawBase by remember { mutableStateOf("3") }
    var lawM by remember { mutableStateOf("4") }
    var lawN by remember { mutableStateOf("2") }
    var lawOperation by remember { mutableStateOf("multiply") } // multiply, divide, power

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
                title = "الأسس والجذور",
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
                            text = "الأسس والجذور",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "حساب القوى، تبسيط الجذور الصماء، وتطبيق قوانين الأسس",
                            fontSize = 12.5.sp,
                            color = Color(0xFF4A657D)
                        )
                    }
                    ExponentsRadicalsIcon(modifier = Modifier.padding(start = 12.dp))
                }
            }

            // Mode Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ClayCard(
                    modifier = Modifier.weight(1f),
                    cornerRadius = 12.dp,
                    elevation = if (selectedTab == 0) 4.dp else 1.dp,
                    backgroundColor = if (selectedTab == 0) Color(0xFFFDECE8) else Color(0xFFF7FAFD),
                    borderColor = if (selectedTab == 0) ExponentCoral else Color(0xFFD6E4F0),
                    onClick = {
                        selectedTab = 0
                        mathResult = null
                    },
                    testTag = "tab_exponents_calc"
                ) {
                    Text(
                        text = "حساب الأس\n(أⁿ)",
                        fontSize = 12.sp,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) ExponentCoral else TextNavyDark,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                ClayCard(
                    modifier = Modifier.weight(1f),
                    cornerRadius = 12.dp,
                    elevation = if (selectedTab == 1) 4.dp else 1.dp,
                    backgroundColor = if (selectedTab == 1) Color(0xFFFDECE8) else Color(0xFFF7FAFD),
                    borderColor = if (selectedTab == 1) ExponentCoral else Color(0xFFD6E4F0),
                    onClick = {
                        selectedTab = 1
                        mathResult = null
                    },
                    testTag = "tab_radicals_simplify"
                ) {
                    Text(
                        text = "تبسيط الجذر\n(√س)",
                        fontSize = 12.sp,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) ExponentCoral else TextNavyDark,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                ClayCard(
                    modifier = Modifier.weight(1f),
                    cornerRadius = 12.dp,
                    elevation = if (selectedTab == 2) 4.dp else 1.dp,
                    backgroundColor = if (selectedTab == 2) Color(0xFFFDECE8) else Color(0xFFF7FAFD),
                    borderColor = if (selectedTab == 2) ExponentCoral else Color(0xFFD6E4F0),
                    onClick = {
                        selectedTab = 2
                        mathResult = null
                    },
                    testTag = "tab_exponents_laws"
                ) {
                    Text(
                        text = "قوانين الأسس\n(أ^م ، أ^ن)",
                        fontSize = 12.sp,
                        fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 2) ExponentCoral else TextNavyDark,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
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
                    when (selectedTab) {
                        0 -> {
                            Text(
                                text = "حساب القوى (الأساس مرفوع للأس):",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextNavyDark
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ClayInputField(
                                    value = baseVal,
                                    onValueChange = { baseVal = it },
                                    label = "الأساس (أ)",
                                    placeholder = "مثال: 2 أو -3",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    testTag = "input_base_val"
                                )
                                ClayInputField(
                                    value = expVal,
                                    onValueChange = { expVal = it },
                                    label = "الأس (ن)",
                                    placeholder = "مثال: 5 أو -2",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    testTag = "input_exp_val"
                                )
                            }
                        }
                        1 -> {
                            Text(
                                text = "تبسيط الجذور التربيعية (تحليل العوامل والمربعات الكاملة):",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextNavyDark
                            )
                            ClayInputField(
                                value = rootNumber,
                                onValueChange = { rootNumber = it },
                                label = "العدد تحت الجذر (س):",
                                placeholder = "مثال: 72 أو 50 أو 18",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                testTag = "input_root_number"
                            )
                        }
                        else -> {
                            Text(
                                text = "اختر قانون الأسس المراد تطبيقه:",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextNavyDark
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                ClayCard(
                                    modifier = Modifier.weight(1f),
                                    cornerRadius = 10.dp,
                                    elevation = if (lawOperation == "multiply") 3.dp else 1.dp,
                                    backgroundColor = if (lawOperation == "multiply") Color(0xFFFDECE8) else Color(0xFFF7FAFD),
                                    borderColor = if (lawOperation == "multiply") ExponentCoral else Color(0xFFD6E4F0),
                                    onClick = { lawOperation = "multiply" }
                                ) {
                                    Text(
                                        text = "الضرب\n(أ^م × أ^ن)",
                                        fontSize = 11.5.sp,
                                        fontWeight = if (lawOperation == "multiply") FontWeight.Bold else FontWeight.Normal,
                                        color = if (lawOperation == "multiply") ExponentCoral else TextNavyDark,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                                ClayCard(
                                    modifier = Modifier.weight(1f),
                                    cornerRadius = 10.dp,
                                    elevation = if (lawOperation == "divide") 3.dp else 1.dp,
                                    backgroundColor = if (lawOperation == "divide") Color(0xFFFDECE8) else Color(0xFFF7FAFD),
                                    borderColor = if (lawOperation == "divide") ExponentCoral else Color(0xFFD6E4F0),
                                    onClick = { lawOperation = "divide" }
                                ) {
                                    Text(
                                        text = "القسمة\n(أ^م ÷ أ^ن)",
                                        fontSize = 11.5.sp,
                                        fontWeight = if (lawOperation == "divide") FontWeight.Bold else FontWeight.Normal,
                                        color = if (lawOperation == "divide") ExponentCoral else TextNavyDark,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                                ClayCard(
                                    modifier = Modifier.weight(1f),
                                    cornerRadius = 10.dp,
                                    elevation = if (lawOperation == "power") 3.dp else 1.dp,
                                    backgroundColor = if (lawOperation == "power") Color(0xFFFDECE8) else Color(0xFFF7FAFD),
                                    borderColor = if (lawOperation == "power") ExponentCoral else Color(0xFFD6E4F0),
                                    onClick = { lawOperation = "power" }
                                ) {
                                    Text(
                                        text = "القوة\n((أ^م)^ن)",
                                        fontSize = 11.5.sp,
                                        fontWeight = if (lawOperation == "power") FontWeight.Bold else FontWeight.Normal,
                                        color = if (lawOperation == "power") ExponentCoral else TextNavyDark,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ClayInputField(
                                    value = lawBase,
                                    onValueChange = { lawBase = it },
                                    label = "الأساس المشترك (أ)",
                                    placeholder = "3",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f)
                                )
                                ClayInputField(
                                    value = lawM,
                                    onValueChange = { lawM = it },
                                    label = "الأس الأول (م)",
                                    placeholder = "4",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f)
                                )
                                ClayInputField(
                                    value = lawN,
                                    onValueChange = { lawN = it },
                                    label = "الأس الثاني (ن)",
                                    placeholder = "2",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    ClayButton(
                        text = "حل المسألة",
                        icon = Icons.Default.Calculate,
                        backgroundColor = ExponentCoral,
                        onClick = {
                            when (selectedTab) {
                                0 -> {
                                    val b = baseVal.toDoubleOrNull() ?: 2.0
                                    val e = expVal.toDoubleOrNull() ?: 2.0
                                    mathResult = MathSolvers.calculatePower(b, e)
                                }
                                1 -> {
                                    val n = rootNumber.toLongOrNull() ?: 72L
                                    mathResult = MathSolvers.simplifySquareRoot(n)
                                }
                                else -> {
                                    val b = lawBase.toDoubleOrNull() ?: 3.0
                                    val m = lawM.toDoubleOrNull() ?: 2.0
                                    val n = lawN.toDoubleOrNull() ?: 2.0
                                    mathResult = MathSolvers.calculateExponentLaw(b, m, n, lawOperation)
                                }
                            }
                        },
                        testTag = "solve_exponents_button"
                    )
                }
            }

            // Results and Sharing
            SolutionAndShareSection(mathResult = mathResult)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
