package com.app.carousel.examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.app.carousel.R
import com.app.carousel.fragment_carousel_adapter.BaseFragmentCarouselItem

class CustomFragment : BaseFragmentCarouselItem() {

    private lateinit var firstTitle: TextView
    private lateinit var firstDescription: TextView
    val TITLE = "FirstTypeModelTitle"
    val DESCRIPTION = "FirstTypeModelDescription"

    fun newInstance(data: FirstTypeModel): CustomFragment {
        val args = Bundle().apply {
            putString(TITLE, data.title)
            putString(DESCRIPTION, data.description)
        }

        val fragment = CustomFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.first_type_model, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firstTitle = view.findViewById(R.id.first_title)
        firstDescription = view.findViewById(R.id.first_description)

        firstTitle.text = arguments?.getString(TITLE, "")
        firstDescription.text = arguments?.getString(DESCRIPTION, "")

        firstTitle.setOnClickListener {
            eventObserver.postValue(it)
        }
    }
}