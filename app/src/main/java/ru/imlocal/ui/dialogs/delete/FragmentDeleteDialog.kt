package ru.imlocal.ui.dialogs.delete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.imlocal.R

class FragmentDeleteDialog : Fragment() {

    companion object {
        fun newInstance() = FragmentDeleteDialog()
    }

    private lateinit var viewModel: DeleteDialogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delete_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DeleteDialogViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
