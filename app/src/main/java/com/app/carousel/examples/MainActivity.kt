package com.app.carousel.examples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.carousel.CustomCarouselView
import com.app.carousel.R
import com.app.carousel.segmented_progress_bar.SegmentParams

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carousel: CustomCarouselView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        carousel = findViewById(R.id.custom_carousel_view)
        carousel.apply {
            setVisualSegmentParams(SegmentParams(
                duration = 6000
            ))
            setVisibleStatePage(true)
            setVisibleSwitchArrow(false)
            setProgressSegmentBarMargin(150)
            buildViewDelegateCarousel(
                dataForDelegateAdapters = listOf(
                    FirstTypeModel("Заголовок 1", "Описание 1"),
                    SecondTypeModel("Описание 1", "Статья 1"),
                    FirstTypeModel("Заголовок 2", "Описание 2"),
                    SecondTypeModel("Описание 2", "Статья 2"),
                ),
                FirstTypeModelCarouselDelegateAdapterCarousel(),
                SecondTypeModelCarouselDelegateAdapterCarousel()
            )
        }
    }
}