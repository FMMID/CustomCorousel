package com.app.carousel.examples

import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.app.carousel.R
import com.app.carousel.carousel.delegate.BaseViewHolder
import com.app.carousel.carousel.delegate.CarouselDelegateAdapter

class SecondTypeModelCarouselDelegateAdapterCarousel :
    CarouselDelegateAdapter<SecondTypeModelCarouselDelegateAdapterCarousel.SecondTypeModelViewHolder, SecondTypeModel>() {

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

    override fun isForViewType(item: Any): Boolean {
        return item is SecondTypeModel
    }

    class SecondTypeModelViewHolder(
        parent: View,
        eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener
    ) : BaseViewHolder<SecondTypeModel>(parent) {
        private val secondDescription: TextView = parent.findViewById(R.id.second_description)
        private val secondArticle: TextView = parent.findViewById(R.id.second_article)

        override fun bind(item: SecondTypeModel) {
            secondDescription.text = item.description
            secondArticle.text = item.article
        }
    }
}