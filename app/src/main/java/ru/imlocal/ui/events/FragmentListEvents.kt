package ru.imlocal.ui.events

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list_places.*
import ru.imlocal.R
import ru.imlocal.adapter.EventPagedListAdapter
import ru.imlocal.data.api.Api
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlacePagedListRepository

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

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_fragment_list_events)
        val eventAdapter = EventPagedListAdapter(activity as Context, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = eventAdapter

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
