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
    private val data: List<ICarouselDelegateModel> = emptyList()
) : RecyclerView.Adapter<BaseViewHolder<ICarouselDelegateModel>>(), ICarouselAdapter {

    protected open var adapterState = AdapterDelegatesStore(adapterISCarousels.toList())

    override fun getItemViewType(position: Int): Int = data[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ICarouselDelegateModel> =
        adapterState
            .getAdapter(viewType)
            .onCreateViewHolder(parent, eventObserver, onClickListener)

    override fun onBindViewHolder(holder: BaseViewHolder<ICarouselDelegateModel>, position: Int) = holder.bind(data[position])

    override fun onViewRecycled(holder: BaseViewHolder<ICarouselDelegateModel>) = holder.onRecycled()

    override fun getItemCount(): Int = data.size
}