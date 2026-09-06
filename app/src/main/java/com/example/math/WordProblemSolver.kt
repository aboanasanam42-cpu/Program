package com.example.math

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt

object WordProblemSolver {

    private val df = DecimalFormat("#.####", DecimalFormatSymbols(Locale.US))

    /**
     * تحويل الأرقام العربية المشرقية (٠١٢٣٤٥٦٧٨٩) إلى أرقام قياسية
     */
    fun normalizeArabicDigits(input: String): String {
        val arabicDigits = "٠١٢٣٤٥٦٧٨٩"
        val englishDigits = "0123456789"
        var result = input
        for (i in arabicDigits.indices) {
            result = result.replace(arabicDigits[i], englishDigits[i])
        }
        return result
    }

    /**
     * تنظيف النص العربي من التشكيل وتوحيد الحروف لتسهيل الفهم
     */
    fun cleanText(input: String): String {
        return normalizeArabicDigits(input)
            .replace(Regex("[\\u064B-\\u065F\\u0670]"), "") // إزالة التشكيل
            .replace('أ', 'ا')
            .replace('إ', 'ا')
            .replace('آ', 'ا')
            .replace('ة', 'ه')
            .replace('ى', 'ي')
            .trim()
    }

    /**
     * استخراج جميع الأعداد من النص بترتيب ورودها
     */
    fun extractNumbers(text: String): List<Double> {
        val normalized = normalizeArabicDigits(text)
        // استخراج أعداد صحيحة أو عشرية
        val regex = Regex("[-+]?\\d*\\.?\\d+")
        return regex.findAll(normalized)
            .mapNotNull { it.value.toDoubleOrNull() }
            .filter { !it.isNaN() }
            .toList()
    }

    /**
     * حل المسألة اللفظية الذكي
     */
    fun solve(rawQuestion: String): MathResult {
        val text = rawQuestion.trim()
        if (text.isEmpty()) {
            return MathResult(
                title = "مسألة لفظية فارغة",
                problemSummary = "لم يتم إدخال أي نص للمسألة.",
                steps = listOf("يرجى كتابة المسألة الرياضية في الحقل أو الضغط على الميكروفون للتحدث بها."),
                finalAnswer = "يرجى إدخال المسألة"
            )
        }

        val cleaned = cleanText(text)
        val numbers = extractNumbers(text)

        // 1. فحص مسائل تشابه المثلثات والمحيط والمساحة (خاصة بالهندسة)
        if (cleaned.contains("مثلث") || cleaned.contains("مثلثين") || cleaned.contains("تشابه") || cleaned.contains("يشابه")) {
            return solveTriangleWordProblem(text, cleaned, numbers)
        }

        // 2. فحص مسائل عددين والنسبة بينهما ومجموعهما أو الفرق بينهما
        if ((cleaned.contains("عددان") || cleaned.contains("عددين") || cleaned.contains("مجهولين") || cleaned.contains("مجهولان")) &&
            (cleaned.contains("مجموع") || cleaned.contains("الفرق") || cleaned.contains("يزيد") || cleaned.contains("ينقص")) &&
            numbers.size >= 3
        ) {
            return solveTwoNumbersRatioProblem(text, cleaned, numbers)
        }

        // 3. فحص مسائل التقسيم التناسبي وتوزيع المبالغ والتركات والخلطات
        val isDistribution = (cleaned.contains("تركه") || cleaned.contains("ورثه") || cleaned.contains("ورث") ||
                ((cleaned.contains("توزيع") || cleaned.contains("وزعت") || cleaned.contains("قسمت") || cleaned.contains("تقسيم")) &&
                 (cleaned.contains("نسبه") || cleaned.contains("نصيب") || cleaned.contains("حصص") || cleaned.contains("اجزاء"))))
        if (isDistribution && numbers.size >= 3) {
            return solveDistributionProblem(text, cleaned, numbers)
        }

        // 4. فحص مسائل التناسب العكسي (عمال وأيام، سرعة وزمن، خزان ومضخات)
        if ((cleaned.contains("عمال") || cleaned.contains("عامل") || cleaned.contains("بناء") ||
                    (cleaned.contains("سرعه") && cleaned.contains("زمن")) ||
                    (cleaned.contains("سرعه") && cleaned.contains("ساعه")) ||
                    cleaned.contains("عكسي")) &&
            numbers.size >= 3
        ) {
            return solveInverseProportionProblem(text, cleaned, numbers)
        }

        // 5. فحص مسائل التناسب الطردي البسيط (قاعدة الثلاثة: سعر دفاتر، مسافة، إنتاج)
        if ((cleaned.contains("اشتري") || cleaned.contains("اشترى") || cleaned.contains("سعره") ||
                    cleaned.contains("سعر") || cleaned.contains("تكلفه") || cleaned.contains("ريال") ||
                    cleaned.contains("كيلو") || cleaned.contains("دفاتر") || cleaned.contains("دفتر") ||
                    cleaned.contains("تناسب") || cleaned.contains("فكم يدفع") || cleaned.contains("فكم") ||
                    cleaned.contains("ثمن") || cleaned.contains("فما ثمن")) &&
            numbers.size >= 3
        ) {
            return solveDirectProportionProblem(text, cleaned, numbers)
        }

        // 6. فحص مسائل الأسس ومضاعفة الخلايا والمساحات المربعة والجذور
        if (cleaned.contains("اس") || cleaned.contains("قوه") || cleaned.contains("جذر") ||
            cleaned.contains("مربعه") || cleaned.contains("مربع") || cleaned.contains("تتضاعف") || cleaned.contains("مكعب")
        ) {
            return solvePowerRootWordProblem(text, cleaned, numbers)
        }

        // 7. إذا كان هناك 3 أرقام مع عبارة استفهامية، غالباً تناسب طردي
        if (numbers.size == 3) {
            return solveDirectProportionProblem(text, cleaned, numbers)
        }

        // 8. حل عام تحليلي للمسألة
        return solveGeneralMathProblem(text, cleaned, numbers)
    }

