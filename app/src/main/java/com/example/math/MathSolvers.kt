package com.example.math

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt

object MathSolvers {

    private val df = DecimalFormat("#.####", DecimalFormatSymbols(Locale.US))

    private fun formatNumber(value: Double): String {
        return if (value == value.roundToLong().toDouble()) {
            value.roundToLong().toString()
        } else {
            df.format(value)
        }
    }

    private fun gcd(a: Long, b: Long): Long {
        var n1 = abs(a)
        var n2 = abs(b)
        while (n2 != 0L) {
            val temp = n2
            n2 = n1 % n2
            n1 = temp
        }
        return if (n1 == 0L) 1L else n1
    }

    // -------------------------------------------------------------
    // 1. تشابه المثلثات (Triangle Similarity)
    // -------------------------------------------------------------
    fun solveTriangleSides(
        a1: Double, b1: Double, c1: Double?,
        a2: Double, b2: Double?, c2: Double?
    ): MathResult {
        val steps = mutableListOf<String>()

        if (a1 <= 0 || a2 <= 0) {
            return MathResult(
                title = "تشابه المثلثات",
                problemSummary = "أطوال الأضلاع المدخلة غير صحيحة",
                steps = listOf("يجب أن تكون أطوال الأضلاع أعداداً موجبة تماماً."),
                finalAnswer = "خطأ في المدخلات"
            )
        }

        // معامل التشابه k = a2 / a1
        val k = a2 / a1
        val kFormatted = formatNumber(k)
        steps.add("حساب معامل التشابه (نسبة التكبير/التصغير): k = الضلع المناظر ÷ الضلع الأصلي = ${formatNumber(a2)} ÷ ${formatNumber(a1)} = $kFormatted")

        val relationType = when {
            abs(k - 1.0) < 0.0001 -> "تطابق (النسبة = 1)"
            k > 1.0 -> "تكبير بمقدار $kFormatted ضعفاً"
            else -> "تصغير بمقدار $kFormatted"
        }
        steps.add("نوع العلاقة الهندسية: $relationType")

        var calcB2Str = ""
        if (b1 > 0) {
            val calculatedB2 = b1 * k
            calcB2Str = "الضلع المناظر الثاني: الضلع 2 = الضلع 1 × k = ${formatNumber(b1)} × $kFormatted = ${formatNumber(calculatedB2)}"
            steps.add(calcB2Str)
        }

        var calcC2Str = ""
        if (c1 != null && c1 > 0) {
            val calculatedC2 = c1 * k
            calcC2Str = "الضلع المناظر الثالث: الضلع 3 = الضلع 1 × k = ${formatNumber(c1)} × $kFormatted = ${formatNumber(calculatedC2)}"
            steps.add(calcC2Str)
        }

        val kSquared = k * k
        steps.add("نسبة المحيطين = معامل التشابه k = $kFormatted")
        steps.add("نسبة المساحتين = k² = ($kFormatted)² = ${formatNumber(kSquared)}")

        val summary = StringBuilder()
        summary.append("مثلث أول بأضلاع: أ = ${formatNumber(a1)}")
        if (b1 > 0) summary.append("، ب = ${formatNumber(b1)}")
        if (c1 != null && c1 > 0) summary.append("، ج = ${formatNumber(c1)}")
        summary.append("\nمثلث ثانٍ مشابه بضلع مناظر: أ' = ${formatNumber(a2)}")

        val finalAns = StringBuilder()
        finalAns.append("معامل التشابه k = $kFormatted ($relationType)\n")
        if (b1 > 0) finalAns.append("الضلع ب' = ${formatNumber(b1 * k)}\n")
        if (c1 != null && c1 > 0) finalAns.append("الضلع ج' = ${formatNumber(c1 * k)}\n")
        finalAns.append("نسبة المساحتين = ${formatNumber(kSquared)}")

        return MathResult(
            title = "تشابه المثلثات (حساب الأضلاع والنسب)",
            problemSummary = summary.toString(),
            steps = steps,
            finalAnswer = finalAns.toString().trim()
        )
    }

