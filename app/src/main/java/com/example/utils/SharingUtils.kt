package com.example.utils

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextDirectionHeuristics
import android.text.TextPaint
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.math.MathResult
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SharingUtils {

    const val PERMANENT_FOOTER = "فكرة أ/ محمد الرمامة | مصحح الفكرة أ/ مطهر الرمامة | تصميم د/ مالك الرمامة - 771134103"

    fun shareViaWhatsApp(context: Context, phoneNumber: String, mathResult: MathResult) {
        val cleanPhone = phoneNumber.replace(Regex("[^0-9+]"), "").trim()
        val text = mathResult.toPlainText()

        try {
            val encodedText = URLEncoder.encode(text, "UTF-8")
            val whatsappUri = if (cleanPhone.isNotEmpty()) {
                // If phone doesn't start with '+', check if it's 9 digits starting with 7 (Yemen standard)
                val finalPhone = when {
                    cleanPhone.startsWith("+") -> cleanPhone.substring(1)
                    cleanPhone.startsWith("00") -> cleanPhone.substring(2)
                    cleanPhone.length == 9 && cleanPhone.startsWith("7") -> "967$cleanPhone"
                    else -> cleanPhone
                }
                Uri.parse("https://api.whatsapp.com/send?phone=$finalPhone&text=$encodedText")
            } else {
                Uri.parse("https://api.whatsapp.com/send?text=$encodedText")
            }

            val intent = Intent(Intent.ACTION_VIEW, whatsappUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to standard share sheet
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(Intent.createChooser(shareIntent, "مشاركة الحل عبر التطبيقات"))
        }
    }

    fun exportAndSharePdf(context: Context, mathResult: MathResult) {
        try {
            val pdfDocument = PdfDocument()
            val pageWidth = 595
            val pageHeight = 842
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            drawPdfContent(canvas, pageWidth, pageHeight, mathResult)

            pdfDocument.finishPage(page)

            // Save PDF to cache directory for safe FileProvider sharing
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val pdfFileName = "Math_Solution_$timeStamp.pdf"
            val outputDir = File(context.cacheDir, "shared_docs")
            if (!outputDir.exists()) outputDir.mkdirs()
            val pdfFile = File(outputDir, pdfFileName)

            val outputStream = FileOutputStream(pdfFile)
            pdfDocument.writeTo(outputStream)
            outputStream.flush()
            outputStream.close()
            pdfDocument.close()

            // Generate content URI via FileProvider
            val contentUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            // Share intent
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_SUBJECT, "حل مسألة: ${mathResult.title}")
                putExtra(Intent.EXTRA_TEXT, "حل المسألة الرياضية - DR/MALIK\n$PERMANENT_FOOTER")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(Intent.createChooser(shareIntent, "فتح أو مشاركة ملف PDF"))
            Toast.makeText(context, "تم إنشاء ملف PDF بنجاح!", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "حدث خطأ أثناء تصدير PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun drawPdfContent(
        canvas: Canvas,
        width: Int,
        height: Int,
        mathResult: MathResult
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // 1. Background (Clean soft tint)
        paint.color = Color.rgb(248, 251, 254)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        // 2. Top Header Banner
        val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        headerPaint.color = Color.rgb(51, 122, 183)
        canvas.drawRoundRect(RectF(24f, 24f, width - 24f, 96f), 16f, 16f, headerPaint)

        // Header Title
        paint.color = Color.WHITE
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("DR / MALIK  -  الرياضيات التفاعلية", width / 2f, 58f, paint)

        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("تطبيق حل المسائل الرياضية التفاعلي خطوة بخطوة", width / 2f, 80f, paint)

        var currentY = 120f

        // 3. Section Title Pill
        val titleCardPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        titleCardPaint.color = Color.rgb(228, 240, 251)
        val titleRect = RectF(30f, currentY, width - 30f, currentY + 40f)
        canvas.drawRoundRect(titleRect, 10f, 10f, titleCardPaint)

        paint.color = Color.rgb(35, 90, 140)
        paint.textSize = 16f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("الموضوع: ${mathResult.title}", width / 2f, currentY + 26f, paint)

        currentY += 56f

        // 4. Problem Inputs Box
        val boxPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        boxPaint.color = Color.WHITE
        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        strokePaint.color = Color.rgb(214, 228, 240)
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 1.5f

        val inputLines = mathResult.problemSummary.split("\n")
        val inputHeight = 36f + (inputLines.size * 20f)
        val inputRect = RectF(30f, currentY, width - 30f, currentY + inputHeight)
        canvas.drawRoundRect(inputRect, 12f, 12f, boxPaint)
        canvas.drawRoundRect(inputRect, 12f, 12f, strokePaint)

        paint.color = Color.rgb(23, 43, 62)
        paint.textSize = 13f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText("معطيات المسألة:", width - 46f, currentY + 24f, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.textSize = 12f
        var inputLineY = currentY + 44f
        for (line in inputLines) {
            canvas.drawText(line, width - 46f, inputLineY, paint)
            inputLineY += 19f
        }

        currentY += inputHeight + 18f

        // 5. Steps Section
        paint.color = Color.rgb(51, 122, 183)
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText("خطوات الحل بالتفصيل:", width - 36f, currentY, paint)

        currentY += 14f

        val stepTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(35, 50, 68)
            textSize = 11.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val stepBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
        }

        for ((index, step) in mathResult.steps.withIndex()) {
            val stepLabel = "${index + 1}. $step"
            val textWidth = (width - 80).coerceAtLeast(100)

            val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StaticLayout.Builder.obtain(stepLabel, 0, stepLabel.length, stepTextPaint, textWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setTextDirection(TextDirectionHeuristics.RTL)
                    .setLineSpacing(3f, 1.1f)
                    .build()
            } else {
                @Suppress("DEPRECATION")
                StaticLayout(
                    stepLabel,
                    stepTextPaint,
                    textWidth,
                    Layout.Alignment.ALIGN_NORMAL,
                    1.1f,
                    3f,
                    true
                )
            }

            val cardH = staticLayout.height + 14f
            val cardRect = RectF(30f, currentY, width - 30f, currentY + cardH)
            canvas.drawRoundRect(cardRect, 8f, 8f, stepBoxPaint)
            canvas.drawRoundRect(cardRect, 8f, 8f, strokePaint)

            canvas.save()
            canvas.translate(40f, currentY + 7f)
            staticLayout.draw(canvas)
            canvas.restore()

            currentY += cardH + 8f
            if (currentY > height - 120f) break
        }

        // 6. Final Answer Highlight Box
        val answerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        answerPaint.color = Color.rgb(230, 246, 238)
        val answerStroke = Paint(Paint.ANTI_ALIAS_FLAG)
        answerStroke.color = Color.rgb(67, 182, 138)
        answerStroke.style = Paint.Style.STROKE
        answerStroke.strokeWidth = 2f

        val ansY = currentY.coerceAtMost(height - 110f)
        val ansRect = RectF(30f, ansY, width - 30f, ansY + 46f)
        canvas.drawRoundRect(ansRect, 10f, 10f, answerPaint)
        canvas.drawRoundRect(ansRect, 10f, 10f, answerStroke)

        paint.color = Color.rgb(20, 110, 75)
        paint.textSize = 13.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("النتيجة النهائية: ${mathResult.finalAnswer.replace("\n", " | ")}", width / 2f, ansY + 28f, paint)

        // 7. PERMANENT BRANDING FOOTER (Required on every PDF)
        val footerBg = Paint(Paint.ANTI_ALIAS_FLAG)
        footerBg.color = Color.rgb(235, 243, 250)
        canvas.drawRect(0f, height - 42f, width.toFloat(), height.toFloat(), footerBg)

        val footerDivider = Paint(Paint.ANTI_ALIAS_FLAG)
        footerDivider.color = Color.rgb(200, 218, 235)
        footerDivider.strokeWidth = 1f
        canvas.drawLine(0f, height - 42f, width.toFloat(), height - 42f, footerDivider)

        paint.color = Color.rgb(23, 43, 62)
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(PERMANENT_FOOTER, width / 2f, height - 16f, paint)
    }
}