    /**
     * حل مسائل عددين النسبة بينهما ومجموعهما أو الفرق بينهما
     */
    private fun solveTwoNumbersRatioProblem(
        raw: String, cleaned: String, numbers: List<Double>
    ): MathResult {
        val steps = mutableListOf<String>()
        val isSum = cleaned.contains("مجموع")
        val r1 = numbers[0]
        val r2 = numbers[1]
        val givenVal = numbers[2]

        steps.add("قراءة معطيات المسألة:")
        steps.add("• النسبة بين العددين المعنيين (س : ص) = ${formatNumber(r1)} : ${formatNumber(r2)}")
        if (isSum) {
            steps.add("• مجموع العددين (س + ص) = ${formatNumber(givenVal)}")
        } else {
            steps.add("• الفرق بين العددين (|س - ص|) = ${formatNumber(givenVal)}")
        }

        steps.add("صياغة التناسب باستخدام ثابت التناسب (ك):")
        steps.add("س = ${formatNumber(r1)} × ك ، ص = ${formatNumber(r2)} × ك")

        val parts = if (isSum) (r1 + r2) else abs(r2 - r1)
        if (parts == 0.0) {
            return MathResult(
                title = "مسألة نسبة ومجموع/فرق",
                problemSummary = raw,
                steps = listOf("أجزاء النسبة متطابقة، لا يمكن قسمة الفرق على صفر."),
                finalAnswer = "معطيات غير متسقة"
            )
        }

        val k = givenVal / parts
        steps.add("حساب ثابت التناسب (ك):")
        if (isSum) {
            steps.add("مجموع الأجزاء = ${formatNumber(r1)} + ${formatNumber(r2)} = ${formatNumber(parts)}")
            steps.add("ك = المجموع ÷ مجموع الأجزاء = ${formatNumber(givenVal)} ÷ ${formatNumber(parts)} = ${formatNumber(k)}")
        } else {
            steps.add("فرق الأجزاء = |${formatNumber(r2)} - ${formatNumber(r1)}| = ${formatNumber(parts)}")
            steps.add("ك = الفرق ÷ فرق الأجزاء = ${formatNumber(givenVal)} ÷ ${formatNumber(parts)} = ${formatNumber(k)}")
        }

        val s = r1 * k
        val y = r2 * k
        steps.add("حساب قيمة العددين:")
        steps.add("العدد الأول (س) = ${formatNumber(r1)} × ${formatNumber(k)} = ${formatNumber(s)}")
        steps.add("العدد الثاني (ص) = ${formatNumber(r2)} × ${formatNumber(k)} = ${formatNumber(y)}")

        steps.add("التحقق من صحة الحل:")
        if (isSum) {
            steps.add("${formatNumber(s)} + ${formatNumber(y)} = ${formatNumber(s + y)} ✓")
        } else {
            steps.add("|${formatNumber(y)} - ${formatNumber(s)}| = ${formatNumber(abs(y - s))} ✓")
        }
        steps.add("النسبة: ${formatNumber(s)} : ${formatNumber(y)} = ${formatNumber(r1)} : ${formatNumber(r2)} ✓")

        return MathResult(
            title = "حل مسألة النسبة والمجموع/الفرق",
            problemSummary = raw,
            steps = steps,
            finalAnswer = "العدد الأول (س) = ${formatNumber(s)}\nالعدد الثاني (ص) = ${formatNumber(y)}"
        )
    }