    // -------------------------------------------------------------
    // إيجاد مساحة المثلث (Triangle Area Calculations)
    // -------------------------------------------------------------
    fun calculateTriangleAreaBaseHeight(base: Double, height: Double, scaleFactorK: Double? = null): MathResult {
        val steps = mutableListOf<String>()
        if (base <= 0 || height <= 0) {
            return MathResult(
                title = "أوجد مساحة المثلث",
                problemSummary = "القاعدة: ${formatNumber(base)} ، الارتفاع: ${formatNumber(height)}",
                steps = listOf("يجب أن تكون قيمتا القاعدة والارتفاع أكبر من الصفر."),
                finalAnswer = "قيم غير صحيحة"
            )
        }

        val area = 0.5 * base * height
        val areaFormatted = formatNumber(area)

        steps.add("قانون مساحة المثلث: المساحة = ½ × طول القاعدة × الارتفاع")
        steps.add("التعويض بالقيم: المساحة = 0.5 × ${formatNumber(base)} × ${formatNumber(height)}")
        steps.add("حساب الناتج: المساحة = $areaFormatted وحدة مربعة")

        var finalAns = "مساحة المثلث = $areaFormatted وحدة مربعة"

        if (scaleFactorK != null && scaleFactorK > 0) {
            val kSquared = scaleFactorK * scaleFactorK
            val similarArea = area * kSquared
            steps.add("بما أن نسبة المساحات في المثلثات المتشابهة = k² (مربع معامل التشابه):")
            steps.add("k² = (${formatNumber(scaleFactorK)})² = ${formatNumber(kSquared)}")
            steps.add("مساحة المثلث المشابه الثاني = مساحة المثلث الأول × k² = $areaFormatted × ${formatNumber(kSquared)} = ${formatNumber(similarArea)}")
            finalAns += "\nمساحة المثلث المشابه (معامل k = ${formatNumber(scaleFactorK)}): ${formatNumber(similarArea)} وحدة مربعة"
        }

        return MathResult(
            title = "أوجد مساحة المثلث (بمعلومية القاعدة والارتفاع)",
            problemSummary = "طول القاعدة = ${formatNumber(base)}\nالارتفاع = ${formatNumber(height)}" +
                    (if (scaleFactorK != null && scaleFactorK > 0) "\nمعامل التشابه k = ${formatNumber(scaleFactorK)}" else ""),
            steps = steps,
            finalAnswer = finalAns
        )
    }

