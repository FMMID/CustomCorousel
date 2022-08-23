package com.app.carousel.carousel.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData

abstract class CarouselDelegateAdapter<VH : BaseViewHolder<T>, T:ICarouselDelegateModel> : ICarouselDelegateAdapter {

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
    ): BaseViewHolder<ICarouselDelegateModel> {
        val inflatedView = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)
        return createViewHolder(inflatedView, eventObserver, onClickListener) as BaseViewHolder<ICarouselDelegateModel>
    }
}