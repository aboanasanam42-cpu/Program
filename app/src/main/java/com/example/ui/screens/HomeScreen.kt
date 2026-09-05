package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BackgroundMathWatermark
import com.example.ui.components.ClayCard
import com.example.ui.components.ExponentsRadicalsIcon
import com.example.ui.components.MainFooterCard
import com.example.ui.components.MainHeaderBar
import com.example.ui.components.ProportionsIcon
import com.example.ui.components.RatioIcon
import com.example.ui.components.TriangleSimilarityIcon
import com.example.ui.theme.TextNavyDark

enum class MathSection {
    HOME,
    TRIANGLE_SIMILARITY,
    PROPORTIONS,
    RATIOS,
    EXPONENTS_RADICALS
}

@Composable
fun HomeScreen(
    onNavigateToSection: (MathSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Background with subtle mathematical watermark formulas
            BackgroundMathWatermark()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Top Header Bar: "DR/MALIK" with drafting compass and calculator
                MainHeaderBar(
                    modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                )

                // 2. The 4 Interactive Cards (2x2 Grid)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Row 1: تشابه المثلثات & التناسب
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Card 1: تشابه المثلثات
                        ClayCard(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(0.92f),
                            cornerRadius = 28.dp,
                            elevation = 7.dp,
                            onClick = { onNavigateToSection(MathSection.TRIANGLE_SIMILARITY) },
                            testTag = "card_triangle_similarity"
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                TriangleSimilarityIcon()
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "تشابه\nالمثلثات",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextNavyDark,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 24.sp
                                )
                            }
                        }

                        // Card 2: التناسب
                        ClayCard(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(0.92f),
                            cornerRadius = 28.dp,
                            elevation = 7.dp,
                            onClick = { onNavigateToSection(MathSection.PROPORTIONS) },
                            testTag = "card_proportions"
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                ProportionsIcon()
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "التناسب",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextNavyDark,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Row 2: النسبة & الأسس والجذور
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Card 3: النسبة
                        ClayCard(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(0.92f),
                            cornerRadius = 28.dp,
                            elevation = 7.dp,
                            onClick = { onNavigateToSection(MathSection.RATIOS) },
                            testTag = "card_ratios"
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                RatioIcon()
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "النسبة",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextNavyDark,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Card 4: الأسس والجذور
                        ClayCard(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(0.92f),
                            cornerRadius = 28.dp,
                            elevation = 7.dp,
                            onClick = { onNavigateToSection(MathSection.EXPONENTS_RADICALS) },
                            testTag = "card_exponents_radicals"
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                ExponentsRadicalsIcon()
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "الأسس\nوالجذور",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextNavyDark,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 24.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Bottom Footer Card with 3-line attribution
                MainFooterCard(
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        }
    }
}
