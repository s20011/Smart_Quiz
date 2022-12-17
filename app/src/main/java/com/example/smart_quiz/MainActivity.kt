package com.example.smart_quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smart_quiz.databinding.ActivityMainBinding
import com.example.smart_quiz.model.User
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.math.sign

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val REQUEST_CODE_AUTH = 100
    private lateinit var auth: FirebaseAuth

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btStart.setOnClickListener {
            val intent = Intent(this@MainActivity,
                HomeActivity::class.java
            )
            startActivity(intent)
            finish()
        }
    }

    private fun signIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signInIntent)

    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK){
            val user = FirebaseAuth.getInstance().currentUser
        }
    }

    private fun createUserInfo() {
        val refUser = Firebase.database.getReference("users")
        val refScore = Firebase.database.getReference("Score")
        val newPostRef = refScore.push()
        val user = FirebaseAuth.getInstance().currentUser
        val scoreId =newPostRef.key
        refUser.child(user!!.uid.toString()).setValue(
            User(
                name = user.displayName.toString(),
                e_mail = user.email.toString(),
                s_id = scoreId.toString()

            )
        )
    }

}