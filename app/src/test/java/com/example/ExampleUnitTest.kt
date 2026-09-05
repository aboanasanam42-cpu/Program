package com.example

import com.example.math.MathSolvers
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun triangleAreaBaseHeight_isCorrect() {
    val result = MathSolvers.calculateTriangleAreaBaseHeight(8.0, 6.0, 2.0)
    assertTrue(result.finalAnswer.contains("24"))
    assertTrue(result.finalAnswer.contains("96"))
  }

  @Test
  fun triangleAreaHeron_isCorrect() {
    val result = MathSolvers.calculateTriangleAreaHeron(6.0, 8.0, 10.0, null)
    assertTrue(result.finalAnswer.contains("24"))
  }
}
