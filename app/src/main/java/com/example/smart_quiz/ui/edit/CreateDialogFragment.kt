package com.example.smart_quiz.ui.edit

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.smart_quiz.R

class CreateDialogFragment:DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        val createDialog = inflater?.inflate(R.layout.create_dialog, null)

        val builder = AlertDialog.Builder(activity)
        builder.setView(createDialog)
            .setPositiveButton("OK") {_, _ ->
                dialogClickListener?.onDialogClick(createDialog)
            }

        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        return alertDialog
    }

    var dialogClickListener: OnDialogClickListener? = null
    interface OnDialogClickListener {
        fun onDialogClick(view: View?)
    }
}