package com.example.smart_quiz.ui.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smart_quiz.R
import com.example.smart_quiz.databinding.FragmentQuizSelectBinding
import com.example.smart_quiz.model.Detail

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuizSelectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizSelectFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentQuizSelectBinding? = null


    private val binding get() = _binding!!

    private val sampleList: MutableList<Detail> = mutableListOf(
        Detail(title = "samplequiz1", LikeNum = 3, q_id = "01"),
        Detail(title = "samplequiz2", LikeNum =4 , q_id = "02"),
        Detail(title = "samplequiz4", LikeNum = 7, q_id = "03"),
        Detail(title = "samplequiz5", LikeNum = 2, q_id = "04")
    )

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
        _binding = FragmentQuizSelectBinding.inflate(inflater, container, false)

        val field_id = arguments?.getString("id")

        val recyclerView = binding.selectRecyclerView
        recyclerView.let {
            it.adapter = SelectAdapter(sampleList)
            it.layoutManager = LinearLayoutManager(view?.context)
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
         * @return A new instance of fragment QuizSelectFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuizSelectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}