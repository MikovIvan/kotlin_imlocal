package ru.imlocal.ui.events

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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list_events.*
import kotlinx.android.synthetic.main.fragment_list_places.progress_bar_list_places
import kotlinx.android.synthetic.main.fragment_list_places.txt_error_list_places
import ru.imlocal.R
import ru.imlocal.adapter.EventPagedListAdapter
import ru.imlocal.data.api.Api
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlacePagedListRepository
import ru.imlocal.ui.main.FragmentMainDirections

class FragmentListEvents : Fragment() {

    private lateinit var viewModel: ListEventsViewModel
    lateinit var eventRepository: PlacePagedListRepository

    companion object {
        fun newInstance() = FragmentListEvents()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService: Api = Api.getClient()
        eventRepository = PlacePagedListRepository(apiService)

        viewModel = getViewModel()

        val eventAdapter = EventPagedListAdapter {
            val action = FragmentMainDirections.actionFragmentMainToFragmentVitrinaEvent(it.id)
            NavHostFragment.findNavController(this).navigate(action)
        }
        with(rv_fragment_list_events) {
            layoutManager = LinearLayoutManager(activity)
            adapter = eventAdapter
        }

        viewModel.eventPagedList.observe(this, Observer {
            eventAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_list_places.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_list_places.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                eventAdapter.setNetworkState(it)
            }
        })

    }

    private fun getViewModel(): ListEventsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ListEventsViewModel(eventRepository) as T
            }
        })[ListEventsViewModel::class.java]
    }
}
