package com.clyde.codingchallenge.Activities

import android.app.AlertDialog
import android.app.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import com.clyde.codingchallenge.R
import kotlinx.android.synthetic.main.no_internet_dialog.view.accept_btn

// this dialog will display when the server returns an error
class ServerBusyDialog : AppCompatDialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater = activity?.layoutInflater ?: LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.server_busy_dialog, null)

        builder.setView(view)
        view.accept_btn.setOnClickListener{
            dialog?.dismiss()
        }
        return builder.create()
    }
}