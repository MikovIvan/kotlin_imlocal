package ru.imlocal.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.favorites_action.view.*
import kotlinx.android.synthetic.main.favorites_place.view.ib_add_to_favorites
import ru.imlocal.R
import ru.imlocal.data.api.ACTION_IMAGE_DIRECTION
import ru.imlocal.data.api.BASE_IMAGE_URL
import ru.imlocal.models.Action
import kotlin.math.min

class RecyclerViewAdapterFavActions(
    private val favActions: List<Action>,
    var fullShow: Boolean = false,
    private val listener: (Action) -> Unit,
    private val listener2: (Action, position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapterFavActions.FavActionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavActionsViewHolder {
        return FavActionsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorites_action, parent, false))
    }

    override fun getItemCount(): Int = if (fullShow) favActions.size else min(favActions.size, 2)

    override fun onBindViewHolder(holder: FavActionsViewHolder, position: Int) {
        holder.bind(favActions[position], listener, listener2)
    }

    fun showAll(fact: Boolean) {
        fullShow = fact
        notifyDataSetChanged()
    }

    inner class FavActionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(favAction: Action?, listener: (Action) -> Unit, listener2: (Action, position: Int) -> Unit) {
            itemView.tv_shop_title.text = favAction?.place?.shopShortName
            itemView.tv_fav_action_title.text = favAction?.title

            Glide.with(itemView.context)
                .load(BASE_IMAGE_URL + ACTION_IMAGE_DIRECTION + favAction?.actionPhotos?.get(0)?.actionPhoto)
                .into(itemView.iv_fav_action_icon)

            itemView.setOnClickListener {
                listener(favAction!!)
            }

            itemView.ib_add_to_favorites.setOnClickListener {
                listener2(favAction!!, adapterPosition)
            }
        }
    }
}