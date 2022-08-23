package com.app.carousel

import SegmentedProgressBar
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
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
    private var isVisibleSwitchArrowPlaceholder: Boolean = false
    private var isVisibleSwitchArrow: Boolean = false
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
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> segmentProgressBar?.pause()
            MotionEvent.ACTION_UP -> segmentProgressBar?.start()
        }
        return false
    }

    fun setVisibleSwitchArrow(isVisible: Boolean) {
        isVisibleSwitchArrow = isVisible
    }

    fun setVisibleSwitchArrowPlaceholder(isVisible: Boolean) {
        isVisibleSwitchArrowPlaceholder = isVisible
    }

    fun setVisibleStatePage(isVisible: Boolean) {
        isVisibleStatePage = isVisible
    }

    fun setVisualSegmentParams(params: SegmentParams) {
        segmentParams.duration = params.duration ?: segmentParams.duration ?: DEFAULT_DURATION
        segmentParams.margin = params.margin ?: segmentParams.margin
        segmentParams.radius = params.radius ?: segmentParams.radius
        segmentParams.segmentCount = params.segmentCount ?: segmentParams.segmentCount
        segmentParams.segmentBackgroundColor = params.segmentBackgroundColor ?: segmentParams.segmentBackgroundColor
        segmentParams.segmentSelectedBackgroundColor = params.segmentSelectedBackgroundColor ?: segmentParams.segmentSelectedBackgroundColor
        segmentParams.segmentStrokeColor = params.segmentStrokeColor ?: segmentParams.segmentStrokeColor
        segmentParams.segmentStrokeWidth = params.segmentStrokeWidth ?: segmentParams.segmentStrokeWidth
        segmentParams.segmentSelectedStrokeColor = params.segmentSelectedStrokeColor ?: segmentParams.segmentSelectedStrokeColor
    }

    fun setProgressSegmentBarMargin(margin: Int) {
        progressSegmentBarMargin = margin
    }

    fun setCarouselOrientation(orientation: Int) {
        viewPagerOrientation = orientation
    }

    fun buildViewDelegateCarousel(dataForDelegateAdapters: List<ICarouselDelegateModel>, vararg carouselDelegateAdapters: ICarouselDelegateAdapter) {
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
                connect(it.id, ConstraintSet.END, this@CustomCarouselView.id, ConstraintSet.END, progressSegmentBarMargin)
                connect(it.id, ConstraintSet.START, this@CustomCarouselView.id, ConstraintSet.START, progressSegmentBarMargin)
            }
            applyTo(this@CustomCarouselView)
        }

        setListeners(countOfItems)

        segmentProgressBar?.start()
        segmentProgressBar?.isVisible = isVisibleStatePage
        viewPager.orientation = viewPagerOrientation
        viewPager.offscreenPageLimit = 1
        leftArrow.isVisible = isVisibleSwitchArrow
        rightArrow.isVisible = isVisibleSwitchArrow
        leftArrowPlaceholder.isVisible = isVisibleSwitchArrowPlaceholder
        rightArrowPlaceholder.isVisible = isVisibleSwitchArrowPlaceholder
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
}