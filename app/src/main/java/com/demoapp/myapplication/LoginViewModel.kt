package com.demoapp.myapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LoginViewModel: ViewModel() {
    val uiEvent = sharedEventFlow<LoginUIEvent>()

    fun checkLogin(userName: String, password: String){
        viewModelScope.launch(Dispatchers.IO){
            uiEvent.emit(LoginUIEvent.LoginSuccess)
        }
    }
}

data class LoginUIState(
    val isLoginSuccess: Boolean = false,
)
sealed class LoginUIEvent {
    object LoginSuccess: LoginUIEvent()
}

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}