package ru.imlocal.ui.events.event

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_vitrina_event.*
import ru.imlocal.R
import ru.imlocal.data.Constants
import ru.imlocal.data.api.Api
import ru.imlocal.data.api.BASE_IMAGE_URL
import ru.imlocal.data.api.EVENT_IMAGE_DIRECTION
import ru.imlocal.data.newDateFormat
import ru.imlocal.data.newDateFormat2
import ru.imlocal.data.repository.EventRepository
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.UserRepository
import ru.imlocal.extensions.showLoginSnackbar
import ru.imlocal.extensions.showSnackbar
import ru.imlocal.models.Event
import ru.imlocal.models.FavType
import ru.imlocal.ui.favorites.FavoritesRepository

class FragmentVitrinaEvent : Fragment() {

    private lateinit var viewModel: VitrinaEventViewModel
    private lateinit var eventRepository: EventRepository
    private lateinit var favoritesRepository: FavoritesRepository
    private val args: FragmentVitrinaEventArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vitrina_event, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_vitrina, menu)
        if (UserRepository.getUser().isLogin) {
            if (!viewModel.isFavorite(args.eventId, FavType.EVENT)) {
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
                if (UserRepository.getUser().isLogin) {
                    if (!viewModel.isFavorite(args.eventId, FavType.EVENT)) {
                        viewModel.addToFavorites(args.eventId, context!!, FavType.EVENT)
                        item.setIcon(R.drawable.ic_heart_pressed)
                        cl_vitrina_event.showSnackbar(resources.getString(R.string.add_to_favorite))
                    } else {
                        viewModel.deleteFromFavorites(args.eventId, context!!, FavType.EVENT)
                        item.setIcon(R.drawable.ic_heart)
                        cl_vitrina_event.showSnackbar(resources.getString(R.string.delete_from_favorites))
                    }
                } else {
                    cl_vitrina_event.showLoginSnackbar(
                        fragment = this,
                        action = FragmentVitrinaEventDirections.actionFragmentVitrinaEventToFragmentLogin()
                    )
                }
            }
            R.id.share -> viewModel.share(context)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService: Api = Api.getClient()
        eventRepository = EventRepository(apiService)
        favoritesRepository = FavoritesRepository(apiService)
//        favoritesRepository = FavoritesRepository(apiService, context!!)

        viewModel = getViewModel(args.eventId)

        viewModel.actionDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_vitrina_event.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_vitrina_event.visibility =
                if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(event: Event) {
        Glide.with(context as Context)
            .load(BASE_IMAGE_URL + EVENT_IMAGE_DIRECTION + event.eventPhotos.get(0).eventPhoto)
            .into(iv_vitrina)

        tv_vitrina_name_of_event.text = event.title
        tv_adress.text = event.address
        when (event.eventTypeId) {
            1 -> tv_event_type.text = "Еда"
            2 -> tv_event_type.text = "Дети"
            3 -> tv_event_type.text = "Спорт"
            4 -> tv_event_type.text = "Город"
            5 -> tv_event_type.text = "Ярмарка"
            6 -> tv_event_type.text = "Творчество"
            7 -> tv_event_type.text = "Театр"
            8 -> tv_event_type.text = "Шоу"
        }

        with(tv_price) {
            text = when (event.price) {
                0 -> getString(R.string.free)
                else -> resources.getString(R.string.price, event.price, Constants.KEY_RUB)
            }
        }

        with(tv_when) {
            text = when {
                event.end != null && !event.end.substring(0, 11).equals(event.begin.substring(0, 11)) ->
                    resources.getString(
                        R.string.dates_vitrina_event,
                        event.begin.newDateFormat(),
                        event.end.newDateFormat2()
                    )
                else -> resources.getString(
                    R.string.date_vitrina_event,
                    event.begin.newDateFormat()
                )
            }
        }
        tv_about_event_text.text = event.description
    }

    private fun getViewModel(actionId: Int): VitrinaEventViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return VitrinaEventViewModel(
                    eventRepository,
                    favoritesRepository,
                    actionId
                ) as T
            }
        })[VitrinaEventViewModel::class.java]
    }

}
