package com.example.smart_quiz.ui.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smart_quiz.R
import com.example.smart_quiz.adapter.FieldAdapter
import com.example.smart_quiz.databinding.FragmentQuizFieldBinding
import com.example.smart_quiz.model.Field
import com.example.smart_quiz.viewmodel.MainViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuizFieldFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizFieldFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentQuizFieldBinding? = null
    private val mainViewModel by activityViewModels<MainViewModel>()

    private val binding get() = _binding!!

    private val fieldList: MutableList<Field> = mutableListOf(
        Field(name = "IT", id = "field-it"),
        Field(name = "動物", id = "field-animal"),
        Field(name = "歴史", id = "field-history")
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
        _binding = FragmentQuizFieldBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            mainViewModel.reselectedItemOnRoot
                .filter { it.isQuiz() }
                .collect {
                    binding.fieldRecyclerview.smoothScrollToPosition(0)
                }
        }
        val recyclerview = binding.fieldRecyclerview
        val adapter = FieldAdapter(fieldList)
        adapter.itemClickListener = object : FieldAdapter.OnItemClickListener {
            override fun onItemClick(holder: FieldAdapter.ViewHolder) {
                val field_id = fieldList[holder.absoluteAdapterPosition].id
                Toast.makeText(context, "TEST$field_id", Toast.LENGTH_LONG).show()
                val bundle = bundleOf("id" to field_id)

                findNavController().navigate(R.id.action_field_to_select, bundle)

            }
        }


        recyclerview.let {
            //adapterをセット
            it.adapter = adapter
            //layoutManagerをセット
            it.layoutManager = LinearLayoutManager(view?.context)
            //枠線を追加

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
         * @return A new instance of fragment QuizFieldFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuizFieldFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}