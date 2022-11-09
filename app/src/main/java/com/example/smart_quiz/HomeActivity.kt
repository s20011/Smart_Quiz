package com.example.smart_quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.smart_quiz.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //下部メニューコンポーネントの取得
        val bottomNavView: BottomNavigationView = binding.bottomNavBar
        //ナビゲーションフラグメント取得
        val navController = supportFragmentManager.findFragmentById(R.id.nav_fragment)
        //下部ニューとナビゲーションを関連付け
        setupWithNavController(bottomNavView, navController!!.findNavController())

    }
}