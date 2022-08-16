package com.app.carousel.delegate_carousel_adapter

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.Channel

interface ICarouselDelegateAdapter {

    fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
        eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener,
    ): BaseViewHolder

    fun onBindViewHolder(
        holder: BaseViewHolder,
        items: List<Any>,
        position: Int
    )

    fun onRecycled(holder: BaseViewHolder)

    /** to know that current adapter can work with item at position */
    fun isForViewType(items: List<Any>, position: Int): Boolean

}