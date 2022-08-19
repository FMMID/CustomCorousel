package com.app.carousel.carousel.delegate

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData

interface ICarouselDelegateAdapter {

    fun onCreateViewHolder(
        parent: ViewGroup,
        eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener,
    ): BaseViewHolder<*>

    fun onBindViewHolder(holder: BaseViewHolder<*>, item: Any)

    /** to know that current adapter can work with item at position */
    fun isForViewType(item: Any): Boolean
}