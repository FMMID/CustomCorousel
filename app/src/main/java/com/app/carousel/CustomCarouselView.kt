package com.app.carousel

import SegmentedProgressBar
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.app.carousel.carousel.delegate.CarouselCompositeDelegateAdapter
import com.app.carousel.carousel.delegate.ICarouselDelegateAdapter
import com.app.carousel.carousel.delegate.ICarouselDelegateModel
import com.app.carousel.carousel.fragment.BaseFragmentCarouselItem
import com.app.carousel.carousel.fragment.CarouselFragmentStateAdapter
import com.app.carousel.segmentedprogressbar.SegmentParams
import com.app.carousel.segmentedprogressbar.SegmentedProgressBarListener

class CustomCarouselView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_RADIUS = 10
        private const val DEFAULT_MARGIN = 50
        private const val DEFAULT_DURATION = 3000L
        private const val DEFAULT_ID_SEGMENT_PROGRESS_BAR = 10010
    }

    var eventObserver: MutableLiveData<Any> = MutableLiveData()
    val onClickElementListener: OnClickListener = OnClickListener { }

    private val viewPager: ViewPager2
    private val leftArrow: View
    private val rightArrow: View
    private val leftArrowPlaceholder: View
    private val rightArrowPlaceholder: View

    private var viewPagerOrientation = ViewPager2.ORIENTATION_HORIZONTAL
    private var segmentProgressBar: SegmentedProgressBar? = null

    // Видимость элементов
    private var isVisibleLeftArrow: Boolean = false
    private var isVisibleRightArrow: Boolean = false
    private var isVisibleLeftArrowPlaceholder: Boolean = false
    private var isVisibleRightArrowPlaceholder: Boolean = false
    private var isVisibleStatePage: Boolean = false

    private var progressSegmentBarMargin = DEFAULT_MARGIN
    private var segmentParams = SegmentParams(
        radius = DEFAULT_RADIUS,
        segmentBackgroundColor = ContextCompat.getColor(context, R.color.carousel_default_segment),
        segmentSelectedBackgroundColor = ContextCompat.getColor(context, R.color.carousel_default_selected),
    )

    init {
        View.inflate(context, R.layout.carousel_view, this)
        viewPager = findViewById(R.id.carousel_viewPager2)
        leftArrow = findViewById(R.id.left_arrow)
        rightArrow = findViewById(R.id.right_arrow)
        leftArrowPlaceholder = findViewById(R.id.left_arrow_placeholder)
        rightArrowPlaceholder = findViewById(R.id.right_arrow_placeholder)

        attrs?.let { initFromAttrs(context, attrs) }
        updateViewState()
    }

    /**
     * Инициализация параметров View из назначенных в xml
     */
    private fun initFromAttrs(context: Context, attrs: AttributeSet) {
        context.withStyledAttributes(attrs, R.styleable.CustomCarouselView) {
            isVisibleStatePage = getBoolean(R.styleable.CustomCarouselView_carousel_visible_state_page, false)
            isVisibleLeftArrow = getBoolean(R.styleable.CustomCarouselView_carousel_left_arrow_visibility, false)
            isVisibleRightArrow = getBoolean(R.styleable.CustomCarouselView_carousel_right_arrow_visibility, false)
            isVisibleLeftArrowPlaceholder = getBoolean(
                R.styleable.CustomCarouselView_carousel_left_arrow_placeholder_visibility,
                false
            )
            isVisibleRightArrowPlaceholder = getBoolean(
                R.styleable.CustomCarouselView_carousel_right_arrow_placeholder_visibility,
                false
            )
            progressSegmentBarMargin = getDimension(
                R.styleable.CustomCarouselView_carousel_progress_segment_bar_margin,
                0f
            ).dp()

            // Параметры сегментов
            with(segmentParams) {
                duration = getInteger(
                    R.styleable.CustomCarouselView_carousel_segment_duration,
                    DEFAULT_DURATION.toInt()
                ).toLong()
                margin = getDimension(
                    R.styleable.CustomCarouselView_carousel_progress_segment_bar_margin,
                    0f
                ).dp()
                radius = getDimension(
                    R.styleable.CustomCarouselView_carousel_segment_radius,
                    DEFAULT_RADIUS.px()
                ).dp()
                segmentBackgroundColor = getColor(
                    R.styleable.CustomCarouselView_carousel_segment_backgroundColor,
                    ContextCompat.getColor(context, R.color.carousel_default_segment)
                )
                segmentSelectedBackgroundColor = getColor(
                    R.styleable.CustomCarouselView_carousel_segment_selectedBackgroundColor,
                    ContextCompat.getColor(context, R.color.carousel_default_selected)
                )
                segmentStrokeColor = getColor(
                    R.styleable.CustomCarouselView_carousel_segment_strokeColor,
                    Color.BLACK
                )
                segmentStrokeWidth = getInteger(
                    R.styleable.CustomCarouselView_carousel_segment_strokeWidth,
                    0
                )
                segmentSelectedStrokeColor = getColor(
                    R.styleable.SegmentedProgressBar_segmentSelectedStrokeColor,
                    Color.BLACK
                )
            }
        }
    }

    /**
     * Обновить состояние view в связи с параметрами
     */
    private fun updateViewState() {
        leftArrow.isVisible = isVisibleLeftArrow
        rightArrow.isVisible = isVisibleRightArrow
        leftArrowPlaceholder.isVisible = isVisibleLeftArrowPlaceholder
        rightArrowPlaceholder.isVisible = isVisibleRightArrowPlaceholder
        invalidate() // попросить перерисоваться (изменение видимости placeholder-ов без этого не будет работать)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> segmentProgressBar?.pause()
            MotionEvent.ACTION_UP -> segmentProgressBar?.start()
        }
        return false
    }

    /**
     * Установить видимость индикатарам перелистывания
     */
    fun setVisibleSwitchArrow(isVisible: Boolean) {
        isVisibleLeftArrow = isVisible
        isVisibleRightArrow = isVisible
        leftArrow.isVisible = isVisibleLeftArrow
        rightArrow.isVisible = isVisibleRightArrow
    }

    /**
     * Установить видимость левого индикатора перелистывания
     */
    fun setLeftArrowVisible(isVisible: Boolean) {
        isVisibleLeftArrow = isVisible
        leftArrow.isVisible = isVisibleLeftArrow
    }

    /**
     * Установить видимость правого индикатора перелистывания
     */
    fun setRightArrowVisible(isVisible: Boolean) {
        isVisibleRightArrow = isVisible
        rightArrow.isVisible = isVisibleRightArrow
    }

    /**
     * Установить видимость подложки индикаторов перелистывания
     */
    fun setVisibleSwitchArrowPlaceholder(isVisible: Boolean) {
        isVisibleLeftArrowPlaceholder = isVisible
        isVisibleRightArrowPlaceholder = isVisible
        leftArrowPlaceholder.isVisible = isVisible
        rightArrowPlaceholder.isVisible = isVisible
    }

    /**
     * Установить видимость подложки левого индикатора
     */
    fun setVisibleSwitchArrowLeftPlaceholder(isVisible: Boolean) {
        isVisibleLeftArrowPlaceholder = isVisible
        leftArrowPlaceholder.isVisible = isVisible
    }

    /**
     * Установить видимость подложки правого индикатора
     */
    fun setVisibleSwitchArrowRightPlaceholder(isVisible: Boolean) {
        isVisibleRightArrowPlaceholder = isVisible
        rightArrowPlaceholder.isVisible = isVisible
    }

    fun setVisibleStatePage(isVisible: Boolean) {
        isVisibleStatePage = isVisible
    }

    fun setVisualSegmentParams(params: SegmentParams) {
        with(segmentParams) {
            duration = params.duration ?: duration ?: DEFAULT_DURATION
            margin = params.margin ?: margin
            radius = params.radius ?: radius
            segmentCount = params.segmentCount ?: segmentCount
            segmentBackgroundColor = params.segmentBackgroundColor ?: segmentBackgroundColor
            segmentSelectedBackgroundColor = params.segmentSelectedBackgroundColor ?: segmentSelectedBackgroundColor
            segmentStrokeColor = params.segmentStrokeColor ?: segmentStrokeColor
            segmentStrokeWidth = params.segmentStrokeWidth ?: segmentStrokeWidth
            segmentSelectedStrokeColor = params.segmentSelectedStrokeColor ?: segmentSelectedStrokeColor
        }
    }

    fun setProgressSegmentBarMargin(margin: Int) {
        progressSegmentBarMargin = margin
    }

    fun setCarouselOrientation(orientation: Int) {
        viewPagerOrientation = orientation
    }

    fun buildViewDelegateCarousel(
        dataForDelegateAdapters: List<ICarouselDelegateModel>,
        vararg carouselDelegateAdapters: ICarouselDelegateAdapter
    ) {
        val adapter = CarouselCompositeDelegateAdapter(
            onClickListener = onClickElementListener,
            eventObserver = eventObserver,
            adapterISCarousels = carouselDelegateAdapters.toList(),
            data = dataForDelegateAdapters
        )
        viewPager.adapter = adapter
        initProgressBar(dataForDelegateAdapters.size)
    }

    fun buildFragmentCarousel(
        dataForFragmentAdapter: List<BaseFragmentCarouselItem>,
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle
    ) {
        viewPager.adapter = CarouselFragmentStateAdapter(
            onClickListener = onClickElementListener,
            eventObserver = eventObserver,
            fragmentManager = fragmentManager,
            lifecycle = lifecycle,
            items = dataForFragmentAdapter
        )
        initProgressBar(dataForFragmentAdapter.size)
    }

    private fun initProgressBar(countOfItems: Int) {
        val context = viewPager.context
        segmentParams.segmentCount = segmentParams.segmentCount ?: countOfItems
        segmentProgressBar = SegmentedProgressBar(context, segmentParams).apply {
            this.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, 20)
            id = DEFAULT_ID_SEGMENT_PROGRESS_BAR
        }
        addView(segmentProgressBar)
        ConstraintSet().apply {
            clone(this@CustomCarouselView)
            segmentProgressBar?.let {
                connect(it.id, ConstraintSet.BOTTOM, this@CustomCarouselView.id, ConstraintSet.BOTTOM, DEFAULT_MARGIN)
                connect(
                    it.id,
                    ConstraintSet.END,
                    this@CustomCarouselView.id,
                    ConstraintSet.END,
                    progressSegmentBarMargin
                )
                connect(
                    it.id,
                    ConstraintSet.START,
                    this@CustomCarouselView.id,
                    ConstraintSet.START,
                    progressSegmentBarMargin
                )
            }
            applyTo(this@CustomCarouselView)
        }

        setListeners(countOfItems)
        segmentProgressBar?.isVisible = isVisibleStatePage
        segmentProgressBar?.start()
        viewPager.orientation = viewPagerOrientation
        viewPager.offscreenPageLimit = 1
    }

    private fun setListeners(countOfItems: Int) {

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                segmentProgressBar?.setPosition(position)
            }
        })

        segmentProgressBar?.listener = object : SegmentedProgressBarListener {
            override fun onPage(oldPageIndex: Int, newPageIndex: Int) {
                viewPager.currentItem = newPageIndex
            }

            override fun onFinished() {
                segmentProgressBar?.reset()
                viewPager.setCurrentItem(0, false)
            }
        }

        leftArrow.setOnClickListener {
            if (viewPager.currentItem - 1 < 0) {
                viewPager.setCurrentItem(countOfItems - 1, false)
            } else {
                viewPager.setCurrentItem(viewPager.currentItem - 1, true)
            }
        }

        rightArrow.setOnClickListener {
            if (viewPager.currentItem + 1 > (countOfItems - 1)) {
                viewPager.setCurrentItem(0, false)
            } else {
                viewPager.setCurrentItem(viewPager.currentItem + 1, true)
            }
        }
    }

    /**
     * Методы для преобразования dp в px и назад
     */
    private fun Float.dp() = (this / resources.displayMetrics.density).toInt()
    private fun Int.px() = (this * resources.displayMetrics.density)
}