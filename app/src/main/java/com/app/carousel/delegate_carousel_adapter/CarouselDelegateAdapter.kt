package com.app.carousel.delegate_carousel_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData

abstract class CarouselDelegateAdapter<VH : BaseViewHolder, T> : ICarouselDelegateAdapter {

    protected abstract fun onBindViewHolder(view: View, item: T, viewHolder: VH)

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected abstract fun createViewHolder(
        parent: View,
        eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener
    ): VH

    override fun onRecycled(holder: BaseViewHolder) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
        eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener,
    ): VH {
        val inflatedView = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)
        val holder = createViewHolder(inflatedView, eventObserver, onClickListener)
        holder.setListener(object : BaseViewHolder.ItemInflateListener {

            override fun inflated(viewType: Any, view: View) {
                onBindViewHolder(view, viewType as T, holder)
            }
        })
        return holder
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder, items: List<Any>, position: Int
    ) {
        holder.bind(items[position])
    }
}