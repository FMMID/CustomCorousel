package com.app.carousel.carousel.delegate

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.app.carousel.ICarouselAdapter

open class CarouselCompositeDelegateAdapter(
    override val onClickListener: View.OnClickListener,
    override val eventObserver: MutableLiveData<Any> = MutableLiveData(),
    private val adapterISCarousels: List<ICarouselDelegateAdapter>,
    private val data: List<Any> = emptyList()
) : RecyclerView.Adapter<BaseViewHolder<*>>(), ICarouselAdapter {

    protected open var adapterState = AdaptersState(adapterISCarousels.toList())

    override fun getItemViewType(itemPosition: Int): Int = itemPosition

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> =
        adapterState
            .getAdapter(data[viewType])
            .onCreateViewHolder(parent, eventObserver, onClickListener)

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) =
        adapterState
            .getAdapter(data[position])
            .onBindViewHolder(holder, data[position])

    override fun onViewRecycled(holder: BaseViewHolder<*>) = holder.onRecycled()

    override fun getItemCount(): Int = data.size
}