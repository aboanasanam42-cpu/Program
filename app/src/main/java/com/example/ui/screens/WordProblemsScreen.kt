package com.example.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.MathResult
import com.example.math.WordProblemSolver
import com.example.ui.components.BackgroundMathWatermark
import com.example.ui.components.ClayButton
import com.example.ui.components.ClayCard
import com.example.ui.components.MainFooterCard
import com.example.ui.components.SolutionAndShareSection
import com.example.ui.components.SubScreenTopBar
import com.example.ui.theme.ClayBackground
import com.example.ui.theme.ClayCardBorder
import com.example.ui.theme.ClayCardSurface
import com.example.ui.theme.HeaderBlueDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextNavyDark
import com.example.ui.theme.WordProblemAccent
import com.example.ui.theme.WordProblemCardBorder
import com.example.ui.theme.WordProblemPurple
import com.example.ui.theme.WordProblemPurpleLight

@Composable
fun WordProblemsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var questionText by remember { mutableStateOf("") }
    var mathResult by remember { mutableStateOf<MathResult?>(null) }
    var isListening by remember { mutableStateOf(false) }

    // Launcher for Android Speech Recognition
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isListening = false
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val spokenMatches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenQuery = spokenMatches?.firstOrNull()
            if (!spokenQuery.isNullOrBlank()) {
                questionText = spokenQuery
                // Auto-solve the spoken problem immediately
                mathResult = WordProblemSolver.solve(spokenQuery)
                Toast.makeText(context, "تم التقاط المسألة بنجاح وجارٍ حلها", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to trigger speech recognition
    fun launchVoiceRecognition() {
        try {
            isListening = true
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA")
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ar")
                putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "ar")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "تفضل بالتحدث بالمسألة الرياضية اللفظية...")
            }
            speechRecognizerLauncher.launch(intent)
        } catch (e: Exception) {
            isListening = false
            Toast.makeText(
                context,
                "خاصية التعرف الصوتي غير متوفرة في هذا الجهاز، يمكنك كتابة المسألة في الحقل أدناه.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ClayBackground,
        topBar = {
            SubScreenTopBar(
                title = "المسائل اللفظية",
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            BackgroundMathWatermark()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Info Header Banner
                ClayCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 20.dp,
                    elevation = 4.dp,
                    backgroundColor = WordProblemPurpleLight,
                    borderColor = WordProblemCardBorder
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(WordProblemPurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = "مسائل لفظية",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "حل المسائل اللفظية (صوتياً وكتابياً)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = WordProblemPurple
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "أدخل مسألتك كتابة أو بالصوت وسيقوم التطبيق بحلها بالتفصيل (تناسب، نسب، تشابه مثلثات، وأسس)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextNavyDark,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                // 2. Preset Word Problem Examples Carousel
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "أمثلة سريعة للمسائل (انقر لتجربتها فوراً):",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyDark
                    )

                    val presetExamples = listOf(
                        "اشترى أحمد 5 دفاتر بمبلغ 45 ريالاً، فكم يدفع إذا اشترى 12 دفتراً من نفس النوع؟",
                        "عددان موجبان النسبة بينهما 3 إلى 5 ومجموعهما 40، فما هما العددان؟",
                        "توفي رجل وترك تركة قدرها 120000 ريال وزعت بين أبنائه الثلاثة بنسبة 3 : 2 : 1، احسب نصيب كل وريث.",
                        "إذا كان 4 عمال ينجزون عملاً في 6 أيام، فكم يوماً يحتاج 8 عمال لإنجاز نفس العمل؟",
                        "مثلث أطوال أضلاعه 6 و 8 و 10 سم يشابه مثلثاً آخر أطول أضلاعه 15 سم. احسب معامل التشابه ومحيط المثلث الثاني.",
                        "مزرعة مربعة الشكل مساحتها 72 متراً مربعاً، احسب طول ضلعها في أبسط صورة جذرية."
                    )

                    val exampleTitles = listOf(
                        "تناسب طردي (دفاتر)",
                        "مجهولان ومجموعهما 40",
                        "تقسيم تركة ومبالغ",
                        "تناسب عكسي (عمال)",
                        "تشابه مثلثات",
                        "جذر مزرعة مربعة"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        exampleTitles.forEachIndexed { index, title ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .border(1.dp, WordProblemCardBorder, RoundedCornerShape(12.dp))
                                    .clickable {
                                        questionText = presetExamples[index]
                                        mathResult = WordProblemSolver.solve(presetExamples[index])
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = title,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = WordProblemPurple
                                )
                            }
                        }
                    }
                }

                // 3. Question Input Area (Text & Microphone)
                ClayCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 24.dp,
                    elevation = 6.dp,
                    backgroundColor = ClayCardSurface,
                    borderColor = WordProblemCardBorder
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "نص المسألة اللفظية:",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextNavyDark
                            )

                            // Clear button
                            if (questionText.isNotEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            questionText = ""
                                            mathResult = null
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "مسح",
                                        tint = Color.Red,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "مسح",
                                        fontSize = 12.sp,
                                        color = Color.Red,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Text Area
                        OutlinedTextField(
                            value = questionText,
                            onValueChange = { questionText = it },
                            placeholder = {
                                Text(
                                    text = "اكتب نص المسألة هنا، أو اضغط زر الميكروفون للتحدث بالمسألة مباشرة...",
                                    fontSize = 13.5.sp,
                                    color = TextMuted,
                                    lineHeight = 20.sp
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .testTag("input_word_problem_text"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = WordProblemPurple,
                                unfocusedBorderColor = ClayCardBorder,
                                focusedContainerColor = Color(0xFFFAFCFF),
                                unfocusedContainerColor = Color(0xFFFAFCFF),
                                focusedTextColor = TextNavyDark,
                                unfocusedTextColor = TextNavyDark
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    if (questionText.isNotBlank()) {
                                        mathResult = WordProblemSolver.solve(questionText)
                                    }
                                }
                            )
                        )

                        // Microphone & Action Controls Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Large Voice Input (Microphone) Button
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                WordProblemPurple,
                                                WordProblemAccent
                                            )
                                        )
                                    )
                                    .clickable {
                                        focusManager.clearFocus()
                                        launchVoiceRecognition()
                                    }
                                    .testTag("button_voice_input"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = "إدخال صوتي",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "إدخال صوتي للمسألة",
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            // Solve Button
                            ClayButton(
                                text = "حل المسألة",
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                backgroundColor = Color(0xFF337AB7),
                                contentColor = Color.White,
                                icon = Icons.Default.Calculate,
                                onClick = {
                                    focusManager.clearFocus()
                                    if (questionText.isNotBlank()) {
                                        mathResult = WordProblemSolver.solve(questionText)
                                    } else {
                                        Toast.makeText(context, "يرجى إدخال أو نطق المسألة أولاً", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                testTag = "button_solve_word_problem"
                            )
                        }
                    }
                }

                // 4. Solution & Sharing Section
                SolutionAndShareSection(mathResult = mathResult)

                // 5. Attribution & Credits Footer
                MainFooterCard()

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
