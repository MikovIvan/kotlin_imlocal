package ru.imlocal.ui.places

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list_places.*
import kotlinx.android.synthetic.main.sort_menu.*
import ru.imlocal.R
import ru.imlocal.adapter.CategoryAdapter
import ru.imlocal.adapter.PlacePagedListAdapter
import ru.imlocal.data.api.Api
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlacePagedListRepository
import ru.imlocal.ui.main.FragmentMainDirections

class FragmentListPlaces : Fragment() {

    private lateinit var viewModel: ListPlacesViewModel
    lateinit var placeRepository: PlacePagedListRepository

    companion object {
        fun newInstance() = FragmentListPlaces()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_places, container, false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            queryHint = requireContext().getString(R.string.search_place)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.handleSearch(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.handleSearch(newText)
                    return true
                }
            })
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort -> viewModel.handleSortMenu()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService: Api = Api.getClient()
        placeRepository = PlacePagedListRepository(apiService)

        viewModel = getViewModel()

        val placeAdapter = PlacePagedListAdapter {
            val action = FragmentMainDirections.actionFragmentMainToFragmentVitrinaPlace(
                it.shopId
            )
            NavHostFragment.findNavController(this).navigate(action)
        }
        with(rv_fragment_list_places) {
            layoutManager = LinearLayoutManager(activity)
            adapter = placeAdapter
        }

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

        viewModel.observeState(this) {
            renderUI(it)
        }

        setupSortMenu()
    }

    private fun renderUI(state: State) {
        if (state.isSortMenuShow) sort_sub_menu_places.open() else sort_sub_menu_places.close()

        btn_sort_by_rating.isChecked = state.isSortByRating
        btn_sort_by_distance.isChecked = state.isSortByDistance

//        btn_toggle_group.addOnButtonCheckedListener { group, checkedId, isChecked ->
//            when (checkedId) {
//                0 -> viewModel.sortByRating()
//                1 -> viewModel.sortByDistance()
//            }
//        }

    }

    private fun setupSortMenu() {
        btn_sort_by_rating.setOnClickListener {
            viewModel.sortByRating()
            viewModel.handleSortMenu()
            Toast.makeText(activity, "tv_sort_by_rating click", Toast.LENGTH_SHORT).show()
        }
        btn_sort_by_distance.setOnClickListener {
            viewModel.sortByDistance()
            viewModel.handleSortMenu()
            Toast.makeText(context, "tv_sort_by_distance click", Toast.LENGTH_SHORT).show()
        }

        with(rv_category) {
            layoutManager = GridLayoutManager(activity, 3)
            adapter = CategoryAdapter("place") {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                viewModel.handleSortMenu()
            }
        }
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