package com.app.carousel.delegate_carousel_adapter

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.Channel

open class BaseViewHolder(
    private val parent: View,
    private val eventObserver: MutableLiveData<Any>,
    private val onClickListener: View.OnClickListener,
) : RecyclerView.ViewHolder(parent) {

    private var listener: ItemInflateListener? = null

    fun setListener(listener: ItemInflateListener?) {
        this.listener = listener
    }

    open fun bind(item: Any) {
        listener?.inflated(item, itemView)
    }

    interface ItemInflateListener {
        fun inflated(viewType: Any, view: View)
    }
}