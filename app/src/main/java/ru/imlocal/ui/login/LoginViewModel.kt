package ru.imlocal.ui.login

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vk.sdk.*
import com.vk.sdk.api.*
import com.vk.sdk.api.VKRequest.VKRequestListener
import com.vk.sdk.api.model.VKApiUserFull
import com.vk.sdk.api.model.VKList
import ru.imlocal.R
import ru.imlocal.data.repository.UserRepository
import ru.imlocal.utils.ActivityNavigation
import ru.imlocal.utils.Event
import ru.imlocal.utils.LiveMessageEvent

class LoginViewModel : ViewModel() {
    private val scope = arrayOf(VKScope.PHOTOS, VKScope.EMAIL)
    val startActivityForResultEvent = LiveMessageEvent<ActivityNavigation>()

    private val _uiState = MutableLiveData<LoginUiModel>()
    val uiState: LiveData<LoginUiModel>
        get() = _uiState

    fun loginFB() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun loginGoogle() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun loginVK(fragment: FragmentActivity) {
        VKSdk.login(fragment, *scope)
    }

    fun onResultFromActivity(context: Context, requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            VKServiceActivity.VKServiceType.Authorization.outerCode -> {
                VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                    override fun onResult(res: VKAccessToken) {
                        Log.d("VK", "VKTOKEN " + res.accessToken)
                        val request =
                            VKApi.users()[VKParameters.from(VKApiConst.FIELDS, "photo_200, contacts, bdate, mobile_phone")]
                        request.executeWithListener(object : VKRequestListener() {
                            override fun onComplete(response: VKResponse) {
                                val userVK = (response.parsedModel as VKList<VKApiUserFull>)[0]
                                UserRepository.saveUser(
                                    userVK.id.toString(),
                                    res.email,
                                    userVK.first_name,
                                    userVK.last_name,
                                    "vkontakte",
                                    res.accessToken
                                )

                                emitUiState(
                                    showSuccess = Event(R.string.login_successful)
                                )

                                Log.d(
                                    "VK", userVK.first_name + " " + userVK.last_name + " " + userVK.bdate + " " + userVK.id
                                            + " " + userVK.mobile_phone + " " + userVK.id + " " + userVK.photo_200
                                )
                            }
                        })
                    }

                    override fun onError(error: VKError) {
                        Toast.makeText(VKUIHelper.getApplicationContext(), "ошибка входа vk", Toast.LENGTH_LONG).show()
                        emitUiState(
                            showError = Event(R.string.login_failed)
                        )
                    }
                })
            }

//            GOOGLE_SIGN_IN -> {
//                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//                googleSignInComplete(task)
//
//            }

            else -> {
                emitUiState(
                    showError = Event(R.string.login_failed)
                )
            }
        }
    }

    private fun emitUiState(
        showSuccess: Event<Int>? = null,
        showError: Event<Int>? = null
    ) {
        val uiModel = LoginUiModel(showSuccess, showError)
        _uiState.value = uiModel
    }
}

data class LoginUiModel(
    val showSuccess: Event<Int>?,
    val showError: Event<Int>?
)
