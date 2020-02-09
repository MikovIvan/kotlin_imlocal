package ru.imlocal.ui.feedback

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_feedback.*
import ru.imlocal.R
import ru.imlocal.extensions.hideKeyboard
import ru.imlocal.extensions.isEmail

class FragmentFeedback : Fragment() {

    companion object {
        fun newInstance() = FragmentFeedback()
    }

    private lateinit var viewModel: FeedbackViewModel

    override fun onPause() {
        super.onPause()
        view?.hideKeyboard()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FeedbackViewModel::class.java)

        et_feedback_message.imeOptions = EditorInfo.IME_ACTION_DONE
        et_feedback_message.setRawInputType(InputType.TYPE_CLASS_TEXT)

        et_feedback_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) textInputLayout_email.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmail()) {
                    textInputLayout_email.error = null
                } else {
                    textInputLayout_email.error = "Неправильно указан email"
                }
            }

        })

        btn_send_feedback.setOnClickListener {
            sendEmail(et_feedback_title, et_feedback_email, et_feedback_message)
        }
    }

    private fun sendEmail(
        etFeedbackTitle: TextInputEditText,
        etFeedbackEmail: TextInputEditText,
        etFeedbackMessage: TextInputEditText
    ) {
        val Email = Intent(Intent.ACTION_SEND)
        Email.type = "text/email"
        Email.putExtra(Intent.EXTRA_EMAIL, arrayOf(activity!!.resources.getString(R.string.feedback_email)))
        Email.putExtra(Intent.EXTRA_SUBJECT, etFeedbackTitle.text)
        Email.putExtra(Intent.EXTRA_TEXT, etFeedbackMessage.text)
        startActivity(Intent.createChooser(Email, "Send Feedback:"))
    }
}
