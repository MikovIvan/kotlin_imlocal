package ru.imlocal.ui.places.place

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_vitrina_shop.*
import ru.imlocal.R
import ru.imlocal.data.api.Api
import ru.imlocal.data.api.BASE_IMAGE_URL
import ru.imlocal.data.api.SHOP_IMAGE_DIRECTION
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlaceRepository
import ru.imlocal.models.FavType
import ru.imlocal.models.Place
import ru.imlocal.ui.favorites.FavoritesRepository
import ru.imlocal.utils.getUser


class FragmentVitrinaPlace : Fragment() {

    private lateinit var viewModel: VitrinaPlaceViewModel
    private lateinit var placeRepository: PlaceRepository
    private lateinit var favoritesRepository: FavoritesRepository
    private val args: FragmentVitrinaPlaceArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vitrina_shop, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_vitrina, menu)
        if (getUser(context!!).isLogin) {
            if (!viewModel.isFavorite(args.placeId, FavType.PLACE)) {
                menu.getItem(0).setIcon(R.drawable.ic_heart)
            } else {
                menu.getItem(0).setIcon(R.drawable.ic_heart_pressed)
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_to_favorites -> {
                if (getUser(context!!).isLogin) {
                    if (!viewModel.isFavorite(args.placeId, FavType.PLACE)) {
                        viewModel.addToFavorites(args.placeId, context!!, FavType.PLACE)
                        item.setIcon(R.drawable.ic_heart_pressed)
                        Snackbar.make(view!!, resources.getString(R.string.add_to_favorite), Snackbar.LENGTH_SHORT).show()
                    } else {
                        viewModel.deleteFromFavorites(args.placeId, context!!, FavType.PLACE)
                        item.setIcon(R.drawable.ic_heart)
                        Snackbar.make(view!!, resources.getString(R.string.delete_from_favorites), Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.share -> viewModel.share(context)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService: Api = Api.getClient()
        placeRepository = PlaceRepository(apiService)
        favoritesRepository = FavoritesRepository(apiService, context!!)

        viewModel = getViewModel(args.placeId)

        viewModel.placeDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_vitrina_place.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_vitrina_place.visibility =
                if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(place: Place) {
        tv_vitrina_name_of_place.text = place.shopShortName
        tv_adress.text = place.shopAddress.toString()
        tv_shop_timetable.text = place.shopWorkTime
        tv_shop_phone.text = place.shopPhone
        tv_price.text = "${place.shopCostMin} - ${place.shopCostMax}"
        tv_about_shop_text.text = place.shopFullDescription
        btn_rating.text = place.shopAvgRating.toString()
        for (shopPhoto in place.placePhotos) {
            when (place.placePhotos.size) {
                1 -> flipperImages(shopPhoto.shopPhoto, false)
                in 2..Int.MAX_VALUE -> flipperImages(shopPhoto.shopPhoto, true)
            }
        }
    }

    private fun flipperImages(photo: String, autostart: Boolean) {
        val imageView = ImageView(activity)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(this)
            .load(BASE_IMAGE_URL + SHOP_IMAGE_DIRECTION + photo)
            .into(imageView)
        with(flipper_vitrina_shop) {
            addView(imageView)
            flipInterval = 2000
            setInAnimation(activity, android.R.anim.slide_in_left)
            setOutAnimation(activity, android.R.anim.slide_out_right)
            if (autostart) {
                startFlipping()
            }
        }
    }

    private fun getViewModel(placeId: Int): VitrinaPlaceViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return VitrinaPlaceViewModel(
                    placeRepository,
                    favoritesRepository,
                    placeId
                ) as T
            }
        })[VitrinaPlaceViewModel::class.java]
    }
}