package com.prolificinteractive

class DotManager(count: Int) {

  companion object {
    private const val SIZE_THRESHOLD = 5

    private val DOT_SIZE = 6.dp
    private val DOT_SPACING = 3.dp
    private val DOT_BOUND = 40.dp
  }

  lateinit var targetScrollListener: TargetScrollListener

  internal var dots: CharArray

  private var selectedIndex = 0
  private var scrollAmount = 0

  /**
   * 8 visible dots maximum at once
   */

  /**
   * On xxxhdpi screen:
   *
   * selected dot = 24px
   * regular  dot = 20px
   * large    dot = 18px
   * medium   dot = 12px
   * small    dot = 10px
   * tiny     dot =  2px
   * none     dot =  0px
   */
  init {
    if (count <= 0) {
      throw IllegalArgumentException("count expected to be > 0, actual: $count")
    }

    dots = CharArray(count)
    dots[0] = '6'
    if (count <= SIZE_THRESHOLD) {
      (1 until count).forEach { i -> dots[i] = '5' }
    } else {
      (1..3).forEach { i -> dots[i] = '5' }
      dots[4] = '4'
      if (count > SIZE_THRESHOLD) {
        dots[5] = '2'
      }
      (SIZE_THRESHOLD + 1 until count).forEach { i -> dots[i] = '0' }
    }
  }

  internal fun dots() = dots.joinToString("")

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
    dots[selectedIndex] = '6'
    dots[selectedIndex - 1] = '5'
  }

  private fun goToNextLarge() {
    var needScroll = false

    // swap 6 and 5
    dots[selectedIndex] = '6'
    dots[selectedIndex - 1] = '5'

    // no more than 3 5's in a row backward
    if (selectedIndex > 3
        && dots[selectedIndex - 1] == '5'
        && dots[selectedIndex - 2] == '5'
        && dots[selectedIndex - 3] == '5'
        && dots[selectedIndex - 4] == '5') {
      dots[selectedIndex - 4] = '4'
      needScroll = true
      if (selectedIndex - 5 >= 0) {
        dots[selectedIndex - 5] = '2'
        (selectedIndex - 6 downTo 0).forEach { i -> dots[i] = '0' }
      }
    }

    // 6 must around around 3 or higher
    if (selectedIndex + 1 < dots.size && dots[selectedIndex + 1] < '3') {
      dots[selectedIndex + 1] = '3'
      needScroll = true
      // set the next one to 1 if any
      if (selectedIndex + 2 < dots.size && dots[selectedIndex + 2] < '1') {
        dots[selectedIndex + 2] = '1'
      }
    }

    if (needScroll) {
      val endBound = selectedIndex * (DOT_SIZE + DOT_SPACING) + DOT_SIZE
      if (endBound > DOT_BOUND) {
        scrollAmount = endBound - DOT_BOUND
        targetScrollListener.targetScrollTo(scrollAmount)
      }
    }
  }

  private fun goToPreviousSmall() {
    dots[selectedIndex] = '6'
    dots[selectedIndex + 1] = '5'
  }

  private fun goToPreviousLarge() {
    var needScroll = false

    // swap 6 and 5
    dots[selectedIndex] = '6'
    dots[selectedIndex + 1] = '5'

    // no more than 3 5's in a row backward
    if (selectedIndex < dots.size - 4
        && dots[selectedIndex + 1] == '5'
        && dots[selectedIndex + 2] == '5'
        && dots[selectedIndex + 3] == '5'
        && dots[selectedIndex + 4] == '5') {
      dots[selectedIndex + 4] = '4'
      needScroll = true
      if (selectedIndex + 5 < dots.size) {
        dots[selectedIndex + 5] = '2'
        (selectedIndex + 6 until dots.size).forEach { i -> dots[i] = '0' }
      }
    }

    // 6 must around around 3 or higher
    if (selectedIndex - 1 >= 0 && dots[selectedIndex - 1] < '3') {
      needScroll = true
      dots[selectedIndex - 1] = '3'
      // set the next one to 1 if any
      if (selectedIndex - 2 >= 0 && dots[selectedIndex - 2] < '1') {
        dots[selectedIndex - 2] = '1'
      }
    }

    if (needScroll) {
      val startBound = selectedIndex * (DOT_SIZE + DOT_SPACING)
      if (startBound < scrollAmount) {
        scrollAmount = selectedIndex * (DOT_SIZE + DOT_SPACING)
        targetScrollListener.targetScrollTo(scrollAmount)
      }
    }
  }

  internal fun initialPadding(): Int {
    return when (dots.size) {
      in 1..4 -> (DOT_BOUND - (DOT_SIZE + DOT_SPACING) * 2 + DOT_SPACING) / 2 + 18.dp
      else -> 18.dp
    }
  }

  interface TargetScrollListener {
    fun targetScrollTo(target: Int)
  }
}