package com.example.smart_quiz.ui.edit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_quiz.EditActivity
import com.example.smart_quiz.R
import com.example.smart_quiz.adapter.SelectAdapter
import com.example.smart_quiz.databinding.FragmentEditBinding
import com.example.smart_quiz.model.Detail
import com.google.android.material.snackbar.Snackbar

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

    //sample
    private val sampleList: MutableList<Detail> = mutableListOf(
        Detail(title = "samplequiz1", LikeNum = 3, q_id = "01"),
        Detail(title = "samplequiz2", LikeNum =4 , q_id = "02"),
        Detail(title = "samplequiz4", LikeNum = 7, q_id = "03"),
        Detail(title = "samplequiz5", LikeNum = 2, q_id = "04")
    )

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditBinding.inflate(inflater, container, false)

        recyclerView = binding.editSelectRecyclerview
        selectAdapter = SelectAdapter(sampleList)
        //recyclerviewのクリック処理
        selectAdapter.itemClickListener = object :SelectAdapter.OnItemClickListener{
            override fun onItemClick(holder: SelectAdapter.ViewHolder) {
                val position = holder.absoluteAdapterPosition
                Snackbar.make(requireView(), "onClick position $position", Snackbar.LENGTH_SHORT)
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