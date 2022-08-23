package com.app.carousel.carousel.delegate

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData

interface ICarouselDelegateAdapter {

    fun onCreateViewHolder(
        parent: ViewGroup,
        eventObserver: MutableLiveData<Any>,
        onClickListener: View.OnClickListener,
    ): BaseViewHolder<ICarouselDelegateModel>

    /** to know that current adapter can work with item at position */
    //TODO itemType:Int
    fun isForViewType(item: Any): Boolean
}