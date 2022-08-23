package com.app.carousel.carousel.delegate

//TODO убрать open, преименовать AdapterDelegatesStore
open class AdaptersState(private val adapterISCarousels: List<ICarouselDelegateAdapter>) {

    //TODO adapterPositionCache -> adapterCache
    //HashMap<Class<out Any>, ICarouselDelegateAdapter> -> HashMap<Int, ICarouselDelegateAdapter>
    private val adapterPositionCache = HashMap<Class<out Any>, ICarouselDelegateAdapter>()

    //TODO давай немного еще переделаем
    //параметром будет itemType: Int
    fun getAdapter(item: ICarouselDelegateModel): ICarouselDelegateAdapter {
        return adapterPositionCache.getOrElse(item::class.java) {
            adapterISCarousels.firstOrNull { it.isForViewType(item) }?.also {
                adapterPositionCache[item::class.java] = it
            } ?: error("Provide adapter for type ${item.javaClass}")
        }
    }
}