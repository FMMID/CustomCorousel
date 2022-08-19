package com.app.carousel.examples

import com.app.carousel.carousel.delegate.ICarouselDelegateModel

data class FirstTypeModel(
    val title: String,
    val description: String
):ICarouselDelegateModel

data class SecondTypeModel(
    val description: String,
    val article: String
):ICarouselDelegateModel