    /**
     * حل مسائل التقسيم التناسبي والتركات وتوزيع المبالغ
     */
    private fun solveDistributionProblem(
        raw: String, cleaned: String, numbers: List<Double>
    ): MathResult {
        val steps = mutableListOf<String>()

        // الرقم الأكبر غالباً هو المبلغ الإجمالي المراد توزيعه
        val totalAmount = numbers.maxOrNull() ?: numbers.first()
        val ratios = numbers.filter { it != totalAmount }.ifEmpty { listOf(numbers[0], numbers[1]) }

        steps.add("تحديد المعطيات:")
        steps.add("• المبلغ الإجمالي / التركة المراد توزيعها = ${formatNumber(totalAmount)}")
        steps.add("• نسب التوزيع = " + ratios.joinToString(" : ") { formatNumber(it) })

        val sumParts = ratios.sum()
        if (sumParts <= 0.0) {
            return MathResult(
                title = "التقسيم التناسبي",
                problemSummary = raw,
                steps = listOf("مجموع النسب يجب أن يكون أكبر من الصفر."),
                finalAnswer = "بيانات غير صالحة"
            )
        }

        steps.add("1. حساب مجموع الأجزاء:")
        steps.add("مجموع الأجزاء = " + ratios.joinToString(" + ") { formatNumber(it) } + " = ${formatNumber(sumParts)}")

        val unitValue = totalAmount / sumParts
        steps.add("2. حساب قيمة الجزء الواحد:")
        steps.add("قيمة الجزء = المبلغ الإجمالي ÷ مجموع الأجزاء = ${formatNumber(totalAmount)} ÷ ${formatNumber(sumParts)} = ${formatNumber(unitValue)}")

        steps.add("3. حساب نصيب كل طرف:")
        val shares = ratios.mapIndexed { index, r ->
            val share = r * unitValue
            steps.add("• نصيب الطرف ${index + 1} = ${formatNumber(r)} × ${formatNumber(unitValue)} = ${formatNumber(share)}")
            share
        }

        steps.add("التحقق بالجمع:")
        steps.add(shares.joinToString(" + ") { formatNumber(it) } + " = ${formatNumber(shares.sum())} ✓")

        val answerText = StringBuilder()
        shares.forEachIndexed { index, sh ->
            answerText.append("نصيب الطرف ${index + 1}: ${formatNumber(sh)}\n")
        }
        answerText.append("قيمة الجزء الواحد: ${formatNumber(unitValue)}")

        return MathResult(
            title = "حل مسألة التقسيم التناسبي والتركات",
            problemSummary = raw,
            steps = steps,
            finalAnswer = answerText.toString().trim()
        )
    }

