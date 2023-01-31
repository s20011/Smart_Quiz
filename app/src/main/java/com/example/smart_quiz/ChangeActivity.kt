package com.example.smart_quiz

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.adapter.AddQuestionAdapter
import com.example.smart_quiz.adapter.QuizEditAdapter
import com.example.smart_quiz.databinding.ActivityChangeBinding
import com.example.smart_quiz.model.Field
import com.example.smart_quiz.model.Quiz
import com.example.smart_quiz.ui.edit.CreateDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChangeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerview: RecyclerView
    private lateinit var addRecyclerview: RecyclerView
    private val fieldList: MutableList<Field> = mutableListOf(
        Field(name = "IT", id = "field-it"),
        Field(name = "動物", id = "field-animal"),
        Field(name = "歴史", id = "field-history")
    )
    private var position: Int? = null //ユーザーが選択したプルダウンメニューのposition
    private val spinnerItems = arrayListOf<String>()
    private val createQuizList: MutableList<Quiz> = mutableListOf()
    private lateinit var d_id: String
    private lateinit var q_id: String
    private val newQuizList = mutableListOf<Quiz>()
    private val updateQuizList = mutableListOf<Quiz>()
    private val keyList = mutableListOf<String>()
    private val updateQuizKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        d_id = intent.getStringExtra("d_id").toString()
        q_id = intent.getStringExtra("q_id").toString()
        val title = intent.getStringExtra("title")
        val field = intent.getStringExtra("field")

        val editTile = binding.changeEdTitle
        editTile.setText(title, TextView.BufferType.NORMAL)
        //setContentView(editTile)

        createQuizList()

        val toolbar = binding.changeToolbar
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.nav_edit_title)

        //Firebase Authの初期化
        auth = Firebase.auth

        fieldList.forEach { spinnerItems.add(it.name) }

        //下のrecyclerview....
        recyclerview = binding.changeCreatedRecyclerview
        val quizAdapter = QuizEditAdapter(createQuizList)
        quizAdapter.itemClickListener = object : QuizEditAdapter.OnItemClickListener {
            override fun onItemClick(holder: QuizEditAdapter.ViewHolder) {
                val position = holder.absoluteAdapterPosition
                reShowDialog(createQuizList[position], position)
            }
        }
        recyclerview.let {
            it.layoutManager = LinearLayoutManager(this@ChangeActivity)
            it.adapter = quizAdapter
            it.itemAnimator?.changeDuration = 0
        }
        //.....

        //上のrecyclerview...
        addRecyclerview = binding.rvAddQuestion
        val addQuizAdapter = AddQuestionAdapter(newQuizList)
        addQuizAdapter.itemClickListener = object : AddQuestionAdapter.OnItemClickListener {
            override fun onItemClick(holder: AddQuestionAdapter.ViewHolder) {
                val position = holder.absoluteAdapterPosition
                rvAddReShowDialog(newQuizList[position], position)
            }
        }
        addRecyclerview.let {
            it.layoutManager = LinearLayoutManager(this@ChangeActivity)
            it.adapter = addQuizAdapter
            it.itemAnimator?.changeDuration = 0
        }

        //......

        val spinner = binding.changeSpField
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
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.btCancelChange.setOnClickListener {
            val builder = AlertDialog.Builder(this@ChangeActivity)
            builder.setTitle("キャンセル")
                .setMessage("変更をやめますか")
                .setPositiveButton(R.string.cancel) { _, _ ->
                    finish()
                }
            val alertDialog = builder.create()
            alertDialog.show()
        }

        binding.btChange.setOnClickListener {
            Snackbar.make(it, "クイズの内容を変更しました", Snackbar.LENGTH_LONG).show()
            if(updateQuizKeyList.size != 0){
                updateQuiz()
            }
            if(newQuizList.size != 0){
                newPostQuiz()
            }
        }

        binding.btQuizAddChange.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog(){
        val createDialog = CreateDialogFragment()
        val args = Bundle()
        args.putBoolean("dis", false)
        createDialog.dialogClickListener = object : CreateDialogFragment.OnDialogClickListener {
            override fun onDialogClick(view: View?) {
                val question = view!!.findViewById<TextInputEditText>(R.id.ed_question)
                val correct = view.findViewById<TextInputEditText>(R.id.ed_correct)
                val choice1 = view.findViewById<TextInputEditText>(R.id.ed_choice1)
                val choice2 = view.findViewById<TextInputEditText>(R.id.ed_choice2)
                val choice3 = view.findViewById<TextInputEditText>(R.id.ed_choice3)

                val newQuiz: Quiz = Quiz(
                    sentence = question.text.toString(),
                    correct = correct.text.toString(),
                    choice1 = choice1.text.toString(),
                    choice2 = choice2.text.toString(),
                    choice3 = choice3.text.toString()
                )

                Log.d("ChangeActivity", "Add newQuizList")
                newQuizList.add(newQuiz)

                addRecyclerview.adapter?.notifyDataSetChanged()

                Log.d("EditActivity", "QuizList => ${createQuizList}")

            }
        }


        createDialog.show(supportFragmentManager, "create_dialog")

    }

    private fun reShowDialog(item: Quiz, position: Int){
        val createDialog = CreateDialogFragment()
        val args = Bundle()
        args.putBoolean("dis", true)
        args.let {
            it.putString("choice1", item.choice1)
            it.putString("choice2", item.choice2)
            it.putString("choice3", item.choice3)
            it.putString("correct", item.correct)
            it.putString("sentence", item.sentence)
        }
        createDialog.arguments = args

        createDialog.dialogClickListener = object : CreateDialogFragment.OnDialogClickListener {
            override fun onDialogClick(view: View?) {
                val question = view!!.findViewById<TextInputEditText>(R.id.ed_question)
                val correct = view.findViewById<TextInputEditText>(R.id.ed_correct)
                val choice1 = view.findViewById<TextInputEditText>(R.id.ed_choice1)
                val choice2 = view.findViewById<TextInputEditText>(R.id.ed_choice2)
                val choice3 = view.findViewById<TextInputEditText>(R.id.ed_choice3)

                createQuizList[position] = Quiz(
                    sentence = question.text.toString(),
                    choice1 = choice1.text.toString(),
                    choice2 = choice2.text.toString(),
                    choice3 = choice3.text.toString(),
                    correct = correct.text.toString()
                )
                recyclerview.adapter?.notifyDataSetChanged()

                updateQuizList.add(createQuizList[position])
                Log.d("ChangeActivity", "updateQuiz => ${updateQuizList[position]}")
                updateQuizKeyList.add(keyList[position])
                Log.d("ChangeActivity", "updateKey => ${updateQuizKeyList[position]}")
            }
        }
        createDialog.show(supportFragmentManager, "create_dialog")
    }

    private fun rvAddReShowDialog(item: Quiz, position: Int) {
        val createDialog = CreateDialogFragment()
        val args = Bundle()
        args.putBoolean("dis", true)
        args.let {
            it.putString("choice1", item.choice1)
            it.putString("choice2", item.choice2)
            it.putString("choice3", item.choice3)
            it.putString("correct", item.correct)
            it.putString("sentence", item.sentence)
        }
        createDialog.arguments = args

        createDialog.dialogClickListener = object : CreateDialogFragment.OnDialogClickListener {
            override fun onDialogClick(view: View?) {
                val question = view!!.findViewById<TextInputEditText>(R.id.ed_question)
                val correct = view.findViewById<TextInputEditText>(R.id.ed_correct)
                val choice1 = view.findViewById<TextInputEditText>(R.id.ed_choice1)
                val choice2 = view.findViewById<TextInputEditText>(R.id.ed_choice2)
                val choice3 = view.findViewById<TextInputEditText>(R.id.ed_choice3)

                newQuizList[position] = Quiz(
                    sentence = question.text.toString(),
                    choice1 = choice1.text.toString(),
                    choice2 = choice2.text.toString(),
                    choice3 = choice3.text.toString(),
                    correct = correct.text.toString()
                )
                addRecyclerview.adapter?.notifyDataSetChanged()
            }
        }
        createDialog.show(supportFragmentManager, "create_dialog")
    }

    private fun updateQuiz() {
        val refQuiz = Firebase.database.getReference("question")
        val updatePoint = hashMapOf<String, Any>()
        for(position in 0..updateQuizKeyList.size - 1){
            updatePoint[updateQuizKeyList[position]] = updateQuizList[position]
            refQuiz.child(q_id).updateChildren(updatePoint)
        }
    }

    private fun createQuizList() {
        val refQuiz = Firebase.database.getReference("question/$q_id")

        refQuiz.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    val item = data.getValue(Quiz::class.java)
                    keyList.add(data.key.toString())

                    createQuizList.add(
                        Quiz(
                            choice1 = item!!.choice1,
                            choice2 = item.choice2,
                            choice3 = item.choice3,
                            correct = item.correct,
                            sentence = item.sentence
                        )
                    )
                }

                recyclerview.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun newPostQuiz() {
        Log.d("ChangeActivity", "Start newPostQuiz")
        val refQuestion = Firebase.database.getReference("question")
        newQuizList.forEach { refQuestion.child(q_id).push().setValue(it) }
    }
}