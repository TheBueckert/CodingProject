package com.clyde.codingchallenge.Activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import com.clyde.codingchallenge.R
import kotlinx.android.synthetic.main.no_internet_dialog.view.*
import java.lang.ClassCastException

// this dialog will display when internet is not available
class NoInternetDialog : AppCompatDialogFragment( ){
    private lateinit var listener:noInternetDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater = activity?.layoutInflater ?: LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.no_internet_dialog, null)

        builder.setView(view)
        // clicking the accept button will start a local realm search
        view.accept_btn.setOnClickListener{
            listener.confirmOrDeny(false)
            dialog?.dismiss()
        }
        // clicking the try again button will attempt to search with the api again
        view.try_again_btn.setOnClickListener {
            listener.confirmOrDeny(true)
            dialog?.dismiss()
        }
        return builder.create()
    }

    // the listener must be implemented when creating a dialog
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as noInternetDialogListener
        } catch (e:ClassCastException) {
            throw ClassCastException(context.toString() + "must implement noInternetDialogListener")
        }
    }
    //this listener will be implemented in the calling activity, to listen for choices
    interface noInternetDialogListener{
        fun confirmOrDeny(boolean: Boolean)
    }
}