package ru.imlocal.ui.actions

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
import kotlinx.android.synthetic.main.fragment_list_actions.*
import kotlinx.android.synthetic.main.fragment_list_places.progress_bar_list_places
import kotlinx.android.synthetic.main.fragment_list_places.txt_error_list_places
import kotlinx.android.synthetic.main.list_item_action.*
import ru.imlocal.R
import ru.imlocal.adapter.ActionPagedListAdapter
import ru.imlocal.data.api.Api
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlacePagedListRepository
import ru.imlocal.extensions.showLoginSnackbar
import ru.imlocal.extensions.showSnackbar
import ru.imlocal.models.FavType
import ru.imlocal.ui.favorites.FavoritesRepository
import ru.imlocal.utils.getUser

class FragmentListActions : Fragment() {

    private lateinit var viewModel: ListActionsViewModel
    lateinit var actionRepository: PlacePagedListRepository
    lateinit var favoritesRepository: FavoritesRepository
    lateinit var actionAdapter: ActionPagedListAdapter

    companion object {
        fun newInstance() = FragmentListActions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_actions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService: Api = Api.getClient()
        actionRepository = PlacePagedListRepository(apiService)
        favoritesRepository = FavoritesRepository(apiService, context!!)
        viewModel = getViewModel()

        actionAdapter = ActionPagedListAdapter(activity as Context, this, favoritesRepository,
            listener = { action, position ->
                viewModel.share(context)
            },
            listener2 = { action, btn ->
                if (getUser(context!!).isLogin) {
                    if (!viewModel.isFavorite(action.id, FavType.ACTION)) {
                        viewModel.addToFavorites(action.id, context!!, FavType.ACTION)
                        btn.setImageResource(R.drawable.ic_heart_pressed)
                        cl_list_item_action.showSnackbar(resources.getString(R.string.add_to_favorite))
                    } else {
                        viewModel.deleteFromFavorites(action.id, context!!, FavType.ACTION)
                        btn.setImageResource(R.drawable.ic_heart)
                        cl_list_item_action.showSnackbar(resources.getString(R.string.delete_from_favorites))
                    }
                } else {
                    cl_list_item_action.showLoginSnackbar(
                        fragment = this,
                        action = FragmentListActionsDirections.actionFragmentListActionsToFragmentLogin()
                    )
                }
            })

        with(rv_fragment_list_actions) {
            layoutManager = LinearLayoutManager(activity)
            adapter = actionAdapter
        }

        viewModel.actionPagedList.observe(this, Observer {
            actionAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_list_places.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_list_places.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                actionAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): ListActionsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ListActionsViewModel(
                    actionRepository,
                    favoritesRepository
                ) as T
            }
        })[ListActionsViewModel::class.java]
    }
}
