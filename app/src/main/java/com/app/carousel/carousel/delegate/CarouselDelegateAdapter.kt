package com.app.carousel.carousel.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData

abstract class CarouselDelegateAdapter<VH : BaseViewHolder<T>, T> : ICarouselDelegateAdapter {

    protected abstract fun onBindViewHolder(view: View, item: T, viewHolder: VH)

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected abstract fun createViewHolder(
        parent: View,
        eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener
    ): VH

    override fun onCreateViewHolder(
        parent: ViewGroup,
        eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener,
    ): VH {
        val inflatedView = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)
        return createViewHolder(inflatedView, eventObserver, onClickListener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, item: Any) {
        (holder as BaseViewHolder<T>).bind(item as T)
    }
}