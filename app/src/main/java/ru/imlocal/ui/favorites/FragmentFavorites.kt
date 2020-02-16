package ru.imlocal.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_favorites.*
import ru.imlocal.R
import ru.imlocal.data.api.Api
import ru.imlocal.models.FavType
import ru.imlocal.models.Favorites

class FragmentFavorites : Fragment() {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var favoritesRepository: FavoritesRepository
    private lateinit var adapterFavPlaces: RecyclerViewAdapterFavPlaces
    private lateinit var adapterFavActions: RecyclerViewAdapterFavActions
    private lateinit var adapterFavEvents: RecyclerViewAdapterFavEvents

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiService: Api = Api.getClient()
        favoritesRepository = FavoritesRepository(apiService)
        viewModel = getViewModel()

        viewModel.favoritesPlaces.observe(this, Observer {
            bindUi(it)
        })

        btn_show_all_places.setOnClickListener {
            adapterFavPlaces.showAll(!adapterFavPlaces.fullShow)
            if (adapterFavPlaces.fullShow) {
                btn_show_all_places.text = "Скрыть все мои места"
            } else {
                btn_show_all_places.text = "Показать все мои места"
            }
        }

        btn_show_all_actions.setOnClickListener {
            adapterFavActions.showAll(!adapterFavActions.fullShow)
            if (adapterFavActions.fullShow) {
                btn_show_all_actions.text = "Скрыть все мои акции"
            } else {
                btn_show_all_actions.text = "Показать все мои акции"
            }
        }

        btn_show_all_events.setOnClickListener {
            adapterFavEvents.showAll(!adapterFavEvents.fullShow)
            if (adapterFavEvents.fullShow) {
                btn_show_all_events.text = "Скрыть все мои события"
            } else {
                btn_show_all_events.text = "Показать все мои события"
            }
        }
    }

    private fun bindUi(favorites: Favorites) {
        adapterFavPlaces = RecyclerViewAdapterFavPlaces(favorites.placesFavoritesList!!.values.toList(),
            listener = {
                val action = FragmentFavoritesDirections.actionFragmentFavoritesToFragmentVitrinaPlace(it.shopId)
                NavHostFragment.findNavController(this@FragmentFavorites).navigate(action)
            },
            listener2 = { place, position ->
                viewModel.deleteFromFavorites(place.shopId, context!!, FavType.PLACE)
                adapterFavPlaces.notifyItemRemoved(position)
            })

        with(rv_list_fav_places) {
            layoutManager = GridLayoutManager(context, 2)
            adapter = adapterFavPlaces
        }

        adapterFavActions = RecyclerViewAdapterFavActions(favorites.actionsFavoritesList!!.values.toList(),
            listener = {
                val action = FragmentFavoritesDirections.actionFragmentFavoritesToFragmentVitrinaAction(it.id)
                NavHostFragment.findNavController(this@FragmentFavorites).navigate(action)
            },
            listener2 = { action, position ->
                viewModel.deleteFromFavorites(action.id, context!!, FavType.ACTION)
                adapterFavActions.notifyItemRemoved(position)
            })

        with(rv_list_fav_actions) {
            layoutManager = GridLayoutManager(context, 2)
            adapter = adapterFavActions
        }

        adapterFavEvents = RecyclerViewAdapterFavEvents(favorites.eventsFavoritesList!!.values.toList(),
            listener = {
                val action = FragmentFavoritesDirections.actionFragmentFavoritesToFragmentVitrinaEvent(it.id)
                NavHostFragment.findNavController(this@FragmentFavorites).navigate(action)
            },
            listener2 = { event, position ->
                viewModel.deleteFromFavorites(event.id, context!!, FavType.EVENT)
                adapterFavEvents.notifyItemRemoved(position)
            })

        with(rv_list_fav_events) {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterFavEvents
        }
    }

    private fun getViewModel(): FavoritesViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return FavoritesViewModel(
                    favoritesRepository
                ) as T
            }
        })[FavoritesViewModel::class.java]
    }

}