    fun calculateTriangleAreaHeron(a: Double, b: Double, c: Double, scaleFactorK: Double? = null): MathResult {
        val steps = mutableListOf<String>()
        if (a <= 0 || b <= 0 || c <= 0) {
            return MathResult(
                title = "أوجد مساحة المثلث",
                problemSummary = "الأضلاع: أ = ${formatNumber(a)} ، ب = ${formatNumber(b)} ، ج = ${formatNumber(c)}",
                steps = listOf("يجب أن تكون أطوال الأضلاع الثلاثة أعداداً موجبة تماماً."),
                finalAnswer = "أطوال غير صحيحة"
            )
        }

        // Check triangle inequality
        if (a + b <= c || a + c <= b || b + c <= a) {
            return MathResult(
                title = "أوجد مساحة المثلث (صيغة هيرون)",
                problemSummary = "الأضلاع: (${formatNumber(a)}، ${formatNumber(b)}، ${formatNumber(c)})",
                steps = listOf(
                    "التحقق من متباينة المثلث: مجموع طولي أي ضلعين يجب أن يكون أكبر من طول الضلع الثالث.",
                    "هذه الأطوال لا تشكّل مثلثاً لأن مجموع الضلعين لا يتجاوز طول الضلع الثالث."
                ),
                finalAnswer = "الأطوال المدخلة لا تمثل مثلثاً حقيقياً"
            )
        }

        val perimeter = a + b + c
        val s = perimeter / 2.0
        val areaSquared = s * (s - a) * (s - b) * (s - c)
        val area = sqrt(areaSquared)
        val areaFormatted = formatNumber(area)

        steps.add("حساب محيط المثلث: المحيط = ${formatNumber(a)} + ${formatNumber(b)} + ${formatNumber(c)} = ${formatNumber(perimeter)}")
        steps.add("حساب نصف المحيط (s): s = المحيط ÷ 2 = ${formatNumber(perimeter)} ÷ 2 = ${formatNumber(s)}")
        steps.add("صيغة هيرون (Heron's Formula): المساحة = √(s × (s - أ) × (s - ب) × (s - ج))")
        steps.add("التعويض: المساحة = √(${formatNumber(s)} × (${formatNumber(s)} - ${formatNumber(a)}) × (${formatNumber(s)} - ${formatNumber(b)}) × (${formatNumber(s)} - ${formatNumber(c)}))")
        steps.add("حاصل الضرب داخل الجذر = ${formatNumber(areaSquared)}")
        steps.add("جذر الناتج: المساحة = $areaFormatted وحدة مربعة")

        var finalAns = "مساحة المثلث = $areaFormatted وحدة مربعة"

        if (scaleFactorK != null && scaleFactorK > 0) {
            val kSquared = scaleFactorK * scaleFactorK
            val similarArea = area * kSquared
            steps.add("نسبة مساحتي المثلثين المتشابهين تساوي k²:")
            steps.add("مساحة المثلث المشابه = $areaFormatted × (${formatNumber(scaleFactorK)})² = ${formatNumber(similarArea)}")
            finalAns += "\nمساحة المثلث المشابه الثاني = ${formatNumber(similarArea)} وحدة مربعة"
        }

        return MathResult(
            title = "أوجد مساحة المثلث (صيغة هيرون للأضلاع الثلاثة)",
            problemSummary = "أطوال أضلاع المثلث: أ = ${formatNumber(a)} ، ب = ${formatNumber(b)} ، ج = ${formatNumber(c)}" +
                    (if (scaleFactorK != null && scaleFactorK > 0) "\nمعامل التشابه k = ${formatNumber(scaleFactorK)}" else ""),
            steps = steps,
            finalAnswer = finalAns
        )
    }

    fun verifyTriangleSimilaritySSS(
        a1: Double, b1: Double, c1: Double,
        a2: Double, b2: Double, c2: Double
    ): MathResult {
        val steps = mutableListOf<String>()

        val sides1 = listOf(a1, b1, c1).sorted()
        val sides2 = listOf(a2, b2, c2).sorted()

        steps.add("ترتيب أطوال أضلاع المثلث الأول تصاعدياً: ${formatNumber(sides1[0])}، ${formatNumber(sides1[1])}، ${formatNumber(sides1[2])}")
        steps.add("ترتيب أطوال أضلاع المثلث الثاني تصاعدياً: ${formatNumber(sides2[0])}، ${formatNumber(sides2[1])}، ${formatNumber(sides2[2])}")

        val r1 = sides2[0] / sides1[0]
        val r2 = sides2[1] / sides1[1]
        val r3 = sides2[2] / sides1[2]

        steps.add("النسبة الأولى بين أقصر ضلعين: ${formatNumber(sides2[0])} ÷ ${formatNumber(sides1[0])} = ${formatNumber(r1)}")
        steps.add("النسبة الثانية بين الضلعين الأوسطين: ${formatNumber(sides2[1])} ÷ ${formatNumber(sides1[1])} = ${formatNumber(r2)}")
        steps.add("النسبة الثالثة بين أطول ضلعين: ${formatNumber(sides2[2])} ÷ ${formatNumber(sides1[2])} = ${formatNumber(r3)}")

        val isSimilar = abs(r1 - r2) < 0.001 && abs(r2 - r3) < 0.001
        val finalAnswer: String

        if (isSimilar) {
            steps.add("بما أن جميع النسب بين الأضلاع المتناظرة متساوية (k = ${formatNumber(r1)})، فإن المثلثين متشابهان بحالة تناسب الأضلاع الثلاثة (SSS).")
            finalAnswer = "المثلثان متشابهان بموجب حالة (SSS) بمعامل تشابه k = ${formatNumber(r1)}"
        } else {
            steps.add("بما أن النسب بين الأضلاع المتناظرة غير متساوية، فإن أطوال الأضلاع غير متناسبة.")
            finalAnswer = "المثلثان غير متشابهين لعدم تناسب الأضلاع المتناظرة"
        }

        return MathResult(
            title = "التحقق من تشابه المثلثات (بحالة الأضلاع الثلاثة SSS)",
            problemSummary = "المثلث الأول: (${formatNumber(a1)}, ${formatNumber(b1)}, ${formatNumber(c1)})\nالمثلث الثاني: (${formatNumber(a2)}, ${formatNumber(b2)}, ${formatNumber(c2)})",
            steps = steps,
            finalAnswer = finalAnswer
        )
    }

