package com.app.carousel.carousel.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.carousel.ICarouselAdapter

class CarouselFragmentStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    override val onClickListener: View.OnClickListener,
    override val eventObserver: MutableLiveData<Any> = MutableLiveData(),
    var items: List<BaseFragmentCarouselItem>,
) : FragmentStateAdapter(fragmentManager, lifecycle), ICarouselAdapter {

    override fun getItemCount() = items.size

    fun setItemsAdapter(items: List<BaseFragmentCarouselItem>) {
        this.items = items
    }

    override fun createFragment(position: Int): Fragment {
        val item = items[position].apply {
            this.eventObserver = this@CarouselFragmentStateAdapter.eventObserver
            this.onClickListener = this@CarouselFragmentStateAdapter.onClickListener
        }
        return item
    }
}