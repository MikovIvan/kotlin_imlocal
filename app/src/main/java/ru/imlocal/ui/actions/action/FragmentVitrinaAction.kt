package ru.imlocal.ui.actions.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.imlocal.R

class FragmentVitrinaAction : Fragment() {

    companion object {
        fun newInstance() = FragmentVitrinaAction()
    }

    private lateinit var viewModel: VitrinaActionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vitrina_action, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VitrinaActionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
