package com.prolificinteractive

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DotsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  internal var dotCount: Int = 0
  internal lateinit var dotManager: DotManager

  private val bluePaint = Paint()
  private val greyPaint = Paint()

  init {
    bluePaint.isAntiAlias = true
    bluePaint.color = Color.parseColor("#3897F0")
    greyPaint.isAntiAlias = true
    greyPaint.color = Color.parseColor("#B2B2B2")
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    setMeasuredDimension((DOT_SIZE + DOT_SPACING) * dotCount - DOT_SPACING + 36.dp, 6.dp)
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    var paddingStart = dotManager.initialPadding()
    for (dot in dotManager.dots) {
      val dotSize = when (dot) {
        '6' -> 6.dp
        '5' -> 5.dp
        '4' -> 4.5f.dp
        '3' -> 3.dp
        '2' -> 2.5f.dp
        '1' -> 0.5f.dp
        else -> 0.dp
      }
      if (dot in '1'..'6') {
        canvas?.drawCircle(paddingStart + 3.dp.toFloat(), 3.dp.toFloat(), dotSize / 2f, when (dot) {
          '6' -> bluePaint
          else -> greyPaint
        })
      }

      paddingStart += 9.dp
    }
  }

  companion object {
    private val DOT_SIZE = 6.dp
    private val DOT_SPACING = 3.dp
  }
}