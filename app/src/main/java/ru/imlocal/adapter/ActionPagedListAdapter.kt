package ru.imlocal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_action.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*
import ru.imlocal.R
import ru.imlocal.data.api.ACTION_IMAGE_DIRECTION
import ru.imlocal.data.api.BASE_IMAGE_URL
import ru.imlocal.data.api.SHOP_IMAGE_DIRECTION
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.models.Action
import ru.imlocal.ui.main.FragmentMainDirections

class ActionPagedListAdapter(val context: Context, val fragment: Fragment) :
    PagedListAdapter<Action, RecyclerView.ViewHolder>(ActionDiffCallBack()) {

    val ACTION_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == ACTION_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.list_item_action, parent, false)
            return ActionItemViewHolder(view, fragment)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ACTION_VIEW_TYPE) {
            (holder as ActionItemViewHolder).bind(getItem(position))
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

    class ActionItemViewHolder(view: View, val fragment: Fragment) : RecyclerView.ViewHolder(view) {

        fun bind(action: Action?) {
            itemView.tv_shop_title.text = action?.place?.shopShortDescription
            itemView.tv_shop_rating.text = action?.place?.shopAvgRating.toString()
            itemView.tv_action_title.text = action?.title
            itemView.tv_shop_adress.text = action?.place?.shopAddress.toString()
            itemView.tv_action_description.text = action?.fullDesc
            itemView.tv_date.text = action?.begin + " - " + action?.end
//            itemView.ib_share.text = place?.shopAvgRating.toString()
//            itemView.ib_add_to_favorites.text = place?.shopAvgRating.toString()

            Glide.with(itemView.context)
                .load(BASE_IMAGE_URL + SHOP_IMAGE_DIRECTION + action?.place?.placePhotos?.get(0)?.shopPhoto)
                .into(itemView.iv_icon)

            Glide.with(itemView.context)
                .load(BASE_IMAGE_URL + ACTION_IMAGE_DIRECTION + action?.actionPhotos?.get(0)?.actionPhoto)
                .into(itemView.iv_action_icon)

            itemView.setOnClickListener {
                val action =
                    FragmentMainDirections.actionFragmentMainToFragmentVitrinaAction(
                        action!!.id
                    )
                NavHostFragment.findNavController(fragment).navigate(action)
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