package com.example.smart_quiz.viewmodel

import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart_quiz.MainBottomNavigationSelectedItem
import com.example.smart_quiz.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val reselectedItemOnRootSource: MutableSharedFlow<MainBottomNavigationSelectedItem> =
        MutableSharedFlow()
    val reselectedItemOnRoot: SharedFlow<MainBottomNavigationSelectedItem> =
        reselectedItemOnRootSource.asSharedFlow()

    fun reselectBottomNavigationItemOnRoot(@IdRes selectedMenuId: Int){
        val reselected = when (selectedMenuId) {
            R.id.nav_menu_home -> MainBottomNavigationSelectedItem.HOME
            R.id.nav_menu_edit -> MainBottomNavigationSelectedItem.EDIT
            R.id.nav_menu_quiz_field -> MainBottomNavigationSelectedItem.QUIZ
            else -> return
        }

        viewModelScope.launch {
            reselectedItemOnRootSource.emit(reselected)
        }
    }
}