package com.chahinem.pageindicator

internal class DotManager(
    count: Int,
    private val dotSize: Int,
    private val dotSpacing: Int,
    private val dotBound: Int,
    private val dotSizes: Map<Byte, Int>,
    private val targetScrollListener: TargetScrollListener? = null
) {

  internal var dots: ByteArray = ByteArray(count)
  internal var selectedIndex = 0

  private var scrollAmount = 0

  init {

    if (count > 0) {
      dots[0] = 6
    }

    if (count <= SIZE_THRESHOLD) {
      (1 until count).forEach { i -> dots[i] = 5 }
    } else {
      (1..3).forEach { i -> dots[i] = 5 }
      dots[4] = 4
      if (count > SIZE_THRESHOLD) {
        dots[5] = 2
      }
      (SIZE_THRESHOLD + 1 until count).forEach { i -> dots[i] = 0 }
    }
  }

  internal fun dots() = dots.joinToString("")

  fun dotSizeFor(size: Byte) = dotSizes[size] ?: 0

  fun goToNext() {
    if (selectedIndex >= dots.size - 1) {
      return
    }

    ++selectedIndex

    if (dots.size <= SIZE_THRESHOLD) {
      goToNextSmall()
    } else {
      goToNextLarge()
    }
  }

  fun goToPrevious() {
    if (selectedIndex == 0) {
      return
    }

    --selectedIndex

    if (dots.size <= SIZE_THRESHOLD) {
      goToPreviousSmall()
    } else {
      goToPreviousLarge()
    }
  }

  private fun goToNextSmall() {
    dots[selectedIndex] = 6
    dots[selectedIndex - 1] = 5
  }

  private fun goToNextLarge() {
    var needScroll = false

    // swap 6 and 5
    dots[selectedIndex] = 6
    dots[selectedIndex - 1] = 5

    // no more than 3 5's in a row backward
    if (selectedIndex > 3
        && dots[selectedIndex - 1] == 5.toByte()
        && dots[selectedIndex - 2] == 5.toByte()
        && dots[selectedIndex - 3] == 5.toByte()
        && dots[selectedIndex - 4] == 5.toByte()) {
      dots[selectedIndex - 4] = 4
      needScroll = true
      if (selectedIndex - 5 >= 0) {
        dots[selectedIndex - 5] = 2
        (selectedIndex - 6 downTo 0)
            .takeWhile { dots[it] != 0.toByte() }
            .forEach { dots[it] = 0 }
      }
    }

    // 6 must around around 3 or higher
    if (selectedIndex + 1 < dots.size && dots[selectedIndex + 1] < 3) {
      dots[selectedIndex + 1] = 3
      needScroll = true
      // set the next one to 1 if any
      if (selectedIndex + 2 < dots.size && dots[selectedIndex + 2] < 1) {
        dots[selectedIndex + 2] = 1
      }
    }

    // Scroll to keep the selected dot within bound
    if (needScroll) {
      val endBound = selectedIndex * (dotSize + dotSpacing) + dotSize
      if (endBound > dotBound) {
        scrollAmount = endBound - dotBound
        targetScrollListener?.scrollToTarget(scrollAmount)
      }
    }
  }

  private fun goToPreviousSmall() {
    dots[selectedIndex] = 6
    dots[selectedIndex + 1] = 5
  }

  private fun goToPreviousLarge() {
    var needScroll = false
    // swap 6 and 5
    dots[selectedIndex] = 6
    dots[selectedIndex + 1] = 5

    // no more than 3 5's in a row backward
    if (selectedIndex < dots.size - 4
        && dots[selectedIndex + 1] == 5.toByte()
        && dots[selectedIndex + 2] == 5.toByte()
        && dots[selectedIndex + 3] == 5.toByte()
        && dots[selectedIndex + 4] == 5.toByte()) {
      dots[selectedIndex + 4] = 4
      needScroll = true
      if (selectedIndex + 5 < dots.size) {
        dots[selectedIndex + 5] = 2
        (selectedIndex + 6 until dots.size)
            .takeWhile { dots[it] != 0.toByte() }
            .forEach { i -> dots[i] = 0 }
      }
    }

    // 6 must around around 3 or higher
    if (selectedIndex - 1 >= 0 && dots[selectedIndex - 1] < 3) {
      needScroll = true
      dots[selectedIndex - 1] = 3
      // set the next one to 1 if any
      if (selectedIndex - 2 >= 0 && dots[selectedIndex - 2] < 1) {
        dots[selectedIndex - 2] = 1
      }
    }

    // Scroll to keep the selected dot within bound
    if (needScroll) {
      val startBound = selectedIndex * (dotSize + dotSpacing)
      if (startBound < scrollAmount) {
        scrollAmount = selectedIndex * (dotSize + dotSpacing)
        targetScrollListener?.scrollToTarget(scrollAmount)
      }
    }
  }

  interface TargetScrollListener {
    fun scrollToTarget(target: Int)
  }

  companion object {
    private const val SIZE_THRESHOLD = 5
  }
}