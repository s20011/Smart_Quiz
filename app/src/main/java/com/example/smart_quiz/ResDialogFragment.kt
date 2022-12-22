package com.example.smart_quiz

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.smart_quiz.ui.edit.CreateDialogFragment

class ResDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        val resDialog = inflater?.inflate(R.layout.res_dialog, null)

        val builder = AlertDialog.Builder(activity)
        builder.setView(resDialog)
            .setPositiveButton("aaa") { _, _ ->
                dialogClickListener?.onDialogClick(resDialog)
            }

        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        return alertDialog
    }

    var dialogClickListener:OnDialogClickListener? = null
    interface OnDialogClickListener {
        fun onDialogClick(view: View?)
    }

}