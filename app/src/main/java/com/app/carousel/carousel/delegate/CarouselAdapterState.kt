package com.app.carousel.carousel.delegate

open class AdaptersState(private val adapterISCarousels: List<ICarouselDelegateAdapter>) {

    private val adapterPositionCache = HashMap<String, ICarouselDelegateAdapter>()

    fun getAdapter(item: Any): ICarouselDelegateAdapter {
        return adapterPositionCache.getOrElse(item.toString()) {
            adapterISCarousels.indexOfFirst { it.isForViewType(item) }.takeIf { it != -1 }?.let {
                adapterISCarousels[it]
            }?.also {
                adapterPositionCache[item.toString()] = it
            } ?: error("Provide adapter for type ${item.javaClass}")
        }
    }
}