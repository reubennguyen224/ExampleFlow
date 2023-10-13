package com.demoapp.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LoginViewModel: ViewModel() {
    val uiState = sharedEventFlow<Boolean>()

    fun checkLogin(userName: String, password: String){
        viewModelScope.launch(Dispatchers.IO){
            uiState.emit(true)
        }
    }
}

data class LoginUIState(
    val isLoginSuccess: Boolean = false,
)

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}