    // -------------------------------------------------------------
    // 2. التناسب (Proportions)
    // -------------------------------------------------------------
    fun solveProportion(
        a: Double?, b: Double?, c: Double?, d: Double?
    ): MathResult {
        val steps = mutableListOf<String>()
        val unknownCount = listOf(a, b, c, d).count { it == null }

        if (unknownCount != 1) {
            return MathResult(
                title = "حل التناسب",
                problemSummary = "التناسب: A / B = C / D",
                steps = listOf("يجب إدخال 3 قيم وترك قيمة واحدة مجهولة (x)."),
                finalAnswer = "يرجى تحديد مجهول واحد فقط"
            )
        }

        steps.add("التناسب المعطى: A / B = C / D")
        steps.add("تطبيق قاعدة التناسب الأساسية: حاصل ضرب الطرفين = حاصل ضرب الوسطين (A × D = B × C)")

        val finalX: Double
        val unknownName: String
        val equationStr: String

        when {
            a == null -> {
                unknownName = "A (الطرف الأول)"
                val num = b!! * c!!
                finalX = num / d!!
                equationStr = "A = (B × C) ÷ D = (${formatNumber(b)} × ${formatNumber(c)}) ÷ ${formatNumber(d)}"
                steps.add("عزل المجهول A: $equationStr")
                steps.add("A = ${formatNumber(num)} ÷ ${formatNumber(d)} = ${formatNumber(finalX)}")
            }
            b == null -> {
                unknownName = "B (الوسط الأول)"
                val num = a * d!!
                finalX = num / c!!
                equationStr = "B = (A × D) ÷ C = (${formatNumber(a)} × ${formatNumber(d)}) ÷ ${formatNumber(c)}"
                steps.add("عزل المجهول B: $equationStr")
                steps.add("B = ${formatNumber(num)} ÷ ${formatNumber(c)} = ${formatNumber(finalX)}")
            }
            c == null -> {
                unknownName = "C (الوسط الثاني)"
                val num = a * d!!
                finalX = num / b
                equationStr = "C = (A × D) ÷ B = (${formatNumber(a)} × ${formatNumber(d)}) ÷ ${formatNumber(b)}"
                steps.add("عزل المجهول C: $equationStr")
                steps.add("C = ${formatNumber(num)} ÷ ${formatNumber(b)} = ${formatNumber(finalX)}")
            }
            else -> {
                unknownName = "D (الطرف الثاني)"
                val num = b * c
                finalX = num / a
                equationStr = "D = (B × C) ÷ A = (${formatNumber(b)} × ${formatNumber(c)}) ÷ ${formatNumber(a)}"
                steps.add("عزل المجهول D: $equationStr")
                steps.add("D = ${formatNumber(num)} ÷ ${formatNumber(a)} = ${formatNumber(finalX)}")
            }
        }

        val aStr = if (a == null) "x" else formatNumber(a)
        val bStr = if (b == null) "x" else formatNumber(b)
        val cStr = if (c == null) "x" else formatNumber(c)
        val dStr = if (d == null) "x" else formatNumber(d)

        steps.add("التحقق بالتعويض: ($aStr ÷ $bStr) = ($cStr ÷ $dStr)")

        return MathResult(
            title = "حل التناسب (إيجاد الحد المجهول)",
            problemSummary = "المعادلة: $aStr / $bStr = $cStr / $dStr",
            steps = steps,
            finalAnswer = "قيمة المجهول $unknownName = ${formatNumber(finalX)}"
        )
    }

