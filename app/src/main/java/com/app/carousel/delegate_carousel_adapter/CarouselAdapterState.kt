package com.app.carousel.delegate_carousel_adapter

data class AdaptersState(
    private val adapterISCarousels: List<ICarouselDelegateAdapter>,
    val data: List<Any> = emptyList()
) {

    private val adapterPositionsCache = Array<Int>(data.size) { -1 }

    fun getAdapterPosition(itemPosition: Int): Int =
        adapterPositionsCache[itemPosition].takeIf { it != -1 }
            ?: adapterISCarousels.indexOfFirst { it.isForViewType(data, itemPosition) }.takeIf { it != -1 }
                ?.also { adapterPositionsCache[itemPosition] = it }
            ?: error("Provide adapter for type ${data[itemPosition].javaClass} at position: $itemPosition")

    fun getAdapter(adapterPosition: Int): ICarouselDelegateAdapter = adapterISCarousels[adapterPosition]

    fun getAdapterByItemPosition(itemPosition: Int): ICarouselDelegateAdapter =
        adapterISCarousels[getAdapterPosition(itemPosition)]
}