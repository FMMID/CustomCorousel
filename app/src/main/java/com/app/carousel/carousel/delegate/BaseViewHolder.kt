package com.app.carousel.carousel.delegate

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T : ICarouselDelegateModel>(
    //TODO это на parent, а itemView, private val нужно убрать
    private val parent: View
) : RecyclerView.ViewHolder(parent) {

    abstract fun bind(item: T)

    abstract fun onRecycled()
}