    fun solveDirectOrInverseProportion(
        x1: Double, y1: Double, x2: Double, isDirect: Boolean
    ): MathResult {
        val steps = mutableListOf<String>()
        val propTypeStr = if (isDirect) "تناسب طردي" else "تناسب عكسي"

        if (isDirect) {
            steps.add("في التناسب الطردي: النسبة بين المتغيرين ثابتة (y₁ ÷ x₁ = y₂ ÷ x₂ = k)")
            val k = y1 / x1
            steps.add("ثابت التناسب الطردي k = ${formatNumber(y1)} ÷ ${formatNumber(x1)} = ${formatNumber(k)}")
            val y2 = k * x2
            steps.add("حساب y₂ عند x₂ = ${formatNumber(x2)}: y₂ = k × x₂ = ${formatNumber(k)} × ${formatNumber(x2)} = ${formatNumber(y2)}")
            return MathResult(
                title = "التناسب الطردي",
                problemSummary = "x₁ = ${formatNumber(x1)} يقابلها y₁ = ${formatNumber(y1)}\nالمطلوب: إيجاد y₂ عندما x₂ = ${formatNumber(x2)}",
                steps = steps,
                finalAnswer = "y₂ = ${formatNumber(y2)} (ثابت التناسب k = ${formatNumber(k)})"
            )
        } else {
            steps.add("في التناسب العكسي: حاصل ضرب المتغيرين ثابت (x₁ × y₁ = x₂ × y₂ = k)")
            val k = x1 * y1
            steps.add("ثابت التناسب العكسي k = ${formatNumber(x1)} × ${formatNumber(y1)} = ${formatNumber(k)}")
            val y2 = k / x2
            steps.add("حساب y₂ عند x₂ = ${formatNumber(x2)}: y₂ = k ÷ x₂ = ${formatNumber(k)} ÷ ${formatNumber(x2)} = ${formatNumber(y2)}")
            return MathResult(
                title = "التناسب العكسي",
                problemSummary = "x₁ = ${formatNumber(x1)} يقابلها y₁ = ${formatNumber(y1)}\nالمطلوب: إيجاد y₂ عندما x₂ = ${formatNumber(x2)}",
                steps = steps,
                finalAnswer = "y₂ = ${formatNumber(y2)} (ثابت التناسب k = ${formatNumber(k)})"
            )
        }
    }

    // -------------------------------------------------------------
    // 3. النسبة (Ratios)
    // -------------------------------------------------------------
    fun simplifyRatio(a: Double, b: Double): MathResult {
        val steps = mutableListOf<String>()

        steps.add("النسبة الأصلية: ${formatNumber(a)} : ${formatNumber(b)}")

        // تحويل الكسور إلى أعداد صحيحة إذا وجدت
        var mult = 1.0
        while (a * mult != (a * mult).roundToLong().toDouble() || b * mult != (b * mult).roundToLong().toDouble()) {
            mult *= 10.0
            if (mult > 100000) break
        }

        val intA = (a * mult).roundToLong()
        val intB = (b * mult).roundToLong()

        if (mult > 1.0) {
            steps.add("ضرب حدي النسبة في $mult لتحويلها لأعداد صحيحة: $intA : $intB")
        }

        val g = gcd(intA, intB)
        steps.add("إيجاد القاسم المشترك الأكبر (ق.م.أ) للعددين $intA و $intB: ق.م.أ = $g")

        val simpA = intA / g
        val simpB = intB / g
        steps.add("قسمة كل حد على ق.م.أ ($g): ($intA ÷ $g) : ($intB ÷ $g) = $simpA : $simpB")

        val percent = (a / b) * 100.0
        steps.add("الصيغة الكسرية: $simpA / $simpB = ${formatNumber(a / b)}")
        steps.add("النسبة المئوية المكافئة: (${formatNumber(a)} ÷ ${formatNumber(b)}) × 100 = ${formatNumber(percent)}%")

        return MathResult(
            title = "تبسيط النسبة",
            problemSummary = "النسبة المراد تبسيطها: ${formatNumber(a)} : ${formatNumber(b)}",
            steps = steps,
            finalAnswer = "أبسط صورة للنسبة: $simpA : $simpB (${formatNumber(percent)}%)"
        )
    }

