package ru.imlocal.ui.add.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.imlocal.R

class FragmentAddAction : Fragment() {

    companion object {
        fun newInstance() = FragmentAddAction()
    }

    private lateinit var viewModel: AddActionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_action, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddActionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
