package com.example.smart_quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.smart_quiz.databinding.ActivityHomeBinding
import com.example.smart_quiz.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //ツールバーをアクションバーとしてセット
        val toolbar = binding.homeToolbar
        setSupportActionBar(toolbar)
        //下部メニューコンポーネントの取得
        val bottomNavView: BottomNavigationView = binding.bottomNavBar
        //ナビゲーションフラグメント取得
        val navController = supportFragmentManager.findFragmentById(R.id.nav_fragment)
        //下部ニューとナビゲーションを関連付け
        setupWithNavController(bottomNavView, navController!!.findNavController())

        binding.bottomNavBar.setOnItemReselectedListener { menuItem ->
            val menuItemIdToFragmentIdMap = mapOf(
                R.id.nav_menu_home to R.id.nav_menu_home,
                R.id.nav_menu_edit to R.id.nav_menu_edit,
                R.id.nav_menu_quiz_field to R.id.nav_menu_quiz_field,
                R.id.quizSelectFragment to R.id.nav_menu_quiz_field
            )

            val viewModel = MainViewModel()
            val nav = this.findNavController(R.id.nav_fragment)
            val rootDestinationIds = menuItemIdToFragmentIdMap.values
            val currentId = nav.currentDestination?.id
                ?: return@setOnItemReselectedListener
            val rootId = menuItemIdToFragmentIdMap[menuItem.itemId]
                ?: return@setOnItemReselectedListener


            if(currentId in rootDestinationIds){
                viewModel.reselectBottomNavigationItemOnRoot(rootId)
            }else {
                nav.popBackStack(rootId, false)
            }
        }

    }
}