package com.rinoindraw.capstonerino.ui.login

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.rinoindraw.capstonerino.R
import com.rinoindraw.capstonerino.databinding.FragmentLoginBinding
import com.rinoindraw.capstonerino.ui.main.MainActivity
import com.rinoindraw.capstonerino.ui.main.MainActivity.Companion.EXTRA_TOKEN
import com.rinoindraw.capstonerino.utils.ConstVal.KEY_EMAIL
import com.rinoindraw.capstonerino.utils.ConstVal.KEY_IS_LOGIN
import com.rinoindraw.capstonerino.utils.ConstVal.KEY_USER_NAME
import com.rinoindraw.capstonerino.utils.SessionManager
import com.rinoindraw.capstonerino.utils.gone
import com.rinoindraw.capstonerino.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

@Suppress("DEPRECATION")
@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var pref: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SessionManager(requireContext())

        playAnimation()
        initAction()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAction() {
        binding.apply {
            tvToRegister.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment)
            )

            btnLogin.setOnClickListener {
                if (edtPassword.error.isNullOrEmpty() && edtEmail.error.isNullOrEmpty()) {
                    loginUser()
                    findNavController().popBackStack(R.id.loginFragment, false)
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_login_format),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun loginUser() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString()

        showLoading(true)

        lifecycleScope.launchWhenResumed {
            try {
                val result = loginViewModel.loginUser(email, password).first()
                result.onSuccess { response ->
                    val loginResult = response.loginResult
                    if (loginResult != null) {
                        val token = loginResult.token ?: ""
                        val username = loginResult.name

                        pref.saveAuthToken(token)
                        pref.setStringPreference(KEY_EMAIL, email)
                        pref.setStringPreference(KEY_USER_NAME, username!!)
                        pref.setBooleanPreference(KEY_IS_LOGIN, true)

                        Intent(requireContext(), MainActivity::class.java).also { intent ->
                            intent.putExtra(EXTRA_TOKEN, token)
                            intent.putExtra(KEY_USER_NAME, username)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                        Toast.makeText(
                            requireContext(),
                            getString(R.string.login_success_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.login_error_message),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    showLoading(false)
                }
                result.onFailure {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.login_error_message),
                        Snackbar.LENGTH_SHORT
                    ).show()

                    showLoading(false)
                }
            } catch (e: Exception) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.login_error_message),
                    Snackbar.LENGTH_SHORT
                ).show()

                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.show() else binding.progressBar.gone()
        if (isLoading) binding.bgDim.show() else binding.bgDim.gone()
        binding.edtEmail.isClickable = !isLoading
        binding.edtEmail.isEnabled = !isLoading
        binding.edtPassword.isClickable = !isLoading
        binding.edtPassword.isEnabled = !isLoading
        binding.btnLogin.isClickable = !isLoading
    }

}