    /**
     * حل مسائل التناسب العكسي (عمال وأيام، سرعة وزمن)
     */
    private fun solveInverseProportionProblem(
        raw: String, cleaned: String, numbers: List<Double>
    ): MathResult {
        val steps = mutableListOf<String>()
        val a1 = numbers[0]
        val b1 = numbers[1]
        val a2 = numbers[2]

        steps.add("تحليل المسألة وتحديد نوع التناسب:")
        steps.add("هذه المسألة تمثل **تناسباً عكسياً** (حيث بزيادة أحد المتغيرين ينقص الآخر بنفس النسبة).")
        steps.add("قاعدة التناسب العكسي: حاصل ضرب الكميتين ثابت دائماً (س₁ × ص₁ = س₂ × ص₂ = ك)")

        val k = a1 * b1
        steps.add("1. حساب الثابت الإجمالي للعمل / المسافة (ك):")
        steps.add("ك = ${formatNumber(a1)} × ${formatNumber(b1)} = ${formatNumber(k)}")

        if (a2 == 0.0) {
            return MathResult(
                title = "التناسب العكسي",
                problemSummary = raw,
                steps = listOf("القيمة المقسوم عليها لا يمكن أن تكون صفراً."),
                finalAnswer = "غير معرّف"
            )
        }

        val b2 = k / a2
        steps.add("2. إيجاد القيمة المطلوبة (س):")
        steps.add("س = ك ÷ ${formatNumber(a2)} = ${formatNumber(k)} ÷ ${formatNumber(a2)} = ${formatNumber(b2)}")

        steps.add("التحقق:")
        steps.add("${formatNumber(a1)} × ${formatNumber(b1)} = ${formatNumber(a2)} × ${formatNumber(b2)} = ${formatNumber(k)} ✓")

        return MathResult(
            title = "حل مسألة التناسب العكسي",
            problemSummary = raw,
            steps = steps,
            finalAnswer = "الناتج المطلوب = ${formatNumber(b2)}"
        )
    }

    /**
     * حل مسائل التناسب الطردي المباشر (قاعدة الثلاثة البسيطة)
     */
    private fun solveDirectProportionProblem(
        raw: String, cleaned: String, numbers: List<Double>
    ): MathResult {
        val steps = mutableListOf<String>()
        val a = numbers[0]
        val b = numbers[1]
        val c = numbers[2]

        steps.add("تحليل معطيات التناسب الطردي:")
        steps.add("الكمية الأولى (${formatNumber(a)}) يقابلها (${formatNumber(b)})")
        steps.add("المطلوب: إيجاد الكمية المقابلة لـ (${formatNumber(c)})")

        steps.add("صياغة التناسب في صورة كسور:")
        steps.add("${formatNumber(a)} / ${formatNumber(b)} = ${formatNumber(c)} / س")

        steps.add("تطبيق خاصية التناسب الأساسية (حاصل ضرب الطرفين = حاصل ضرب الوسطين):")
        steps.add("${formatNumber(a)} × س = ${formatNumber(b)} × ${formatNumber(c)}")

        val product = b * c
        steps.add("حاصل ضرب الوسطين = ${formatNumber(b)} × ${formatNumber(c)} = ${formatNumber(product)}")

        if (a == 0.0) {
            return MathResult(
                title = "التناسب الطردي",
                problemSummary = raw,
                steps = listOf("لا يمكن القسمة على صفر."),
                finalAnswer = "غير معرّف"
            )
        }

        val ans = product / a
        steps.add("حساب المجهول (س):")
        steps.add("س = ${formatNumber(product)} ÷ ${formatNumber(a)} = ${formatNumber(ans)}")

        val unitRate = b / a
        steps.add("التحقق باستخدام معدل الوحدة:")
        steps.add("سعر أو قيمة الوحدة الواحدة = ${formatNumber(b)} ÷ ${formatNumber(a)} = ${formatNumber(unitRate)}")
        steps.add("القيمة المطلوبة = ${formatNumber(c)} × ${formatNumber(unitRate)} = ${formatNumber(ans)} ✓")

        return MathResult(
            title = "حل مسألة التناسب الطردي",
            problemSummary = raw,
            steps = steps,
            finalAnswer = "الناتج المطلوب = ${formatNumber(ans)}"
        )
    }

