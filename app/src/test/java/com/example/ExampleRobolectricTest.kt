package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.math.MathSolvers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("DR/MALIK Math", appName)
  }

  @Test
  fun `test math solver proportion`() {
    // 3 / 5 = 12 / x  => x = 20
    val result = MathSolvers.solveProportion(3.0, 5.0, 12.0, null)
    assertTrue(result.finalAnswer.contains("20"))
  }

  @Test
  fun `test math solver ratio simplification`() {
    // 24 : 36  => 2 : 3
    val result = MathSolvers.simplifyRatio(24.0, 36.0)
    assertTrue(result.finalAnswer.contains("2 : 3"))
  }

  @Test
  fun `test math solver power`() {
    // 2^5 = 32
    val result = MathSolvers.calculatePower(2.0, 5.0)
    assertTrue(result.finalAnswer.contains("32"))
  }

  @Test
  fun `test math solver radical simplification`() {
    // sqrt(72) = 6 sqrt(2)
    val result = MathSolvers.simplifySquareRoot(72L)
    assertTrue(result.finalAnswer.contains("6√2"))
  }
}
