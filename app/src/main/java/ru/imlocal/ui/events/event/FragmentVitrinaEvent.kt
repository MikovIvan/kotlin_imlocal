package ru.imlocal.ui.events.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.imlocal.R

class FragmentVitrinaEvent : Fragment() {

    companion object {
        fun newInstance() = FragmentVitrinaEvent()
    }

    private lateinit var viewModel: VitrinaEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vitrina_event, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VitrinaEventViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