    /**
     * حل مسائل تشابه المثلثات والمساحة والمحيط
     */
    private fun solveTriangleWordProblem(
        raw: String, cleaned: String, numbers: List<Double>
    ): MathResult {
        val steps = mutableListOf<String>()

        if (numbers.size >= 4) {
            // مثلاً مثلث أضلاعه 6, 8, 10 ومثلث مشابه ضلع فيه 15
            val sides1 = numbers.subList(0, 3).sorted()
            val side2Known = numbers[3]

            val a1 = sides1[0]
            val b1 = sides1[1]
            val c1 = sides1[2]

            val p1 = a1 + b1 + c1
            val k = side2Known / c1
            val p2 = p1 * k
            val a2 = a1 * k
            val b2 = b1 * k

            steps.add("المثلث الأول أطوال أضلاعه: ${formatNumber(a1)} ، ${formatNumber(b1)} ، ${formatNumber(c1)}")
            steps.add("محيط المثلث الأول = ${formatNumber(a1)} + ${formatNumber(b1)} + ${formatNumber(c1)} = ${formatNumber(p1)}")
            steps.add("الضلع المناظر في المثلث الثاني = ${formatNumber(side2Known)}")

            steps.add("1. حساب معامل التشابه (ك):")
            steps.add("ك = الضلع في المثلث الثاني ÷ الضلع المناظر في الأول = ${formatNumber(side2Known)} ÷ ${formatNumber(c1)} = ${formatNumber(k)}")

            steps.add("2. حساب بقية أضلاع المثلث الثاني:")
            steps.add("• الضلع الأول = ${formatNumber(a1)} × ${formatNumber(k)} = ${formatNumber(a2)}")
            steps.add("• الضلع الثاني = ${formatNumber(b1)} × ${formatNumber(k)} = ${formatNumber(b2)}")

            steps.add("3. حساب محيط المثلث الثاني:")
            steps.add("محيط المثلث الثاني = محيط الأول × ك = ${formatNumber(p1)} × ${formatNumber(k)} = ${formatNumber(p2)}")

            val kSquared = k * k
            steps.add("4. نسبة المساحات بين المثلثين:")
            steps.add("النسبة بين مساحتي المثلثين = ك² = (${formatNumber(k)})² = ${formatNumber(kSquared)}")

            return MathResult(
                title = "حل مسألة تشابه المثلثات",
                problemSummary = raw,
                steps = steps,
                finalAnswer = "معامل التشابه (ك) = ${formatNumber(k)}\nمحيط المثلث الثاني = ${formatNumber(p2)}\nأضلاع المثلث الثاني = ${formatNumber(a2)} ، ${formatNumber(b2)} ، ${formatNumber(side2Known)}"
            )
        } else if (numbers.size == 3) {
            // حساب مساحة هيرون ومحيط
            val a = numbers[0]
            val b = numbers[1]
            val c = numbers[2]
            return MathSolvers.calculateTriangleAreaHeron(a, b, c)
        } else {
            return MathResult(
                title = "تشابه المثلثات",
                problemSummary = raw,
                steps = listOf("يرجى تزويد أطوال أضلاع المثلث والضلع المناظر لحساب معامل التشابه والأضلاع."),
                finalAnswer = "يرجى تحديد أطوال الأضلاع"
            )
        }
    }

