package com.example.smart_quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.smart_quiz.databinding.ActivityEditBinding
import com.example.smart_quiz.model.Quiz
import com.example.smart_quiz.ui.edit.CreateDialogFragment
import com.google.android.material.textfield.TextInputEditText

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private val createQuizList: MutableList<Quiz> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btTest.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val createDialog = CreateDialogFragment()
        createDialog.dialogClickListener = object : CreateDialogFragment.OnDialogClickListener {
            override fun onDialogClick(view: View?) {
                val question = view!!.findViewById<TextInputEditText>(R.id.ed_question)
                val correct = view.findViewById<TextInputEditText>(R.id.ed_correct)
                val choice1 = view.findViewById<TextInputEditText>(R.id.ed_choice1)
                val choice2 = view.findViewById<TextInputEditText>(R.id.ed_choice2)
                val choice3 = view.findViewById<TextInputEditText>(R.id.ed_choice3)

                createQuizList.add(
                    Quiz(
                        sentence = question.text.toString(),
                        correct = correct.text.toString(),
                        choice1 = choice1.text.toString(),
                        choice2 = choice2.text.toString(),
                        choice3 = choice3.text.toString()
                    )
                )

                Log.d("EditActivity", "QuizList => ${createQuizList}")

            }
        }

        createDialog.show(supportFragmentManager, "create_dialog")

    }
}