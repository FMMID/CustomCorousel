package com.app.carousel.examples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.carousel.CustomCarouselView
import com.app.carousel.R
import com.app.carousel.segmentedprogressbar.SegmentParams

class MainActivity : AppCompatActivity() {

    private lateinit var carousel: CustomCarouselView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        carousel = findViewById(R.id.custom_carousel_view)
        carousel.apply {
            setVisualSegmentParams(
                SegmentParams(
                    duration = 6000
                )
            )
            setVisibleStatePage(true)
            setVisibleSwitchArrow(false)
            setProgressSegmentBarMargin(150)
            buildViewDelegateCarousel(
                dataForDelegateAdapters = listOf(
                    FirstTypeModel("Заголовок 1", "Описание 1", R.layout.first_type_model),
                    SecondTypeModel("Описание 1", "Статья 1", R.layout.second_type_model),
                    FirstTypeModel("Заголовок 2", "Описание 2", R.layout.first_type_model),
                    SecondTypeModel("Описание 2", "Статья 2", R.layout.second_type_model),
                ),
                FirstTypeModelCarouselDelegateAdapterCarousel(),
                SecondTypeModelCarouselDelegateAdapterCarousel()
            )
        }
    }
}