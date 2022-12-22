package com.example.smart_quiz

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class ResDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        val resDialog = inflater?.inflate(R.layout.res_dialog, null)

        val message = arguments?.getInt("message")
        val btText = arguments?.getString("btText")
        val userSelect = arguments?.getString("userSelect")
        val resCorrect = arguments?.getString("resCorrect")

        //クイズの答え
        val txCorrect = resDialog!!.findViewById<TextView>(R.id.tx_question_correct)
        txCorrect.text = resCorrect

        //Userの選んだ答え
        val txUserSelect = resDialog!!.findViewById<TextView>(R.id.tx_user_select)
        txUserSelect.text = userSelect

        val resImage = resDialog!!.findViewById<ImageView>(R.id.image_res)
        if(message == 1) {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.correct)
            resImage.setImageBitmap(bitmap)
        }else {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.not_correct)
            resImage.setImageBitmap(bitmap)
        }

        val builder = AlertDialog.Builder(activity)
        builder.setView(resDialog)
            .setPositiveButton(btText) { _, _ ->
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