    fun proportionalDistribution(
        totalAmount: Double, r1: Double, r2: Double, r3: Double?
    ): MathResult {
        val steps = mutableListOf<String>()

        val r3Val = if (r3 != null && r3 > 0) r3 else 0.0
        val sumParts = r1 + r2 + r3Val

        steps.add("النسب المعطاة للتوزيع: ${formatNumber(r1)} : ${formatNumber(r2)}" + (if (r3Val > 0) " : ${formatNumber(r3Val)}" else ""))
        steps.add("المبلغ / الكمية الإجمالية المراد تقسيمها: ${formatNumber(totalAmount)}")
        steps.add("مجموع الأجزاء = ${formatNumber(r1)} + ${formatNumber(r2)}" + (if (r3Val > 0) " + ${formatNumber(r3Val)}" else "") + " = ${formatNumber(sumParts)}")

        val unitValue = totalAmount / sumParts
        steps.add("قيمة الجزء الواحد = المبلغ الكلي ÷ مجموع الأجزاء = ${formatNumber(totalAmount)} ÷ ${formatNumber(sumParts)} = ${formatNumber(unitValue)}")

        val share1 = r1 * unitValue
        val share2 = r2 * unitValue
        steps.add("نصيب الطرف الأول = عدد أجزائه × قيمة الجزء = ${formatNumber(r1)} × ${formatNumber(unitValue)} = ${formatNumber(share1)}")
        steps.add("نصيب الطرف الثاني = عدد أجزائه × قيمة الجزء = ${formatNumber(r2)} × ${formatNumber(unitValue)} = ${formatNumber(share2)}")

        var share3 = 0.0
        if (r3Val > 0) {
            share3 = r3Val * unitValue
            steps.add("نصيب الطرف الثالث = عدد أجزائه × قيمة الجزء = ${formatNumber(r3Val)} × ${formatNumber(unitValue)} = ${formatNumber(share3)}")
        }

        val checkSum = share1 + share2 + share3
        steps.add("التحقق بالجمع: ${formatNumber(share1)} + ${formatNumber(share2)}" + (if (r3Val > 0) " + ${formatNumber(share3)}" else "") + " = ${formatNumber(checkSum)}")

        val finalAns = StringBuilder()
        finalAns.append("نصيب الأول: ${formatNumber(share1)}\n")
        finalAns.append("نصيب الثاني: ${formatNumber(share2)}\n")
        if (r3Val > 0) finalAns.append("نصيب الثالث: ${formatNumber(share3)}\n")
        finalAns.append("قيمة الجزء الواحد: ${formatNumber(unitValue)}")

        return MathResult(
            title = "التقسيم التناسبي",
            problemSummary = "تقسيم ${formatNumber(totalAmount)} بنسبة ${formatNumber(r1)} : ${formatNumber(r2)}" + (if (r3Val > 0) " : ${formatNumber(r3Val)}" else ""),
            steps = steps,
            finalAnswer = finalAns.toString().trim()
        )
    }

