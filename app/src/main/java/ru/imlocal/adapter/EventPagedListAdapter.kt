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
import kotlinx.android.synthetic.main.list_item_event.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*
import ru.imlocal.R
import ru.imlocal.data.api.BASE_IMAGE_URL
import ru.imlocal.data.api.EVENT_IMAGE_DIRECTION
import ru.imlocal.data.newDateFormat
import ru.imlocal.data.newDateFormat2
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.models.Event
import ru.imlocal.ui.main.FragmentMainDirections

class EventPagedListAdapter(val context: Context, val fragment: Fragment) :
    PagedListAdapter<Event, RecyclerView.ViewHolder>(EventDiffCallBack()) {

    val EVENT_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == EVENT_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.list_item_event, parent, false)
            return EventItemViewHolder(view, fragment)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == EVENT_VIEW_TYPE) {
            (holder as EventItemViewHolder).bind(getItem(position))
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
            EVENT_VIEW_TYPE
        }
    }

    class EventDiffCallBack : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }

    class EventItemViewHolder(view: View, val fragment: Fragment) : RecyclerView.ViewHolder(view) {

        fun bind(event: Event?) {
            itemView.tv_event_title.text = event?.title

            with(itemView.tv_event_price) {
                text = when (event?.price) {
                    0 -> itemView.context.getString(R.string.en_free)
                    else -> event?.price.toString()
                }
            }

            with(itemView.tv_event_date) {
                text = when {
                    event?.end != null && !event.end.substring(0, 11).equals(event.begin.substring(0, 11)) ->
                        context.getString(R.string.dates_vitrina_event, event.begin.newDateFormat(), event.end.newDateFormat2())
                    else -> context.getString(R.string.date_vitrina_event, event?.begin?.newDateFormat())
                }
            }

            Glide.with(itemView.context)
                .load(BASE_IMAGE_URL + EVENT_IMAGE_DIRECTION + event?.eventPhotos?.get(0)?.eventPhoto)
                .into(itemView.iv_event_image)

            itemView.setOnClickListener {
                val action = FragmentMainDirections.actionFragmentMainToFragmentVitrinaEvent(event!!.id)
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