package com.example.smart_quiz.ui.edit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.EditActivity
import com.example.smart_quiz.R
import com.example.smart_quiz.adapter.SelectAdapter
import com.example.smart_quiz.databinding.FragmentEditBinding
import com.example.smart_quiz.model.Detail
import com.example.smart_quiz.model.makeRecord
import com.example.smart_quiz.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentEditBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var selectAdapter: SelectAdapter
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var auth: FirebaseAuth

    //sample
    private val sampleList: MutableList<Detail> = mutableListOf(
        Detail(title = "samplequiz1", likeNum = 3, q_id = "01"),
        Detail(title = "samplequiz2", likeNum =4 , q_id = "02"),
        Detail(title = "samplequiz4", likeNum = 7, q_id = "03"),
        Detail(title = "samplequiz5", likeNum = 2, q_id = "04")
    )

    private val detailsList: MutableList<Detail> = mutableListOf()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //Firebase Authの初期化
        auth = Firebase.auth

        createList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            mainViewModel.reselectedItemOnRoot
                .filter { it.isEdit() }
                .collect {
                    binding.editSelectRecyclerview.smoothScrollToPosition(0)
                }
        }

        recyclerView = binding.editSelectRecyclerview
        selectAdapter = SelectAdapter(detailsList)
        //recyclerviewのクリック処理
        selectAdapter.itemClickListener = object :SelectAdapter.OnItemClickListener{
            override fun onItemClick(holder: SelectAdapter.ViewHolder) {
                val position = holder.absoluteAdapterPosition
                Snackbar.make(
                    requireView(), "onClick position $position", Snackbar.LENGTH_SHORT
                ).show()

                Log.d("EditFragment", "onClick")
            }
        }

        recyclerView.let {
            it.adapter = selectAdapter
            it.layoutManager = LinearLayoutManager(view?.context)
            it.itemAnimator?.changeDuration = 0
        }

        binding.fabAdd.setOnClickListener{
            Log.d("EditFragment", "onClick fab")
            val intent = Intent(context, EditActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun createList() {
        //userIdの参照
        val uid = auth.currentUser!!.uid
        val refUser = Firebase.database.getReference("users/$uid")
        val refDetail = Firebase.database.getReference("Details")

        refUser.child("make-record")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(data in snapshot.children){
                        val field = data.child("field").getValue(String::class.java)
                        val d_id = data.child("d_id").getValue(String::class.java)
                        refDetail.child(field!!).child(d_id!!)
                            .addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val likeNum = snapshot.child("likeNum")
                                        .getValue(Int::class.java)
                                    val title = snapshot.child("title")
                                        .getValue(String::class.java)
                                    val q_id = snapshot.child("q_id")
                                        .getValue(String::class.java)

                                    detailsList.add(
                                        Detail(
                                            title = title!!,
                                            likeNum = likeNum!!,
                                            q_id = q_id!!
                                        )
                                    )

                                    binding.editSelectRecyclerview.adapter?.notifyDataSetChanged()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}