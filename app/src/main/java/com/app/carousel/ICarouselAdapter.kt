package com.app.carousel

import android.view.View
import androidx.lifecycle.MutableLiveData

interface ICarouselAdapter {

    val onClickListener: View.OnClickListener

    val eventObserver: MutableLiveData<Any>
}