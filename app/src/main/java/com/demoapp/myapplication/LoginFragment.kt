package com.demoapp.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.demoapp.myapplication.databinding.FragmentLoginBinding
import com.training.finalproject.utils.replaceFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class LoginFragment: Fragment() {
    private val viewModel by viewModels<LoginViewModel>()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val userNameTextFlow = MutableStateFlow("")
    private val passwordTextFlow = MutableStateFlow("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edtUsername.doOnTextChanged { text, _, _, _ ->
            userNameTextFlow.value = text.toString()
        }
        binding.edtPassword.doOnTextChanged { text, _, _, _ ->
            passwordTextFlow.value = text.toString()
        }
        val buttonStateFlow = userNameTextFlow.combine(passwordTextFlow) {text1, text2 ->
            text1.isNotEmpty() && text2.isNotEmpty()
        }
        buttonStateFlow.observe(viewLifecycleOwner){
            binding.btnLogin.isVisible = it
        }
        binding.btnLogin.setOnClickListener {
            viewModel.checkLogin(binding.edtUsername.toString(), binding.edtPassword.toString())

            Log.e("test", "in click")
        }
        viewModel.uiEvent.observe(viewLifecycleOwner){
            // xu ly su kien login success hoac fail
            when (it) {
                LoginUIEvent.LoginSuccess -> replaceFragment(ItemListFragment(), R.id.container, true)
            }
            Log.e("test", "in observer")
        }
    }
}