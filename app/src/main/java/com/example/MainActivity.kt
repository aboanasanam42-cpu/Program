package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.ui.screens.ExponentsRadicalsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MathSection
import com.example.ui.screens.ProportionScreen
import com.example.ui.screens.RatioScreen
import com.example.ui.screens.TriangleSimilarityScreen
import com.example.ui.theme.ClayBackground
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // RTL Support (Arabic layout by default)
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = ClayBackground
                    ) {
                        DrMalikMathApp()
                    }
                }
            }
        }
    }
}

@Composable
fun DrMalikMathApp() {
    var currentSection by remember { mutableStateOf(MathSection.HOME) }

    // Back handling: if on a sub-screen, return to HOME
    BackHandler(enabled = currentSection != MathSection.HOME) {
        currentSection = MathSection.HOME
    }

    AnimatedContent(
        targetState = currentSection,
        transitionSpec = {
            if (targetState == MathSection.HOME) {
                (slideInHorizontally { width -> -width } + fadeIn())
                    .togetherWith(slideOutHorizontally { width -> width } + fadeOut())
            } else {
                (slideInHorizontally { width -> width } + fadeIn())
                    .togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
            }
        },
        label = "math_screen_transition"
    ) { section ->
        when (section) {
            MathSection.HOME -> {
                HomeScreen(
                    onNavigateToSection = { newSection ->
                        currentSection = newSection
                    }
                )
            }
            MathSection.TRIANGLE_SIMILARITY -> {
                TriangleSimilarityScreen(
                    onBack = { currentSection = MathSection.HOME }
                )
            }
            MathSection.PROPORTIONS -> {
                ProportionScreen(
                    onBack = { currentSection = MathSection.HOME }
                )
            }
            MathSection.RATIOS -> {
                RatioScreen(
                    onBack = { currentSection = MathSection.HOME }
                )
            }
            MathSection.EXPONENTS_RADICALS -> {
                ExponentsRadicalsScreen(
                    onBack = { currentSection = MathSection.HOME }
                )
            }
        }
    }
}
