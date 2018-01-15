package com.chahinem.pageindicator

import org.junit.Assert.assertEquals
import org.junit.Test

class DotManagerTest {
  companion object {
    private val dotSizes = mapOf(
        6.toByte() to 12,
        5.toByte() to 10,
        4.toByte() to 9,
        3.toByte() to 6,
        2.toByte() to 5,
        1.toByte() to 1
    )
  }

  @Test fun init_isCorrect() {
    assertEquals("6", DotManager(1, 0, 0, 0, dotSizes).dots())
    assertEquals("65", DotManager(2, 0, 0, 0, dotSizes).dots())
    assertEquals("655", DotManager(3, 0, 0, 0, dotSizes).dots())
    assertEquals("6555", DotManager(4, 0, 0, 0, dotSizes).dots())
    assertEquals("65555", DotManager(5, 0, 0, 0, dotSizes).dots())
    assertEquals("655542", DotManager(6, 0, 0, 0, dotSizes).dots())
    assertEquals("6555420", DotManager(7, 0, 0, 0, dotSizes).dots())
    assertEquals("65554200", DotManager(8, 0, 0, 0, dotSizes).dots())
  }

  @Test fun goToNext_small() {
    val dotManager = DotManager(5, 0, 0, 0, dotSizes)
    assertEquals("65555", dotManager.dots())
    dotManager.goToNext()
    assertEquals("56555", dotManager.dots())
    dotManager.goToNext()
    assertEquals("55655", dotManager.dots())
    dotManager.goToNext()
    assertEquals("55565", dotManager.dots())
    dotManager.goToNext()
    assertEquals("55556", dotManager.dots())
  }

  @Test fun goToNext_large6() {
    val dotManager = DotManager(6, 0, 0, 0, dotSizes)
    assertEquals("655542", dotManager.dots())
    dotManager.goToNext()
    assertEquals("565542", dotManager.dots())
    dotManager.goToNext()
    assertEquals("556542", dotManager.dots())
    dotManager.goToNext()
    assertEquals("555642", dotManager.dots())
    dotManager.goToNext()
    assertEquals("455563", dotManager.dots())
    dotManager.goToNext()
    assertEquals("245556", dotManager.dots())
  }

  @Test fun goToNext_large8() {
    val dotManager = DotManager(8, 0, 0, 0, dotSizes)
    assertEquals("65554200", dotManager.dots())
    dotManager.goToNext()
    assertEquals("56554200", dotManager.dots())
    dotManager.goToNext()
    assertEquals("55654200", dotManager.dots())
    dotManager.goToNext()
    assertEquals("55564200", dotManager.dots())
    dotManager.goToNext()
    assertEquals("45556310", dotManager.dots())
    dotManager.goToNext()
    assertEquals("24555631", dotManager.dots())
    dotManager.goToNext()
    assertEquals("02455563", dotManager.dots())
    dotManager.goToNext()
    assertEquals("00245556", dotManager.dots())
  }

  @Test fun goToPrevious_small() {
    val dotManager = DotManager(5, 0, 0, 0, dotSizes)
    assertEquals("65555", dotManager.dots())
    dotManager.goToNext()
    dotManager.goToNext()
    dotManager.goToNext()
    dotManager.goToNext()
    assertEquals("55556", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("55565", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("55655", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("56555", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("65555", dotManager.dots())
  }

  @Test fun goToPrevious_large6() {
    val dotManager = DotManager(6, 0, 0, 0, dotSizes)
    dotManager.goToNext()
    dotManager.goToNext()
    dotManager.goToNext()
    dotManager.goToNext()
    dotManager.goToNext()
    assertEquals("245556", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("245565", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("245655", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("246555", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("365554", dotManager.dots()) // double check
    dotManager.goToPrevious()
    assertEquals("655542", dotManager.dots())
  }

  @Test fun goToPrevious_large8() {
    val dotManager = DotManager(8, 0, 0, 0, dotSizes)
    assertEquals("65554200", dotManager.dots())
    dotManager.goToNext()
    dotManager.goToNext()
    dotManager.goToNext()
    dotManager.goToNext()
    dotManager.goToNext()
    dotManager.goToNext()
    dotManager.goToNext()
    assertEquals("00245556", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("00245565", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("00245655", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("00246555", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("01365554", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("13655542", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("36555420", dotManager.dots())
    dotManager.goToPrevious()
    assertEquals("65554200", dotManager.dots())
  }
}