package com.example.smart_quiz.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.smart_quiz.R
import com.example.smart_quiz.databinding.FragmentHomeBinding
import com.example.smart_quiz.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSingInClient: GoogleSignInClient
    private lateinit var userInfo: User

    private val binding get() = _binding!!

    private val signInActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            if(result.data != null){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try{
                    //Googleサインインが成功したので、Firebaseで認証する。
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    //Googleサインインに失敗し、UIを適切に更新する
                    Log.w(TAG, "Google sing in failed", e)
                }
            }

        }

    }

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        //Googleサインインを設定する
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSingInClient = GoogleSignIn.getClient(requireActivity(), gso)

        //Firebase Authの初期化
        auth = Firebase.auth

        binding.btSignin.setOnClickListener {
            //サインインボタンが押されたときサインインする
            Log.d("GoogleSignInActivity", "START =======")
            signIn()
        }

        binding.btSignout.setOnClickListener {
            //サインアウトボタンが押されたときサインアウトする
            signOut()
        }

        binding.btCreateAcount.setOnClickListener {
            createUserInfo()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()

        //ユーザーがサインインしているかどうかを確認し、それに応じてUIを更新する
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //
    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    //サインインに成功したときユーザーの情報をUIに反映
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    readUserInfo()
                    updateUI(user)
                } else {
                    //サインインに失敗したときメッセージを送る
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }

            }
    }

    //
    private fun signIn() {
        val signInIntent = googleSingInClient.signInIntent
        signInActivityLauncher.launch(signInIntent)
    }

    //
    private fun signOut() {
        Firebase.auth.signOut()
        updateUI(auth.currentUser)
    }

    //ログイン状況によってUIを変更
    private fun updateUI(user: FirebaseUser?){
        if(user == null) {
            binding.txName.text = "NULL"
            binding.txEmail.text = "NULL"
        } else {
            binding.txName.text = user.displayName.toString()
            binding.txEmail.text = user.email.toString()

            readUserInfo()
        }
    }

    //user情報を読み込む
    private fun readUserInfo() {
        val refUser = Firebase.database.getReference("users")
        refUser.child(auth.uid.toString())
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    Log.d("HomeFragment", "UserInfo = $user")
                    if(user == null){
                        createUserInfo()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("HomeFragment", "ERROR")
                }
            })
    }

    private fun createUserInfo() {
        val refUser = Firebase.database.getReference("users")
        val refScore = Firebase.database.getReference("Score")
        val newPostRef = refScore.push()
        val user = auth.currentUser
        val scoreId =newPostRef.key
        refUser.child(auth.uid.toString()).setValue(
            User(
                name = user!!.displayName.toString(),
                e_mail = user.email.toString(),
                s_id = scoreId.toString()

            )
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */

        private const val MESSAGE_NOT_LOGIN = "ログインしてください"
        private const val RC_SIGN_IN = 100
        private const val TAG = "GoogleActivity"

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}