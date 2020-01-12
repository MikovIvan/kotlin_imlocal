package ru.imlocal.ui.places

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list_places.*
import ru.imlocal.R
import ru.imlocal.adapter.PlacePagedListAdapter
import ru.imlocal.data.api.Api
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlacePagedListRepository

class FragmentListPlaces : Fragment() {

    private lateinit var viewModel: ListPlacesViewModel

    lateinit var placeRepository: PlacePagedListRepository

    companion object {
        fun newInstance() = FragmentListPlaces()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_places, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService: Api = Api.getClient()
        placeRepository = PlacePagedListRepository(apiService)

        viewModel = getViewModel()

        val activity = activity as Context

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_fragment_list_places)
        val placeAdapter = PlacePagedListAdapter(activity, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = placeAdapter

        fab.setOnClickListener { findNavController().navigate(R.id.fragment_map) }

        viewModel.placePagedList.observe(this, Observer {
            placeAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_list_places.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_list_places.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                placeAdapter.setNetworkState(it)
            }
        })

    }

    private fun getViewModel(): ListPlacesViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ListPlacesViewModel(placeRepository) as T
            }
        })[ListPlacesViewModel::class.java]
    }
}