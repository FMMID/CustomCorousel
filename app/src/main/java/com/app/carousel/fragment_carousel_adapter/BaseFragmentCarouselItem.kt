package com.app.carousel.fragment_carousel_adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData

abstract class BaseFragmentCarouselItem : Fragment() {

    var onClickListener: View.OnClickListener = View.OnClickListener { }
    var eventObserver: MutableLiveData<Any> = MutableLiveData()
}