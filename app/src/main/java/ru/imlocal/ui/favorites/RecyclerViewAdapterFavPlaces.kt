package ru.imlocal.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.favorites_place.view.*
import ru.imlocal.R
import ru.imlocal.data.api.BASE_IMAGE_URL
import ru.imlocal.data.api.SHOP_IMAGE_DIRECTION
import ru.imlocal.models.Place
import kotlin.math.min

class RecyclerViewAdapterFavPlaces(
    private val favPlaces: List<Place>,
    var fullShow: Boolean = false,
    private val listener: (Place) -> Unit,
    private val listener2: (Place, position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapterFavPlaces.FavPlacesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavPlacesViewHolder {
        return FavPlacesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorites_place, parent, false))
    }

    override fun getItemCount(): Int = if (fullShow) favPlaces.size else min(favPlaces.size, 2)

    override fun onBindViewHolder(holder: FavPlacesViewHolder, position: Int) {
        holder.bind(favPlaces[position], listener, listener2)
    }

    fun showAll(fact: Boolean) {
        fullShow = fact
        notifyDataSetChanged()
    }

    inner class FavPlacesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(favPlace: Place?, listener: (Place) -> Unit, listener2: (Place, position: Int) -> Unit) {
            itemView.tv_fav_place_title.text = favPlace?.shopShortName

            Glide.with(itemView.context)
                .load(BASE_IMAGE_URL + SHOP_IMAGE_DIRECTION + favPlace?.placePhotos?.get(0)?.shopPhoto)
                .into(itemView.iv_fav_place_icon)

            itemView.setOnClickListener {
                listener(favPlace!!)
            }

            itemView.ib_add_to_favorites.setOnClickListener {
                listener2(favPlace!!, adapterPosition)
            }
        }
    }
}