    // -------------------------------------------------------------
    // 4. الأسس والجذور (Exponents & Radicals)
    // -------------------------------------------------------------
    fun calculatePower(base: Double, exponent: Double): MathResult {
        val steps = mutableListOf<String>()

        steps.add("الأساس (a) = ${formatNumber(base)} ، الأس (n) = ${formatNumber(exponent)}")

        when {
            exponent == 0.0 -> {
                steps.add("أي عدد غير صفري مرفوع للأس صفر يساوي 1 دائماً (a⁰ = 1).")
                return MathResult(
                    title = "حساب الأسس",
                    problemSummary = "(${formatNumber(base)})⁰",
                    steps = steps,
                    finalAnswer = "الناتج = 1"
                )
            }
            exponent < 0 -> {
                val posExp = -exponent
                val posResult = base.pow(posExp)
                val finalResult = 1.0 / posResult
                steps.add("قاعدة الأس السالب: a⁻ⁿ = 1 ÷ aⁿ")
                steps.add("(${formatNumber(base)})⁻${formatNumber(posExp)} = 1 ÷ (${formatNumber(base)})^${formatNumber(posExp)}")
                steps.add("حساب المقام: (${formatNumber(base)})^${formatNumber(posExp)} = ${formatNumber(posResult)}")
                steps.add("النتيجة = 1 ÷ ${formatNumber(posResult)} = ${formatNumber(finalResult)}")
                return MathResult(
                    title = "حساب الأس السالب",
                    problemSummary = "(${formatNumber(base)})^(${formatNumber(exponent)})",
                    steps = steps,
                    finalAnswer = "الناتج = ${formatNumber(finalResult)} (كسرياً: 1 / ${formatNumber(posResult)})"
                )
            }
            exponent == exponent.roundToLong().toDouble() && exponent in 1.0..10.0 -> {
                val expInt = exponent.toInt()
                val expanded = List(expInt) { formatNumber(base) }.joinToString(" × ")
                steps.add("الأس عدد صحيح موجب: تكرار ضرب الأساس في نفسه $expInt مرات")
                steps.add("(${formatNumber(base)})^$expInt = $expanded")
                val result = base.pow(exponent)
                steps.add("إتمام عملية الضرب = ${formatNumber(result)}")
                return MathResult(
                    title = "حساب الأسس",
                    problemSummary = "(${formatNumber(base)})^$expInt",
                    steps = steps,
                    finalAnswer = "الناتج = ${formatNumber(result)}"
                )
            }
            else -> {
                val result = base.pow(exponent)
                steps.add("حساب القوة: (${formatNumber(base)})^${formatNumber(exponent)} = ${formatNumber(result)}")
                return MathResult(
                    title = "حساب الأسس العامة",
                    problemSummary = "(${formatNumber(base)})^(${formatNumber(exponent)})",
                    steps = steps,
                    finalAnswer = "الناتج = ${formatNumber(result)}"
                )
            }
        }
    }

    fun simplifySquareRoot(number: Long): MathResult {
        val steps = mutableListOf<String>()

        if (number < 0) {
            return MathResult(
                title = "تبسيط الجذور",
                problemSummary = "√($number)",
                steps = listOf("لا يوجد جذر حقيقي لعدد سالب في مجموعة الأعداد الحقيقية."),
                finalAnswer = "قيمة غير معرفة في ℝ (عدد مركب: ${formatNumber(sqrt(abs(number).toDouble()))}i)"
            )
        }

        steps.add("العدد تحت الجذر: $number")
        val exactRoot = sqrt(number.toDouble())

        if (exactRoot == exactRoot.toLong().toDouble()) {
            val rootLong = exactRoot.toLong()
            steps.add("العدد $number هو مربع كامل لأن: $rootLong × $rootLong = $number")
            steps.add("√$number = $rootLong")
            return MathResult(
                title = "الجذر التربيعي المباشر",
                problemSummary = "√$number",
                steps = steps,
                finalAnswer = "الناتج = $rootLong"
            )
        }

        // إيجاد أكبر مربع كامل يقسم number
        var outside = 1L
        var inside = number

        var d = 2L
        while (d * d <= inside) {
            while (inside % (d * d) == 0L) {
                outside *= d
                inside /= (d * d)
            }
            d++
        }

        if (outside > 1) {
            val square = outside * outside
            steps.add("تحليل العدد إلى حاصل ضرب أكبر مربع كامل وعامل آخر: $number = $square × $inside")
            steps.add("توزيع الجذر: √$number = √$square × √$inside")
            steps.add("استخراج المربع الكامل خارج الجذر: √$square = $outside")
            steps.add("الصورة المبسطة الدقيقة: $outside√$inside")
        } else {
            steps.add("العدد $number لا يحتوي على عوامل مربعة كاملة، لذلك هو في أبسط صورة جذرية.")
        }

        steps.add("القيمة العشرية التقريبية: √$number ≈ ${formatNumber(exactRoot)}")

        val simplifiedStr = if (outside > 1) "$outside√$inside (≈ ${formatNumber(exactRoot)})" else "√$number (≈ ${formatNumber(exactRoot)})"

        return MathResult(
            title = "تبسيط الجذر التربيعي",
            problemSummary = "√$number",
            steps = steps,
            finalAnswer = "أبسط صورة = $simplifiedStr"
        )
    }

