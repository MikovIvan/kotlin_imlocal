package ru.imlocal.ui.places.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_vitrina_shop.*
import ru.imlocal.R
import ru.imlocal.data.api.Api
import ru.imlocal.data.api.BASE_IMAGE_URL
import ru.imlocal.data.api.SHOP_IMAGE_DIRECTION
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlaceRepository
import ru.imlocal.models.Place


class FragmentVitrinaPlace : Fragment() {

    private lateinit var viewModel: VitrinaPlaceViewModel
    private lateinit var placeRepository: PlaceRepository


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val safeArgs: FragmentVitrinaPlaceArgs by navArgs()
        val placeId = safeArgs.placeId

        val apiService: Api = Api.getClient()
        placeRepository = PlaceRepository(apiService)

        viewModel = getViewModel(placeId)

        viewModel.placeDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })


        return inflater.inflate(R.layout.fragment_vitrina_shop, container, false)
    }

    private fun bindUI(place: Place) {
        tv_vitrina_name_of_place.text = place.shopShortName
        tv_adress.text = place.shopAddress.toString()
        tv_shop_timetable.text = place.shopWorkTime
        tv_shop_phone.text = place.shopPhone
        tv_price.text = place.shopCostMin + "-" + place.shopCostMax
        tv_about_shop_text.text = place.shopFullDescription
        flipperImages(place.shopPhotos[0].shopPhoto, true)
    }

    fun flipperImages(photo: String, autostart: Boolean) {
        val imageView: ImageView = ImageView(activity)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(this)
            .load(BASE_IMAGE_URL + SHOP_IMAGE_DIRECTION + photo)
            .into(imageView)
        flipper_vitrina_shop.addView(imageView)
        flipper_vitrina_shop.flipInterval = 4000
        flipper_vitrina_shop.isAutoStart = autostart
        flipper_vitrina_shop.setInAnimation(activity, android.R.anim.slide_in_left)
        flipper_vitrina_shop.setOutAnimation(activity, android.R.anim.slide_out_right)
    }

    private fun getViewModel(placeId: Int): VitrinaPlaceViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return VitrinaPlaceViewModel(
                    placeRepository,
                    placeId
                ) as T
            }
        })[VitrinaPlaceViewModel::class.java]
    }
}