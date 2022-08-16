package com.app.carousel.examples

import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.app.carousel.R
import com.app.carousel.delegate_carousel_adapter.CarouselDelegateAdapter
import com.app.carousel.delegate_carousel_adapter.BaseViewHolder

class SecondTypeModelCarouselDelegateAdapterCarousel : CarouselDelegateAdapter<SecondTypeModelCarouselDelegateAdapterCarousel.SecondTypeModelViewHolder, SecondTypeModel>() {

    override val layoutId: Int = R.layout.second_type_model

    override fun onBindViewHolder(view: View, item: SecondTypeModel, viewHolder: SecondTypeModelViewHolder) {
        viewHolder.bind(item)
    }

    override fun createViewHolder(
        parent: View,
        eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener
    ): SecondTypeModelViewHolder {
        return SecondTypeModelViewHolder(parent, eventObserver, onClickListener)
    }

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is SecondTypeModel
    }

    class SecondTypeModelViewHolder(
        parent: View,
        eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener
    ) : BaseViewHolder(
        parent,
        eventObserver,
        onClickListener
    ) {
        private val secondDescription: TextView = parent.findViewById(R.id.second_description)
        private val secondArticle: TextView = parent.findViewById(R.id.second_article)

        fun bind(secondModel: SecondTypeModel) {
            secondDescription.text = secondModel.description
            secondArticle.text = secondModel.article
        }
    }
}