package com.app.carousel.examples

import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.app.carousel.R
import com.app.carousel.delegate_carousel_adapter.BaseViewHolder
import com.app.carousel.delegate_carousel_adapter.CarouselDelegateAdapter

class FirstTypeModelCarouselDelegateAdapterCarousel :
    CarouselDelegateAdapter<FirstTypeModelCarouselDelegateAdapterCarousel.FirstTypeModelViewHolder, FirstTypeModel>() {

    override val layoutId: Int = R.layout.first_type_model

    override fun onBindViewHolder(view: View, item: FirstTypeModel, viewHolder: FirstTypeModelViewHolder) {
        viewHolder.bind(item)
    }

    override fun createViewHolder(
        parent: View,
        eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener
    ): FirstTypeModelViewHolder {
        return FirstTypeModelViewHolder(parent, eventObserver, onClickListener)
    }

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is FirstTypeModel
    }

    class FirstTypeModelViewHolder(
        parent: View,
        val eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener
    ) : BaseViewHolder(
        parent,
        eventObserver,
        onClickListener
    ) {
        private val firstTitle: TextView = parent.findViewById(R.id.first_title)
        private val firstDescription: TextView = parent.findViewById(R.id.first_description)

        fun bind(firstModel: FirstTypeModel) {
            firstTitle.text = firstModel.title
            firstDescription.text = firstModel.description

            firstTitle.setOnClickListener {
                eventObserver.postValue(firstModel.title)
            }
        }
    }
}