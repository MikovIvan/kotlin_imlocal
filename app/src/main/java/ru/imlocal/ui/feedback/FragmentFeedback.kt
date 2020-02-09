package ru.imlocal.ui.feedback

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_feedback.*
import ru.imlocal.R

class FragmentFeedback : Fragment() {

    companion object {
        fun newInstance() = FragmentFeedback()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        et_feedback_message.imeOptions = EditorInfo.IME_ACTION_DONE
        et_feedback_message.setRawInputType(InputType.TYPE_CLASS_TEXT)

        btn_send_feedback.setOnClickListener {
            sendEmail(et_feedback_title, et_feedback_message)
        }
    }

    private fun sendEmail(
        etFeedbackTitle: TextInputEditText,
        etFeedbackMessage: TextInputEditText
    ) {
        val email = Intent(Intent.ACTION_SEND)
        email.type = "text/email"
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(activity!!.resources.getString(R.string.feedback_email)))
        email.putExtra(Intent.EXTRA_SUBJECT, etFeedbackTitle.text.toString())
        email.putExtra(Intent.EXTRA_TEXT, etFeedbackMessage.text.toString())
        startActivity(Intent.createChooser(email, "Send Feedback:"))
    }
}
