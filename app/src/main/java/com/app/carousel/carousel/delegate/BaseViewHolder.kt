package com.app.carousel.carousel.delegate

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(
    private val parent: View
) : RecyclerView.ViewHolder(parent) {

    abstract fun bind(item: T)
}