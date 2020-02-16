package ru.imlocal.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.favorites_event.view.*
import kotlinx.android.synthetic.main.favorites_place.view.ib_add_to_favorites
import ru.imlocal.R
import ru.imlocal.data.Constants
import ru.imlocal.data.api.BASE_IMAGE_URL
import ru.imlocal.data.api.EVENT_IMAGE_DIRECTION
import ru.imlocal.data.newDateFormat
import ru.imlocal.data.newDateFormat2
import ru.imlocal.models.Event
import kotlin.math.min

class RecyclerViewAdapterFavEvents(
    private val favEvents: List<Event>,
    var fullShow: Boolean = false,
    private val listener: (Event) -> Unit,
    private val listener2: (Event, position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapterFavEvents.FavEventsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavEventsViewHolder {
        return FavEventsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorites_event, parent, false))
    }

    override fun getItemCount(): Int = if (fullShow) favEvents.size else min(favEvents.size, 2)

    override fun onBindViewHolder(holder: FavEventsViewHolder, position: Int) {
        holder.bind(favEvents[position], listener, listener2)
    }

    fun showAll(fact: Boolean) {
        fullShow = fact
        notifyDataSetChanged()
    }

    inner class FavEventsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(favEvent: Event?, listener: (Event) -> Unit, listener2: (Event, position: Int) -> Unit) {
            itemView.tv_fav_event_title.text = favEvent?.title
            itemView.tv_fav_event_price.text = favEvent?.price.toString() + Constants.KEY_RUB
            with(itemView.tv_fav_event_date) {
                text = when {
                    favEvent?.end != null && !favEvent.end.substring(0, 11).equals(favEvent.begin.substring(0, 11)) ->
                        context.getString(
                            R.string.dates_vitrina_event,
                            favEvent.begin.newDateFormat(),
                            favEvent.end.newDateFormat2()
                        )
                    else -> context.getString(R.string.date_vitrina_event, favEvent?.begin?.newDateFormat())
                }
            }

            Glide.with(itemView.context)
                .load(BASE_IMAGE_URL + EVENT_IMAGE_DIRECTION + favEvent?.eventPhotos?.get(0)?.eventPhoto)
                .into(itemView.iv_fav_event_image)

            itemView.setOnClickListener {
                listener(favEvent!!)
            }

            itemView.ib_add_to_favorites.setOnClickListener {
                listener2(favEvent!!, adapterPosition)
            }
        }
    }
}