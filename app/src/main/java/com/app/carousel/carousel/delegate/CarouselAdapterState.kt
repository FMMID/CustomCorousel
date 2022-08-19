package com.app.carousel.carousel.delegate

open class AdaptersState(private val adapterISCarousels: List<ICarouselDelegateAdapter>) {

    private val adapterPositionCache = HashMap<Class<out Any>, ICarouselDelegateAdapter>()

    fun getAdapter(item: Any): ICarouselDelegateAdapter {
        return adapterPositionCache.getOrElse(item::class.java) {
            adapterISCarousels.firstOrNull { it.isForViewType(item) }?.also {
                adapterPositionCache[item::class.java] = it
            } ?: error("Provide adapter for type ${item.javaClass}")
        }
    }
}