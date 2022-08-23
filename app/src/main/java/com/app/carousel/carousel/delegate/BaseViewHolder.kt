package com.app.carousel.carousel.delegate

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T : ICarouselDelegateModel>(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: T)

    abstract fun onRecycled()
}