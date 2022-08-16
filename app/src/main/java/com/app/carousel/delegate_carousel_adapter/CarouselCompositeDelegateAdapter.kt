package com.app.carousel.delegate_carousel_adapter

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.app.carousel.ICarouselAdapter

open class CarouselCompositeDelegateAdapter(
    override val onClickListener: View.OnClickListener,
    override val eventObserver: MutableLiveData<Any> = MutableLiveData(),
    adapterISCarousels: List<ICarouselDelegateAdapter>,
) : RecyclerView.Adapter<BaseViewHolder>(), ICarouselAdapter {

    protected open var adapterState = AdaptersState(adapterISCarousels.toList())

    override fun getItemViewType(itemPosition: Int): Int = adapterState.getAdapterPosition(itemPosition)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        adapterState
            .getAdapter(viewType)
            .onCreateViewHolder(parent, viewType, eventObserver, onClickListener)

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) =
        adapterState
            .getAdapter(getItemViewType(position))
            .onBindViewHolder(holder, adapterState.data, position)

    override fun onViewRecycled(holder: BaseViewHolder) =
        adapterState
            .getAdapter(holder.itemViewType)
            .onRecycled(holder)

    open fun swapData(data: List<Any>) {
        val newAdapterState = adapterState.copy(data = data)
        adapterState = newAdapterState
    }

    override fun getItemCount(): Int = adapterState.data.size
}