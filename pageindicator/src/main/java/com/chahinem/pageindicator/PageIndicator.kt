package com.chahinem.pageindicator

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import com.chahinem.pageindicator.DotManager.TargetScrollListener

class PageIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), TargetScrollListener {

  private lateinit var dotSizes: IntArray
  private lateinit var dotAnimators: Array<ValueAnimator>

  private val defaultPaint = Paint().apply { isAntiAlias = true }
  private val selectedPaint = Paint().apply { isAntiAlias = true }

  private val dotSize: Int
  private val dotSizeMap: Map<Byte, Int>
  private val dotBound: Int
  private val dotSpacing: Int
  private val animDuration: Long
  private val animInterpolator: Interpolator

  private var dotManager: DotManager? = null
  private var scrollAmount: Int = 0
  private var scrollAnimator: ValueAnimator? = null
  private var initialPadding: Int = 0

  private var scrollListener: RecyclerView.OnScrollListener? = null
  private var pageChangeListener: ViewPager.OnPageChangeListener? = null

  private var _count = 0
  private var count: Int
    set(value) {
      _count = value
      dotManager = DotManager(
          _count,
          dotSize,
          dotSpacing,
          dotBound,
          dotSizeMap,
          this)

      dotSizes = IntArray(_count)
      dotManager?.let { it.dots.forEachIndexed { index, dot -> dotSizes[index] = it.dotSizeFor(dot) } }
      dotAnimators = Array(_count, { _ -> ValueAnimator() })

      initialPadding = when (count) {
        in 1..4 -> (dotBound + (4 - count) * (dotSize + dotSpacing) + dotSpacing) / 2
        else -> 2 * (dotSize + dotSpacing)
      }
      invalidate()
    }
    get() = _count

  init {
    val ta = getContext().obtainStyledAttributes(attrs, R.styleable.PageIndicator)
    dotSizeMap = mapOf(
        BYTE_6 to ta.getDimensionPixelSize(R.styleable.PageIndicator_piSize1, 6.dp),
        BYTE_5 to ta.getDimensionPixelSize(R.styleable.PageIndicator_piSize2, 5f.dp),
        BYTE_4 to ta.getDimensionPixelSize(R.styleable.PageIndicator_piSize3, 4.5f.dp),
        BYTE_3 to ta.getDimensionPixelSize(R.styleable.PageIndicator_piSize4, 3f.dp),
        BYTE_2 to ta.getDimensionPixelSize(R.styleable.PageIndicator_piSize5, 2.5f.dp),
        BYTE_1 to ta.getDimensionPixelSize(R.styleable.PageIndicator_piSize6, .5f.dp)
    )
    dotSize = dotSizeMap.values.max() ?: 0
    dotSpacing = ta.getDimensionPixelSize(R.styleable.PageIndicator_piDotSpacing, 3.dp)
    dotBound = ta.getDimensionPixelSize(R.styleable.PageIndicator_piDotBound, 40.dp)

    animDuration = ta.getInteger(
        R.styleable.PageIndicator_piAnimDuration, DEFAULT_ANIM_DURATION).toLong()
    defaultPaint.color = ta.getColor(
        R.styleable.PageIndicator_piDefaultColor, resources.getColor(R.color.pi_default_color))
    selectedPaint.color = ta.getColor(
        R.styleable.PageIndicator_piSelectedColor, resources.getColor(R.color.pi_selected_color))
    animInterpolator = AnimationUtils.loadInterpolator(context, ta.getResourceId(
        R.styleable.PageIndicator_piAnimInterpolator,
        R.anim.pi_default_interpolator))
    ta.recycle()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    // FIXME: add support for `match_parent`
    setMeasuredDimension(4 * (dotSize + dotSpacing) + dotBound, dotSize)
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    var paddingStart = initialPadding
    // FIXME: do not iterate on the entire list but only a subset of it
    dotManager?.dots?.forEachIndexed { index, dot ->
      canvas?.drawCircle(
          paddingStart + dotSize / 2f - scrollAmount,
          dotSize / 2f,
          dotSizes[index] / 2f,
          when (dot) {
            BYTE_6 -> selectedPaint
            else -> defaultPaint
          })
      paddingStart += dotSize + dotSpacing
    }
  }

  override fun onSaveInstanceState(): Parcelable {
    val superState = super.onSaveInstanceState()
    val savedState = SavedState(superState)
    savedState.count = this.count
    savedState.selectedIndex = this.dotManager?.selectedIndex ?: 0
    return savedState
  }

  override fun onRestoreInstanceState(state: Parcelable?) {
    if (state !is SavedState) {
      super.onRestoreInstanceState(state)
      return
    }

    super.onRestoreInstanceState(state.superState)

    this.count = state.count
    for (i in 0 until state.selectedIndex) {
      swipeNext()
    }
  }

  override fun scrollToTarget(target: Int) {
    scrollAnimator?.cancel()
    scrollAnimator = ValueAnimator.ofInt(scrollAmount, target).apply {
      duration = animDuration
      interpolator = DEFAULT_INTERPOLATOR
      addUpdateListener { animation ->
        scrollAmount = animation.animatedValue as Int
        invalidate()
      }
      start()
    }
  }

  fun attachTo(recyclerView: RecyclerView) {
    recyclerView.removeOnScrollListener(scrollListener)
    count = recyclerView.adapter.itemCount
    scrollListener = ScrollListener(this)
    recyclerView.addOnScrollListener(scrollListener)
    scrollToTarget(0)
  }

  fun attachTo(viewPager: ViewPager) {
    pageChangeListener?.let { viewPager.removeOnPageChangeListener(it) }
    count = (viewPager.adapter as PagerAdapter).count
    pageChangeListener = PageChangeListener(this)
    viewPager.addOnPageChangeListener(pageChangeListener as OnPageChangeListener)
    scrollToTarget(0)
  }

  fun swipePrevious() {
    dotManager?.goToPrevious()
    animateDots()
  }

  fun swipeNext() {
    dotManager?.goToNext()
    animateDots()
  }

  private fun animateDots() {
    dotManager?.let {
      // FIXME: do not iterate on the entire list but only a subset of it
      it.dots.forEachIndexed { index, dot ->
        dotAnimators[index].cancel()
        dotAnimators[index] = ValueAnimator.ofInt(dotSizes[index], it.dotSizeFor(dot))
            .apply {
              duration = animDuration
              interpolator = DEFAULT_INTERPOLATOR
              addUpdateListener { animation ->
                dotSizes[index] = animation.animatedValue as Int
                invalidate()
              }
            }
        dotAnimators[index].start()
      }
    }
  }

  companion object {
    private const val BYTE_6 = 6.toByte()
    private const val BYTE_5 = 5.toByte()
    private const val BYTE_4 = 4.toByte()
    private const val BYTE_3 = 3.toByte()
    private const val BYTE_2 = 2.toByte()
    private const val BYTE_1 = 1.toByte()

    private const val DEFAULT_ANIM_DURATION = 200

    private val DEFAULT_INTERPOLATOR = DecelerateInterpolator()
  }
}