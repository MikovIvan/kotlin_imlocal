package ru.imlocal.ui.actions.action

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_vitrina_action.*
import ru.imlocal.R
import ru.imlocal.data.api.ACTION_IMAGE_DIRECTION
import ru.imlocal.data.api.Api
import ru.imlocal.data.api.BASE_IMAGE_URL
import ru.imlocal.data.api.SHOP_IMAGE_DIRECTION
import ru.imlocal.data.repository.ActionRepository
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.models.Action

class FragmentVitrinaAction : Fragment() {

    private lateinit var viewModel: VitrinaActionViewModel
    private lateinit var actionRepository: ActionRepository
    private val args: FragmentVitrinaActionArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vitrina_action, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService: Api = Api.getClient()
        actionRepository = ActionRepository(apiService)

        viewModel = getViewModel(args.actId)

        viewModel.actionDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_vitrina_action.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_vitrina_action.visibility =
                if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(action: Action) {
        Glide.with(context as Context)
            .load(BASE_IMAGE_URL + SHOP_IMAGE_DIRECTION + action.place.placePhotos.get(0).shopPhoto)
            .into(iv_icon_vitrina)
        tv_shop_title_vitrina.text = action.place.shopShortName
        tv_shop_adress_vitrina.text = action.place.shopAddress.toString()

        tv_action_title_vitrina.text = action.title
        tv_action_description_vitrina.text = action.fullDesc
        tv_date_vitrina.text = action.begin + " - " + action.end

        for (actionPhoto in action.actionPhotos) {
            when (action.actionPhotos.size) {
                1 -> flipperImages(actionPhoto.actionPhoto, false)
                in 2..Int.MAX_VALUE -> flipperImages(actionPhoto.actionPhoto, true)
            }
        }
    }

    fun flipperImages(photo: String, autostart: Boolean) {
        val imageView = ImageView(activity)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(this)
            .load(BASE_IMAGE_URL + ACTION_IMAGE_DIRECTION + photo)
            .into(imageView)
        flipper_vitrina_action.addView(imageView)
        flipper_vitrina_action.flipInterval = 2000
        flipper_vitrina_action.setInAnimation(activity, android.R.anim.slide_in_left)
        flipper_vitrina_action.setOutAnimation(activity, android.R.anim.slide_out_right)
        if (autostart) {
            flipper_vitrina_action.startFlipping()
        }
    }

    private fun getViewModel(actionId: Int): VitrinaActionViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return VitrinaActionViewModel(
                    actionRepository,
                    actionId
                ) as T
            }
        })[VitrinaActionViewModel::class.java]
    }
}