    fun calculateExponentLaw(
        base: Double, m: Double, n: Double, operation: String
    ): MathResult {
        val steps = mutableListOf<String>()
        val baseStr = formatNumber(base)
        val mStr = formatNumber(m)
        val nStr = formatNumber(n)

        return when (operation) {
            "multiply" -> {
                // a^m * a^n = a^(m+n)
                steps.add("قانون ضرب القوى ذات الأساس المشترك: aᵐ × aⁿ = a^(m + n) (نجمع الأسس)")
                val sumExp = m + n
                steps.add("($baseStr)^$mStr × ($baseStr)^$nStr = ($baseStr)^($mStr + $nStr)")
                steps.add("جمع الأسس: $mStr + $nStr = ${formatNumber(sumExp)}")
                val result = base.pow(sumExp)
                steps.add("حساب القيمة النهائية: ($baseStr)^${formatNumber(sumExp)} = ${formatNumber(result)}")
                MathResult(
                    title = "قانون ضرب الأسس (جمع الأسس)",
                    problemSummary = "($baseStr)^$mStr × ($baseStr)^$nStr",
                    steps = steps,
                    finalAnswer = "الناتج = ($baseStr)^${formatNumber(sumExp)} = ${formatNumber(result)}"
                )
            }
            "divide" -> {
                // a^m / a^n = a^(m-n)
                steps.add("قانون قسمة القوى ذات الأساس المشترك: aᵐ ÷ aⁿ = a^(m - n) (نطرح الأسس)")
                val subExp = m - n
                steps.add("($baseStr)^$mStr ÷ ($baseStr)^$nStr = ($baseStr)^($mStr - $nStr)")
                steps.add("طرح الأسس: $mStr - $nStr = ${formatNumber(subExp)}")
                val result = base.pow(subExp)
                steps.add("حساب القيمة النهائية: ($baseStr)^${formatNumber(subExp)} = ${formatNumber(result)}")
                MathResult(
                    title = "قانون قسمة الأسس (طرح الأسس)",
                    problemSummary = "($baseStr)^$mStr ÷ ($baseStr)^$nStr",
                    steps = steps,
                    finalAnswer = "الناتج = ($baseStr)^${formatNumber(subExp)} = ${formatNumber(result)}"
                )
            }
            else -> {
                // (a^m)^n = a^(m*n)
                steps.add("قانون قوة القوة: (aᵐ)ⁿ = a^(m × n) (نضرب الأسس)")
                val multExp = m * n
                steps.add("[($baseStr)^$mStr]^$nStr = ($baseStr)^($mStr × $nStr)")
                steps.add("ضرب الأسس: $mStr × $nStr = ${formatNumber(multExp)}")
                val result = base.pow(multExp)
                steps.add("حساب القيمة النهائية: ($baseStr)^${formatNumber(multExp)} = ${formatNumber(result)}")
                MathResult(
                    title = "قانون قوة القوة (ضرب الأسس)",
                    problemSummary = "[($baseStr)^$mStr]^$nStr",
                    steps = steps,
                    finalAnswer = "الناتج = ($baseStr)^${formatNumber(multExp)} = ${formatNumber(result)}"
                )
            }
        }
    }
}
