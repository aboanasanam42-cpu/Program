package com.example.math

data class MathResult(
    val title: String,
    val problemSummary: String,
    val steps: List<String>,
    val finalAnswer: String
) {
    fun toPlainText(): String {
        val sb = StringBuilder()
        sb.appendLine("📐 *DR/MALIK - حل المسألة الرياضية*")
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine("📌 *الموضوع:* $title")
        sb.appendLine("📝 *معطيات المسألة:*")
        sb.appendLine(problemSummary)
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine("💡 *خطوات الحل التفصيلية:*")
        steps.forEachIndexed { index, step ->
            sb.appendLine("${index + 1}. $step")
        }
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine("✅ *النتيجة النهائية:*")
        sb.appendLine(finalAnswer)
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine("✨ فكرة أ/ محمد الرميمة | مصحح الفكرة أ/ مطهر الرميمة | تصميم وبرمجة د/ مالك الرميمة - 771134103")
        return sb.toString()
    }
}