    /**
     * حل مسائل الأسس والجذور
     */
    private fun solvePowerRootWordProblem(
        raw: String, cleaned: String, numbers: List<Double>
    ): MathResult {
        val steps = mutableListOf<String>()

        if (cleaned.contains("جذر") || cleaned.contains("مربعه") || cleaned.contains("مساحتها")) {
            val n = numbers.firstOrNull() ?: 72.0
            steps.add("المعطى: المساحة أو العدد تحت الجذر = ${formatNumber(n)}")
            steps.add("إيجاد طول الضلع بأخذ الجذر التربيعي: طول الضلع = √${formatNumber(n)}")

            val rootRes = MathSolvers.simplifySquareRoot(n.toLong())
            steps.addAll(rootRes.steps)

            return MathResult(
                title = "حل مسألة الجذور والضلع المربع",
                problemSummary = raw,
                steps = steps,
                finalAnswer = "طول الضلع = ${rootRes.finalAnswer}"
            )
        } else {
            val base = numbers.getOrNull(0) ?: 2.0
            val exp = numbers.getOrNull(1) ?: 5.0

            steps.add("تحديد الأساس والأس:")
            steps.add("الأساس (أ) = ${formatNumber(base)} ، الأس (ن) = ${formatNumber(exp)}")

            val res = MathSolvers.calculatePower(base, exp)
            steps.addAll(res.steps)

            return MathResult(
                title = "حل مسألة التضاعف والأسس",
                problemSummary = raw,
                steps = steps,
                finalAnswer = res.finalAnswer
            )
        }
    }

    /**
     * تحليل عام للمسألة اللفظية عند عدم مطابقة نمط محدد مسبقاً
     */
    private fun solveGeneralMathProblem(
        raw: String, cleaned: String, numbers: List<Double>
    ): MathResult {
        val steps = mutableListOf<String>()

        steps.add("تحليل نص المسألة اللفظية:")
        steps.add("النص المدخل: \"$raw\"")

        if (numbers.isEmpty()) {
            steps.add("لم يتم العثور على أرقام محددة في نص المسألة.")
            steps.add("يرجى التأكد من ذكر الأرقام والمعطيات بوضوح في المسألة (مثل: عدد الدفاتر، الأسعار، الأيام، النسب).")
            return MathResult(
                title = "تحليل المسألة اللفظية",
                problemSummary = raw,
                steps = steps,
                finalAnswer = "يرجى كتابة أرقام ومعطيات المسألة بوضوح"
            )
        }

        steps.add("الأرقام المستخرجة من المسألة: " + numbers.joinToString(" ، ") { formatNumber(it) })

        if (numbers.size == 2) {
            val a = numbers[0]
            val b = numbers[1]
            steps.add("حساب النسبة بين العددين المعطيين:")
            val ratioRes = MathSolvers.simplifyRatio(a, b)
            steps.addAll(ratioRes.steps)
            return MathResult(
                title = "تحليل المسألة اللفظية (نسبة)",
                problemSummary = raw,
                steps = steps,
                finalAnswer = ratioRes.finalAnswer
            )
        }

        steps.add("تم تصنيف المسألة كمسألة رياضية لفظية مركبة.")
        steps.add("يمكنك الاستعانة بأحد الأنماط المباشرة في الواجهة (تناسب، نسبة، أسس، مثلثات) للحصول على صيغ متخصصة.")

        return MathResult(
            title = "تحليل المسألة اللفظية",
            problemSummary = raw,
            steps = steps,
            finalAnswer = "المعطيات: " + numbers.joinToString(" ، ") { formatNumber(it) }
        )
    }

    private fun formatNumber(value: Double): String {
        return if (value == value.roundToLong().toDouble()) {
            value.roundToLong().toString()
        } else {
            df.format(value)
        }
    }
}
