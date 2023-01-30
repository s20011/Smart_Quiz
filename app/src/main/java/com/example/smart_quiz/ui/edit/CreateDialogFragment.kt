package com.example.smart_quiz.ui.edit

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.smart_quiz.R
import com.google.android.material.textfield.TextInputEditText

class CreateDialogFragment:DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        val createDialog = inflater?.inflate(R.layout.create_dialog, null)

        val dis = arguments?.getBoolean("dis")
        Log.d("CreateDialogFragment", "dis = $dis")
        if(dis == true){
            val choice1 = arguments?.getString("choice1")
            val choice2 = arguments?.getString("choice2")
            val choice3 = arguments?.getString("choice3")
            val correct = arguments?.getString("correct")
            val sentence = arguments?.getString("sentence")

            val question = createDialog!!.findViewById<TextInputEditText>(R.id.ed_question)
            val edCorrect = createDialog.findViewById<TextInputEditText>(R.id.ed_correct)
            val edChoice1 = createDialog.findViewById<TextInputEditText>(R.id.ed_choice1)
            val edChoice2 = createDialog.findViewById<TextInputEditText>(R.id.ed_choice2)
            val edChoice3 = createDialog.findViewById<TextInputEditText>(R.id.ed_choice3)

            Log.d("CreateDialogFragment", "sentence = $sentence")
            question.setText(sentence, TextView.BufferType.NORMAL)
            edCorrect.setText(correct, TextView.BufferType.NORMAL)
            edChoice1.setText(choice1, TextView.BufferType.NORMAL)
            edChoice2.setText(choice2, TextView.BufferType.NORMAL)
            edChoice3.setText(choice3, TextView.BufferType.NORMAL)

        }

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