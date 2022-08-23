package com.app.carousel.carousel.delegate

class AdapterDelegatesStore(private val adapterISCarousels: List<ICarouselDelegateAdapter>) {

    private val adapterCache = HashMap<Int, ICarouselDelegateAdapter>()

    fun getAdapter(itemType: Int): ICarouselDelegateAdapter {
        return adapterCache.getOrElse(itemType) {
            adapterISCarousels.firstOrNull { it.isForViewType(itemType) }?.also {
                adapterCache[itemType] = it
            } ?: error("Provide adapter for type $itemType")
        }
    }
}