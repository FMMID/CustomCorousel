package com.app.carousel.examples

import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.app.carousel.R
import com.app.carousel.carousel.delegate.BaseViewHolder
import com.app.carousel.carousel.delegate.CarouselDelegateAdapter

class FirstTypeModelCarouselDelegateAdapterCarousel :
    CarouselDelegateAdapter<FirstTypeModelCarouselDelegateAdapterCarousel.FirstTypeModelViewHolder, FirstTypeModel>() {

    override val layoutId: Int = R.layout.first_type_model

    override fun onBindViewHolder(view: View, item: FirstTypeModel, viewHolder: FirstTypeModelViewHolder) {
        viewHolder.bind(item)
    }

    override fun createViewHolder(
        itemView: View,
        eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener
    ): FirstTypeModelViewHolder {
        return FirstTypeModelViewHolder(itemView, eventObserver, onClickListener)
    }

    override fun isForViewType(itemType: Int): Boolean {
        return itemType == layoutId
    }

    class FirstTypeModelViewHolder(
        itemView: View,
        val eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener
    ) : BaseViewHolder<FirstTypeModel>(itemView) {
        private val firstTitle: TextView = itemView.findViewById(R.id.first_title)
        private val firstDescription: TextView = itemView.findViewById(R.id.first_description)
        override fun bind(item: FirstTypeModel) {
            firstTitle.text = item.title
            firstDescription.text = item.description

            firstTitle.setOnClickListener {
                eventObserver.postValue(item.title)
            }
        }

        override fun onRecycled() {}
    }
}