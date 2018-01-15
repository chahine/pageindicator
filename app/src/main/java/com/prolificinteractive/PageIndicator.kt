package com.prolificinteractive

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.HorizontalScrollView
import com.prolificinteractive.DotManager.TargetScrollListener

class PageIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr), TargetScrollListener {

  private lateinit var dotManager: DotManager

  private val dotPaint = Paint()

  init {
    dotPaint.isAntiAlias = true
    dotPaint.color = Color.parseColor("#3F51B5")
  }

  private lateinit var dotsView: DotsView

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    val count = 9
    dotManager = DotManager(count)
    dotManager.targetScrollListener = this
    dotsView = DotsView(context)
    dotsView.dotCount = count
    dotsView.dotManager = dotManager
    addView(dotsView)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    setMeasuredDimension(76.dp, 6.dp)
  }

  override fun targetScrollTo(target: Int) {
    Log.d("chahine", "target = [$target]")
    smoothScrollTo(target, target)
  }

  fun swipePrevious() {
    dotsView.dotManager.goToPrevious()
    dotsView.invalidate()
  }

  fun swipeNext() {
    dotsView.dotManager.goToNext()
    dotsView.invalidate()
  }
}