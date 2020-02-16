package ru.imlocal.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import ru.imlocal.R
import ru.imlocal.data.repository.UserRepository
import ru.imlocal.ui.MainActivity.Companion.enter
import ru.imlocal.ui.MainActivity.Companion.favorites
import ru.imlocal.ui.MainActivity.Companion.logout
import ru.imlocal.utils.ActivityNavigation

class FragmentLogin : Fragment(), ActivityNavigation {

    companion object {
        fun newInstance() = FragmentLogin()
    }

    private val viewModel: LoginViewModel by activityViewModels { LoginViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.startActivityForResultEvent.setEventReceiver(this, this)

        viewModel.uiState.observe(this, Observer {
            val uiModel = it

            if (uiModel.showError != null && !uiModel.showError.consumed) {
                uiModel.showError.consume()?.let {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }

            if (uiModel.showSuccess != null && !uiModel.showSuccess.consumed) {
                uiModel.showSuccess.consume()?.let {
                    enter.title = UserRepository.userLogin.username
                    favorites.isVisible = true
                    logout.isVisible = true
//                    nav_host.findNavController().popBackStack()
                    nav_host.findNavController().popBackStack(R.id.fragment_login, true)
                }
            }
        })

        btn_login_vk.setOnClickListener { viewModel.loginVK(activity!!) }
        btn_login_fb.setOnClickListener { viewModel.loginFB() }
        btn_login_google.setOnClickListener { viewModel.loginGoogle() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onResultFromActivity(context!!, requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

//    private fun getViewModel(): LoginViewModel {
//        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                @Suppress("UNCHECKED_CAST")
//                return LoginViewModel(userRepository) as T
//            }
//        })[LoginViewModel::class.java]
//    }

    class LoginViewModelFactory : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel() as T
        }
    }
}
