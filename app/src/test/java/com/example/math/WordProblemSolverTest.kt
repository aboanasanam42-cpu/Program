package com.example.math

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WordProblemSolverTest {

    @Test
    fun testDirectProportionProblem() {
        val q = "اشترى أحمد 5 دفاتر بمبلغ 45 ريالاً، فكم يدفع إذا اشترى 12 دفتراً من نفس النوع؟"
        val result = WordProblemSolver.solve(q)
        assertNotNull(result)
        assertTrue(result.steps.isNotEmpty())
        // 45 * 12 / 5 = 108
        assertTrue(result.finalAnswer.contains("108"))
    }

    @Test
    fun testTwoUnknownsRatioProblem() {
        val q = "عددان موجبان النسبة بينهما 3 إلى 5 ومجموعهما 40، فما هما العددان؟"
        val result = WordProblemSolver.solve(q)
        assertNotNull(result)
        // 40 / 8 = 5 -> 15 and 25
        assertTrue(result.finalAnswer.contains("15"))
        assertTrue(result.finalAnswer.contains("25"))
    }

    @Test
    fun testDistributionInheritanceProblem() {
        val q = "توفي رجل وترك تركة قدرها 120000 ريال وزعت بين ورثته بنسبة 3 : 2 : 1، احسب نصيب كل وريث."
        val result = WordProblemSolver.solve(q)
        assertNotNull(result)
        // 120000 / 6 = 20000 -> 60000, 40000, 20000
        assertTrue(result.finalAnswer.contains("60000") || result.finalAnswer.contains("60,000"))
        assertTrue(result.finalAnswer.contains("40000") || result.finalAnswer.contains("40,000"))
        assertTrue(result.finalAnswer.contains("20000") || result.finalAnswer.contains("20,000"))
    }

    @Test
    fun testInverseProportionProblem() {
        val q = "إذا كان 4 عمال ينجزون عملاً في 6 أيام، فكم يوماً يحتاج 8 عمال لإنجاز نفس العمل؟"
        val result = WordProblemSolver.solve(q)
        assertNotNull(result)
        // 4 * 6 = 24 / 8 = 3
        assertTrue(result.finalAnswer.contains("3"))
    }

    @Test
    fun testTriangleSimilarityProblem() {
        val q = "مثلث أطوال أضلاعه 6 و 8 و 10 سم يشابه مثلثاً آخر أطول أضلاعه 15 سم. احسب معامل التشابه ومحيط المثلث الثاني."
        val numbers = WordProblemSolver.extractNumbers(q)
        val result = WordProblemSolver.solve(q)
        org.junit.Assert.assertEquals("Numbers should be 4: $numbers", 4, numbers.size)
        assertTrue(result.finalAnswer.contains("1.5"))
        assertTrue(result.finalAnswer.contains("36"))
    }

    @Test
    fun testArabicIndicDigitsNormalization() {
        val q = "اشترى أحمد ٥ دفاتر بمبلغ ٤٥ ريالاً، فكم يدفع إذا اشترى ١٢ دفتراً؟"
        val result = WordProblemSolver.solve(q)
        assertNotNull(result)
        assertTrue(result.finalAnswer.contains("108"))
    }
}
