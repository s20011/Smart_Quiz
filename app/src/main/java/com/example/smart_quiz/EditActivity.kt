package com.example.smart_quiz

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.adapter.ChoiceAdapter
import com.example.smart_quiz.adapter.QuizEditAdapter
import com.example.smart_quiz.databinding.ActivityEditBinding
import com.example.smart_quiz.model.*
import com.example.smart_quiz.ui.edit.CreateDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import org.jetbrains.annotations.NotNull

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    private lateinit var auth: FirebaseAuth
    private val quizTitleList: MutableList<String> = mutableListOf("何もありません")
    private var initial = false
    private var quizListInitial = false
    private lateinit var recyclerview: RecyclerView
    private val fieldList: MutableList<Field> = mutableListOf(
        Field(name = "IT", id = "field-it"),
        Field(name = "動物", id = "field-animal"),
        Field(name = "歴史", id = "field-history")
    )
    private var position: Int? = null //ユーザーが選択したプルダウンメニューのposition
    private var checker = false //ユーザーが項目を記入しているか
    private val spinnerItems = arrayListOf<String>()
    private val createQuizList: MutableList<Quiz> = mutableListOf(
        Quiz(
            choice1 = "initial",
            choice2 = "initial",
            choice3 = "initial",
            sentence = "何もありません"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.editToolbar
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.nav_edit_title)

        //Firebase Authの初期化
        auth = Firebase.auth

        fieldList.forEach { spinnerItems.add(it.name) }

        //recyclerviewの初期化
        recyclerview = binding.createdRecyclerview
        val quizAdapter = QuizEditAdapter(createQuizList)
        recyclerview.let {
            it.layoutManager = LinearLayoutManager(this@EditActivity)
            it.adapter = quizAdapter
            it.itemAnimator?.changeDuration = 0
        }

        //スピナーの初期化
        val spinner = binding.spField
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            spinnerItems
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, spposition: Int, p3: Long){
                val spinnerParent = parent as Spinner
                position = spposition
                checker = true
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                checker = false
            }
        }

        binding.btQuizAdd.setOnClickListener {
            showDialog()
        }

        binding.btCancel.setOnClickListener {
            val builder = AlertDialog.Builder(this@EditActivity)
            builder.setTitle("キャンセル")
                .setMessage("作成をやめますか")
                .setPositiveButton(R.string.cancel) { _, _ ->
                    finish()
                }
            val alertDialog = builder.create()
            alertDialog.show()
        }

        binding.btCreation.setOnClickListener {
            createQuiz()
            Snackbar.make(it, "新しくクイズを作成しました", Snackbar.LENGTH_LONG).show()
            finish()
        }


    }

    //制作画面のダイアログ表示
    private fun showDialog(){
        val createDialog = CreateDialogFragment()
        createDialog.dialogClickListener = object : CreateDialogFragment.OnDialogClickListener {
            override fun onDialogClick(view: View?) {
                val question = view!!.findViewById<TextInputEditText>(R.id.ed_question)
                val correct = view.findViewById<TextInputEditText>(R.id.ed_correct)
                val choice1 = view.findViewById<TextInputEditText>(R.id.ed_choice1)
                val choice2 = view.findViewById<TextInputEditText>(R.id.ed_choice2)
                val choice3 = view.findViewById<TextInputEditText>(R.id.ed_choice3)

                if(!quizListInitial){
                    createQuizList.clear()
                    quizListInitial = true
                }

                createQuizList.add(
                    Quiz(
                        sentence = question.text.toString(),
                        correct = correct.text.toString(),
                        choice1 = choice1.text.toString(),
                        choice2 = choice2.text.toString(),
                        choice3 = choice3.text.toString()
                    )
                )

                if(!initial){
                    quizTitleList.clear()
                    initial = true
                }

                quizTitleList.add(question.text.toString())
                recyclerview.adapter?.notifyDataSetChanged()

                Log.d("EditActivity", "QuizList => ${createQuizList}")

            }
        }


        createDialog.show(supportFragmentManager, "create_dialog")

    }

    //RealtimeDatabaseへの書き込み
    private fun createQuiz() {
        //Firebaseのインスタンス取得
        val database = FirebaseDatabase.getInstance()
        //questionを参照
        val refQuestion = database.getReference("question")
        //一意のキーを取得
        val newPostRef = refQuestion.push()
        //RealtimeDatabase -> questionへの書き込み
        createQuizList.forEach { newPostRef.push().setValue(it) }

        val user = auth.currentUser

        //usersのインスタンス参照
        val refUser = database.getReference("users/${user!!.uid}")

        //新しく作成したquestionのkey取得
        val key = newPostRef.key


        //Detailを分野別に参照
        val refDetail = database.getReference("Details/${fieldList[position!!].id}")
        //RealtimeDatabase -> Detailへの書き込み
        val newPostDetails = refDetail.push()
        //新しく作成したDetailsのkeyを取得
        val detailsKey = newPostDetails.key.toString()
        newPostDetails.setValue(
            CreateDetail(
                likeNum = 0,
                q_id = key.toString(),
                title = binding.edTitle.text.toString(),
                author = user!!.uid
            )
        )

        refUser.child("make-record").push()
            .setValue(
                makeRecord(
                    d_id = detailsKey,
                    field = fieldList[position!!].id
                )
            )

    }
}