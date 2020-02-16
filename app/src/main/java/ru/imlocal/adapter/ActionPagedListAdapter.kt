package ru.imlocal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_action.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*
import kotlinx.coroutines.runBlocking
import ru.imlocal.R
import ru.imlocal.data.api.ACTION_IMAGE_DIRECTION
import ru.imlocal.data.api.BASE_IMAGE_URL
import ru.imlocal.data.api.SHOP_IMAGE_DIRECTION
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.models.Action
import ru.imlocal.models.FavType
import ru.imlocal.ui.favorites.FavoritesRepository
import ru.imlocal.ui.main.FragmentMainDirections
import ru.imlocal.utils.getUser

class ActionPagedListAdapter(
    val context: Context,
    val fragment: Fragment,
    private val favoritesRepository: FavoritesRepository,
    private val listener: (Action, position: Int) -> Unit,
    private val listener2: (Action, btn: ImageButton) -> Unit
) : PagedListAdapter<Action, RecyclerView.ViewHolder>(ActionDiffCallBack()) {

    val ACTION_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return if (viewType == ACTION_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.list_item_action, parent, false)
            ActionItemViewHolder(view, fragment)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ACTION_VIEW_TYPE) {
            (holder as ActionItemViewHolder).bind(context, getItem(position), favoritesRepository, listener, listener2)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            ACTION_VIEW_TYPE
        }
    }

    class ActionDiffCallBack : DiffUtil.ItemCallback<Action>() {
        override fun areItemsTheSame(oldItem: Action, newItem: Action): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Action, newItem: Action): Boolean {
            return oldItem == newItem
        }

    }

    class ActionItemViewHolder(view: View, val fragment: Fragment) :
        RecyclerView.ViewHolder(view) {

        fun bind(
            context: Context,
            action: Action?,
            favoritesRepository: FavoritesRepository,
            listener: (Action, position: Int) -> Unit,
            listener2: (Action, btn: ImageButton) -> Unit
        ) {
            runBlocking {
                if (getUser(context).isLogin) {
                    if (favoritesRepository.isFavorite(action!!.id, FavType.ACTION)) {
                        itemView.ib_add_to_favorites.setImageResource(R.drawable.ic_heart_pressed)
                    }
                } else {
                    itemView.ib_add_to_favorites.setImageResource(R.drawable.ic_heart)
                }
            }
            with(itemView) {
                tv_shop_title.text = action?.place?.shopShortName
                tv_shop_rating.text = action?.place?.shopAvgRating.toString()
                tv_action_title.text = action?.title
                tv_shop_adress.text = action?.place?.shopAddress.toString()
                tv_action_description.text = action?.fullDesc
                tv_date.text = action?.begin + " - " + action?.end
                ib_share.setOnClickListener {
                    listener(action!!, adapterPosition)
                }
                ib_add_to_favorites.setOnClickListener {
                    listener2(action!!, ib_add_to_favorites)
                }

                Glide.with(context)
                    .load(BASE_IMAGE_URL + SHOP_IMAGE_DIRECTION + action?.place?.placePhotos?.get(0)?.shopPhoto)
                    .into(iv_icon)

                Glide.with(context)
                    .load(BASE_IMAGE_URL + ACTION_IMAGE_DIRECTION + action?.actionPhotos?.get(0)?.actionPhoto)
                    .into(iv_action_icon)

                setOnClickListener {
                    val act = FragmentMainDirections.actionFragmentMainToFragmentVitrinaAction(action!!.id)
                    NavHostFragment.findNavController(fragment).navigate(act)
                }
            }

        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE;
            } else {
                itemView.progress_bar_item.visibility = View.GONE;
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            } else {
                itemView.error_msg_item.visibility = View.GONE;
            }
        }
    }


    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }

    }

}