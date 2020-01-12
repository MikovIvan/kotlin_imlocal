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
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list_places.*
import ru.imlocal.R
import ru.imlocal.adapter.ActionPagedListAdapter
import ru.imlocal.data.api.Api
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlacePagedListRepository

class FragmentListActions : Fragment() {

    private lateinit var viewModel: ListActionsViewModel
    lateinit var actionRepository: PlacePagedListRepository

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
        viewModel = getViewModel()

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_fragment_list_actions)
        val actionAdapter = ActionPagedListAdapter(activity as Context, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = actionAdapter

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
                return ListActionsViewModel(actionRepository) as T
            }
        })[ListActionsViewModel::class.java]
    }
}
